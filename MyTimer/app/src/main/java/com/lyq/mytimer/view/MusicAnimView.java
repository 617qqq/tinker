package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lyq.mytimer.R;

import java.util.ArrayList;
import java.util.Random;

public class MusicAnimView extends View {

	private float mWidth, mHeight;
	private int mRadius;
	private int mCenterRadius;
	private ArrayList<Circle> mCircle = new ArrayList<>();
	private Paint mPaint;
	private Paint mPaintCentre;
	private Paint mPaintPoint;
	private Path mPath = new Path();


	public MusicAnimView(Context context) {
		super(context);
		init();
	}

	public MusicAnimView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MusicAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mRadius = (int) (Math.min(mWidth, mHeight) / 2);
		mCenterRadius = mRadius * 3 / 5;
		buildCircle();
		if (mPaintCentre == null) {
			mPaintCentre = new Paint();
			mPaintCentre.setAntiAlias(true);
			BitmapShader shader = new BitmapShader(getBitmap(),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			mPaintCentre.setShader(shader);
			mPaintCentre.setStyle(Paint.Style.FILL);
		}
	}

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

	private int resId = R.drawable.rect_re;

	private Bitmap getBitmap() {
		try {
			Drawable drawable = getResources().getDrawable(resId);
			Bitmap bitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, BITMAP_CONFIG);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds((int) mWidth / 2 - mCenterRadius
					, (int) mHeight / 2 - mCenterRadius
					, (int) mWidth / 2 + mCenterRadius
					, (int) mHeight / 2 + mCenterRadius);
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void buildCircle() {
		for (int i = 0; i < 3; i++) {
			mCircle.add(Circle.build(mCenterRadius, i));
		}
	}

	long startTime;

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.STROKE);

		mPaintPoint = new Paint();
		mPaintPoint.setAntiAlias(true);
		mPaintPoint.setStyle(Paint.Style.FILL);

		startTime = System.currentTimeMillis();
	}

	public void setBitmap(int resId){
		this.resId = resId;
		BitmapShader shader = new BitmapShader(getBitmap(),
				Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mPaintCentre.setShader(shader);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCircle(canvas);
		drawBorder(canvas);
		drawCentre(canvas);
		postInvalidate();
	}

	private void drawBorder(Canvas canvas) {
		mPaintPoint.setColor(Color.parseColor("#44000000"));
		canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius + 15, mPaintPoint);
	}

	private void drawCentre(Canvas canvas) {
		float offset = (float) (System.currentTimeMillis() - startTime) / 10000f;
		canvas.save();
		canvas.translate(mWidth / 2, mHeight / 2);
		canvas.rotate(360 * offset % 360);
		canvas.translate(-mWidth / 2, -mHeight / 2);
		canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius, mPaintCentre);
		canvas.restore();
	}

	private void drawCircle(Canvas canvas) {
		for (Circle item : mCircle) {
			float offset = (float) (System.currentTimeMillis() - item.buildTime) / 3000.0f;
			float radius = (mRadius - mCenterRadius) * offset + item.radius;
			if (radius < mRadius) {
				mPath.reset();
				mPath.addCircle(mWidth / 2, mHeight / 2, radius, Path.Direction.CW);

				int color = getColor(radius);
				mPaint.setColor(color);
				mPaintPoint.setColor(color);

				canvas.drawPath(mPath, mPaint);

				canvas.save();
				canvas.translate(mWidth/2, mHeight/2);
				canvas.rotate(item.pointAngle + 90 * offset);
				canvas.drawCircle(radius, 0 , item.pointRadius, mPaintPoint);
				canvas.restore();
			} else {
				item.reset(mCenterRadius);
			}
		}
	}

	private int getColor(float radius) {
		int i = (int) (radius - mCenterRadius) / ((mRadius - mCenterRadius) / 6);
		return Circle.color[Math.max(0, Math.min(i, 5))];
	}

	static class Circle {
		float pointAngle;
		float pointRadius;
		float radius;
		long buildTime;

		static int count = 6;

		static int[] color = {
				Color.parseColor("#AAFFFFFF"),
				Color.parseColor("#88FFFFFF"),
				Color.parseColor("#66FFFFFF"),
				Color.parseColor("#44FFFFFF"),
				Color.parseColor("#22FFFFFF"),
				Color.parseColor("#00FFFFFF"),
		};

		static Random random = new Random();

		public Circle(float pointAngle, float pointRadius, float radius, long buildTime) {
			this.pointAngle = pointAngle;
			this.pointRadius = pointRadius;
			this.radius = radius;
			this.buildTime = buildTime;
		}

		static Circle build(int maxRadius, int index) {
			int itemRadius = maxRadius / count;
			float randomRadius = maxRadius + random.nextInt(itemRadius / 3) - itemRadius / 3;
			long time = System.currentTimeMillis() + 1000 * index;

			return new Circle(
					(float) random.nextInt(360),
					random.nextInt(8) + random.nextFloat() + 5,
					randomRadius, time
			);
		}

		public void reset(int maxRadius) {
			int itemRadius = maxRadius / count;
			radius = maxRadius + random.nextInt(itemRadius / 3) - itemRadius / 3;
			buildTime = System.currentTimeMillis();
			pointRadius = random.nextInt(8) + random.nextFloat() + 5;
			pointAngle = random.nextInt(360);
		}
	}
}
