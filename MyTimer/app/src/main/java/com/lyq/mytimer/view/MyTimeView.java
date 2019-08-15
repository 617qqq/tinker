package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyTimeView extends View {

	private float mWidth, mHeight;
	private float mHourR, mMinuteR, mSecondR;
	private Paint mPaint = new Paint();
	private Calendar mCalendar, mResumeCalendar;
	private SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm MM.dd  EEE", Locale.CHINA);
	private String[] sNum = {"", "一", "二", "三", "四", "五"
			, "六", "七", "八", "九", "十"};
	private int mLastX;
	private int mLastY;
	//最后点击坐标
	private int mDownX;
	private int mDownY;
	//最后事件时间
	private long mLastTime;
	//最后点击时间
	private long mDownTime;
	//手动旋转角度
	private float degreesMin, degreesSecond;
	//是否是旋转中
	private boolean isTouching;
	//是否是在回复中
	private boolean isResume;
	//是否开始恢复
	private boolean isStartResume;
	//旋转中线条渐变
	private LinearGradient mSweepGradient;
	//回复的角度
	private float mResumeDegreesMin, mResumeDegreesSecond;
	//回复开始时间
	private long mResumeTime;

	public MyTimeView(Context context) {
		super(context);
		init();
	}

	public MyTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	private void init() {
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mResumeCalendar = Calendar.getInstance();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

		mHourR = mWidth * 0.143f;
		mMinuteR = mWidth * 0.35f;
		mSecondR = mWidth * 0.35f;
		if (mSweepGradient == null) {
			int[] colors = {Color.TRANSPARENT, Color.RED};
			mSweepGradient = new LinearGradient(0, 0, mWidth, 0
					, colors, null, Shader.TileMode.CLAMP);
		}
	}

	private boolean isPointInMinuteCircle(int x, int y) {
		boolean isXIn = Math.abs(x - mWidth / 2) < mMinuteR;
		boolean isYIn = Math.abs(y - mHeight / 2) < mMinuteR;
		return isXIn && isYIn;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);

		mCalendar = Calendar.getInstance();
		if (isTouching && mCalendar.getTimeInMillis() - mLastTime > 5000) {
			isTouching = false;
			isResume = true;
			isStartResume = true;
		}
		if (isResume && mCalendar.getTimeInMillis() - mLastTime > 6000) {
			isResume = false;
			degreesMin = 0;
			degreesSecond = 0;
		}
		if (isTouching || isResume) {
			mCalendar.setTimeInMillis(mDownTime);
		}
		if (isStartResume) {
			initResumeParam();
			isStartResume = false;
		}
		canvas.save();
		canvas.translate(mWidth / 2, mHeight / 2);

		if (isTouching) {
			mPaint.setShader(mSweepGradient);
			mPaint.setStrokeWidth(mHourR * 0.2f);
			canvas.drawLine(0, 0, mWidth / 2, 0, mPaint);
			mPaint.setShader(null);
		}

		mPaint.setColor(Color.WHITE);

		drawCenterInfo(canvas);
		drawHour(canvas);
		drawMinute(canvas);
		drawSecond(canvas);

		canvas.restore();
		invalidate();
	}

	private void initResumeParam() {
		mResumeTime = System.currentTimeMillis() + 1000;
		mResumeCalendar.setTimeInMillis(mResumeTime);
		int minOffset = 6 * (mResumeCalendar.get(Calendar.MINUTE) - mCalendar.get(Calendar.MINUTE));
		mResumeDegreesMin = degreesMin + minOffset;
		mResumeDegreesMin = mResumeDegreesMin % 360;
		//当前秒数相较DOWN事件的秒数，相差的角度
		int secondOffset = 6 * (mResumeCalendar.get(Calendar.SECOND) - mCalendar.get(Calendar.SECOND));
		//1秒内需要旋转的角度
		mResumeDegreesSecond = degreesSecond + secondOffset;
		mResumeDegreesSecond = mResumeDegreesSecond % 360;
	}

	private void drawSecond(Canvas canvas) {
		canvas.save();
		int second = mCalendar.get(Calendar.SECOND);

		mPaint.setTextSize(mHourR * 0.2f);
		mPaint.setTextAlign(Paint.Align.LEFT);

		mPaint.setAlpha(125);
		int offset = 0;
		if (isTouching) {
			canvas.rotate(degreesSecond);
		} else if (isResume) {
			canvas.rotate(degreesSecond - mResumeDegreesSecond * (System.currentTimeMillis() + 1000 - mResumeTime) / 1000);
		} else {
			offset = mCalendar.get(Calendar.MILLISECOND);
			offset = offset < 500 ? 0 : (offset - 500);
			canvas.rotate(-offset / 500f * 6);
		}
		for (int i = 0; i < 60; i++) {
			canvas.rotate(6);
			second = ++second % 60;
			if (i == 59 && offset == 0 && !isResume) {
				mPaint.setAlpha(255);
			}
			canvas.drawText(getTimeChinese(second, "秒"), mSecondR, mHourR * .08f, mPaint);
		}
		canvas.restore();
	}

	private void drawMinute(Canvas canvas) {
		canvas.save();
		int min = mCalendar.get(Calendar.MINUTE);

		mPaint.setTextSize(mHourR * 0.2f);
		mPaint.setTextAlign(Paint.Align.RIGHT);

		mPaint.setAlpha(125);
		boolean isRotating = false;
		if (isTouching) {
			canvas.rotate(degreesMin);
		} else if (isResume) {
			canvas.rotate(degreesMin - mResumeDegreesMin * (System.currentTimeMillis() + 1000 - mResumeTime) / 1000);
		} else {
			if (mCalendar.get(Calendar.SECOND) == 59) {
				canvas.rotate(-mCalendar.get(Calendar.MILLISECOND) / 1000f * 6);
				isRotating = true;
			}
		}
		for (int i = 0; i < 60; i++) {
			canvas.rotate(6);
			min = ++min % 60;
			if (i == 59 && !isRotating && !isResume) {
				mPaint.setAlpha(255);
			}
			canvas.drawText(getTimeChinese(min, "分"), mMinuteR, mHourR * .08f, mPaint);
		}
		canvas.restore();
	}

	private void drawHour(Canvas canvas) {
		canvas.save();
		int hour = mCalendar.get(Calendar.HOUR);

		mPaint.setTextSize(mHourR * 0.25f);
		mPaint.setTextAlign(Paint.Align.CENTER);

		mPaint.setAlpha(125);
		boolean isRotating = false;
		if (mCalendar.get(Calendar.MINUTE) == 59 && mCalendar.get(Calendar.SECOND) == 59) {
			canvas.rotate(-mCalendar.get(Calendar.MILLISECOND) / 1000f * 30);
			isRotating = true;
		}
		for (int i = 0; i < 12; i++) {
			canvas.rotate(30);
			hour = ++hour % 12;
			hour = hour == 0 ? 12 : hour;
			if (i == 11 && !isRotating) {
				mPaint.setAlpha(255);
			}
			canvas.drawText(getTimeChinese(hour, "点"), mHourR, mHourR * .1f, mPaint);
		}
		canvas.restore();
	}

	private String getTimeChinese(int min, String unit) {
		if (min == 0) {
			return "";
		} else if (min >= 20) {
			return sNum[min / 10] + sNum[10] + sNum[min % 10] + unit;
		} else if (min >= 10) {
			return sNum[10] + sNum[min % 10] + unit;
		} else {
			return sNum[min % 10] + unit;
		}
	}

	private void drawCenterInfo(Canvas canvas) {
		String time = dataFormat.format(mCalendar.getTime());

		mPaint.setTextSize(mHourR * 0.4f);
		mPaint.setAlpha(255);
		mPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(time.substring(0, 6), 0, 0, mPaint);

		mPaint.setTextSize(mHourR * .2f);
		canvas.drawText(time.substring(6), 0, mHourR * .2f, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		mLastTime = System.currentTimeMillis();
		boolean isHandle = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isHandle = true;
				mDownX = x;
				mDownY = y;
				if (!isTouching) {
					mDownTime = mLastTime;
					isTouching = true;
					computeEnterTime();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				boolean pointInMinuteCircle = isPointInMinuteCircle(mDownX, mDownY);
				float offset = (float) Math.sqrt(Math.pow(y - mLastY, 2) + Math.pow(x - mLastX, 2))
						/ (pointInMinuteCircle ? 5f : 9f);
				offset = getOffset(x, y, offset);
				if (pointInMinuteCircle) {
					degreesMin += offset;
				} else {
					degreesSecond += offset;
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				isHandle = true;
				if (onKeyConfirm != null) {
					if (targetDegreesMin - targetDegreesOffset < degreesMin
							&& degreesMin < targetDegreesMin + targetDegreesOffset
							&& targetDegreesSecond - targetDegreesOffset < degreesSecond
							&& degreesSecond < targetDegreesSecond + targetDegreesOffset
					) {
						onKeyConfirm.on(true);
					} else {
						onKeyConfirm.on(false);
					}
				}
				break;
		}
		mLastX = x;
		mLastY = y;
		return isHandle;
	}

	private boolean isLeftRadius(int x) {
		return x < mWidth / 2;
	}

	private boolean isTopRadius(int y) {
		return y < mHeight / 2;
	}

	private float getOffset(int x, int y, float offset) {
		if (Math.abs(x - mLastX) > Math.abs(y - mLastY)) {
			offset = x > mLastX ? -offset : offset;
			return isTopRadius(mLastY) ? -offset : offset;
		} else {
			offset = y < mLastY ? -offset : offset;
			return isLeftRadius(mLastX) ? -offset : offset;
		}
	}

	private float targetDegreesMin, targetDegreesSecond, targetDegreesOffset = 1.5f;
	private Calendar downCalendar = Calendar.getInstance();
	private OnKeyConfirm onKeyConfirm;

	private void computeEnterTime() {
		int targetMin = 6, targetSecond = 17;
		downCalendar.setTimeInMillis(mDownTime);
		targetDegreesMin = -6 * (targetMin - downCalendar.get(Calendar.MINUTE));
		targetDegreesSecond = -6 * (targetSecond - downCalendar.get(Calendar.SECOND));
	}

	public interface OnKeyConfirm {
		void on(boolean isConfirm);
	}

	public void setOnKeyConfirm(OnKeyConfirm onKeyConfirm) {
		this.onKeyConfirm = onKeyConfirm;
	}
}
