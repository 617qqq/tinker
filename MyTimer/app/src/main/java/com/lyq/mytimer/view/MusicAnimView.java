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

	/** 中心圆旋转一周所用时间 */
	private static float TIME_MIN_CIRCLE_ANGLE = 10000.0f;
	/** 线从出现到消失所用时间 */
	private static float TIME_CIRCLE_RADIUS = 3000.0f;

	private float mWidth, mHeight;
	/** 最大半径 */
	private int mRadius;
	/** 中心圆半径 */
	private int mCenterRadius;
	/** 圆圈对象 */
	private ArrayList<Circle> mCircle = new ArrayList<>();
	/** 圆圈画笔 */
	private Paint mPaint;
	/** 中心画笔 */
	private Paint mPaintCenter;
	/** 小圆点和中心轮廓的画笔 */
	private Paint mPaintPoint;
	/** 暂停时中心转动角度 */
	private float centerLastAngle;
	/** 中心转动开始时间戳。暂停时会标记时间戳为0，为0时使用最后的角度 */
	private long centerAnimTime;
	/** 最后生成圆圈的时间 */
	private static long lastResetTime;
	/** 默认图片 */
	private int resId = R.drawable.rect_re;
	/** 是否正在播放 */
	private boolean isPlaying;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

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
		centerAnimTime = lastResetTime = System.currentTimeMillis();
		for (int i = 0; i < Circle.MAX_NUM; i++) {
			mCircle.add(Circle.build());
		}
	}

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
		//暂停时会startTime戳为0，为0时使用最后的角度
		float angle;
		if (centerAnimTime != 0) {
			float offset = (float) (time - centerAnimTime) / TIME_MIN_CIRCLE_ANGLE;
			angle = (centerLastAngle + 360 * offset) % 360;
		} else {
			angle = centerLastAngle;
		}
		if (! isPlaying) {
			//暂停，标记时间戳为0，记录最后的角度
			centerLastAngle = angle;
			centerAnimTime = 0;
		}
		canvas.save();
		canvas.translate(mWidth / 2, mHeight / 2);
		canvas.rotate(angle);
		canvas.translate(- mWidth / 2, - mHeight / 2);
		canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius, mPaintCenter);
		canvas.restore();
	}

	private void drawCircle(Canvas canvas, long curTime) {
		for (Circle item : mCircle) {
			float radius = item.getCurRadius(curTime, mCenterRadius, mRadius);
			if (radius < mRadius) {
				if (! isPlaying && radius < mCenterRadius) {
					item.buildTime = 0;
				} else {
					int alpha = 255 - (int) (255f * (curTime - item.buildTime) / TIME_CIRCLE_RADIUS);
					mPaint.setAlpha(alpha);
					mPaintPoint.setAlpha(alpha);
					//绘制圆圈
					canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);
					//绘制小圆点
					canvas.save();
					canvas.translate(mWidth / 2, mHeight / 2);
					canvas.rotate(item.getPointAngle(curTime));
					canvas.drawCircle(radius, 0, item.pointRadius, mPaintPoint);
					canvas.restore();
				}
			} else if (isPlaying) {
				item.reset();
			}
		}
	}

	public void toPrevious(int bitmapId) {

	}

	public void updateState(boolean isPlaying) {
		this.isPlaying = isPlaying;
		if (isPlaying) {
			centerAnimTime = lastResetTime = System.currentTimeMillis();
		}
		postInvalidate();
	}

	public void toNext(int bitmapId) {

	}

	static class Circle {
		/** 小圆点所在角度 */
		float pointAngle;
		/** 小圆点半径 */
		float pointRadius;
		/** 重置时间（出现时间） */
		long buildTime;
		/** 圆圈最大数量，实际为MAX_NUM-1，因为只有随机数全部为0时才能同时显示4条 */
		private static final int MAX_NUM = 4;
		/** 圆圈间隔 */
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

		/**
		 * @return TIME_CIRCLE_RADIUS 时间内半径增加的是 maxRadius - centerRadius
		 */
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
	}
}
