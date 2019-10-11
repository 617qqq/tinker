package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	/** 最大半径 */
	private int mRadius;
	/** 中心圆半径 */
	private int mCenterRadius;
	private ArrayList<Circle> mCircle = new ArrayList<>();
	private Paint mPaint;
	private Paint mPaintCenter;
	private Paint mPaintPoint;

	/** 中心圆旋转一周所用时间 */
	private static float TIME_MIN_CIRCLE_ANGLE = 10000.0f;
	/** 线从出现到消失所用时间 */
	private static float TIME_CIRCLE_RADIUS = 3000.0f;

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
		if (mPaintCenter == null) {
			mPaintCenter = new Paint();
			mPaintCenter.setAntiAlias(true);
			BitmapShader shader = new BitmapShader(getBitmap(),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			mPaintCenter.setShader(shader);
			mPaintCenter.setStyle(Paint.Style.FILL);
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
		mCircle.clear();
		startTime = lastResetTime = System.currentTimeMillis();
		for (int i = 0; i < Circle.MAX_NUM; i++) {
			mCircle.add(Circle.build());
		}
	}

	long startTime;

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.WHITE);

		mPaintPoint = new Paint();
		mPaintPoint.setAntiAlias(true);
		mPaintPoint.setStyle(Paint.Style.FILL);
		mPaintPoint.setColor(Color.WHITE);
	}

	public void setBitmap(int resId) {
		this.resId = resId;
		BitmapShader shader = new BitmapShader(getBitmap(),
				Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mPaintCenter.setShader(shader);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long curTime = System.currentTimeMillis();

		drawCircle(canvas, curTime);
		drawBorder(canvas);
		drawCenter(canvas, curTime);
		postInvalidate();
	}

	private void drawBorder(Canvas canvas) {
		mPaintPoint.setAlpha(70);
		canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius + 15, mPaintPoint);
	}

	private void drawCenter(Canvas canvas, long time) {
		float offset = (float) (time - startTime) / TIME_MIN_CIRCLE_ANGLE;
		canvas.save();
		canvas.translate(mWidth / 2, mHeight / 2);
		canvas.rotate(360 * offset % 360);
		canvas.translate(- mWidth / 2, - mHeight / 2);
		canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius, mPaintCenter);
		canvas.restore();
	}

	private void drawCircle(Canvas canvas, long curTime) {
		for (Circle item : mCircle) {
			float radius = item.getCurRadius(curTime, mCenterRadius, mRadius);
			if (radius < mRadius) {
				int alpha = 255 - (int) (255f * (curTime - item.buildTime) / TIME_CIRCLE_RADIUS);
				mPaint.setAlpha(alpha);
				mPaintPoint.setAlpha(alpha);

				canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);

				canvas.save();
				canvas.translate(mWidth / 2, mHeight / 2);
				canvas.rotate(item.getPointAngle(curTime));
				canvas.drawCircle(radius, 0, item.pointRadius, mPaintPoint);
				canvas.restore();
			} else {
				item.reset();
			}
		}
	}

	public static long lastResetTime;

	static class Circle {
		float pointAngle;
		float pointRadius;
		long buildTime;

		private static final int MAX_NUM = 4;
		private static float TIME_DIVIDE = TIME_CIRCLE_RADIUS / MAX_NUM;

		static Random random = new Random();

		Circle(float pointAngle, float pointRadius, long buildTime) {
			this.pointAngle = pointAngle;
			this.pointRadius = pointRadius;
			this.buildTime = buildTime;
		}

		static Circle build() {
			resetBuildTime();
			return new Circle(
					(float) random.nextInt(360),
					random.nextInt(8) + random.nextFloat() + 5,
					lastResetTime
			);
		}

		float getCurRadius(long curTime, float centerRadius, float maxRadius) {
			float offset = (float) (curTime - buildTime) / TIME_CIRCLE_RADIUS;
			return (maxRadius - centerRadius) * offset + centerRadius;
		}

		/**
		 * @return TIME_CIRCLE_RADIUS 时间内移动弧度为90度
		 */
		float getPointAngle(long curTime) {
			float offset = (float) (curTime - buildTime) / TIME_CIRCLE_RADIUS;
			return pointAngle + 90 * offset;
		}

		void reset() {
			resetBuildTime();

			buildTime = lastResetTime;
			pointRadius = random.nextInt(8) + random.nextFloat() + 5;
			pointAngle = random.nextInt(360);
		}

		private static void resetBuildTime() {
			lastResetTime = lastResetTime
					+ (long) TIME_DIVIDE
					+ random.nextInt((int) TIME_DIVIDE / 2);
		}
	}
}
