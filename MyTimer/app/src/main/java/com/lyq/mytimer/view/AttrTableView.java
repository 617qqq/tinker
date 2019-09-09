package com.lyq.mytimer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class AttrTableView extends View {

	private int SIZE_ADAPTER = 5;

	private float mWidth, mHeight;
	private int mRadius;
	private Paint mPaintBackGround = new Paint();
	private Paint mPaintFrame = new Paint();
	private Paint mPaintLabel = new Paint();
	private Paint mPaintValue = new Paint();
	private Path mOutBorderPath = new Path();
	private Path mValuePath = new Path();
	private float mAnimatorOffset = 1f;
	private int[] mValueRadius = new int[SIZE_ADAPTER];
	private Point[][] points = new Point[5][SIZE_ADAPTER];
	private Point[] pointValue = new Point[SIZE_ADAPTER];
	private Shader gradient;
	private ValueAnimator animator;

	private double SIN72, COS72, SIN36, COS36;
	private ArrayList<Integer> colors = new ArrayList<>();
	private int cFrame = Color.parseColor("#44FFFFFF");
	private int cValue = Color.parseColor("#4400E5EE");
	private int cValueLine = Color.parseColor("#00e4ee");
	private int pxTextSize = 39;
	private int cTextColor = Color.BLACK;


	private String[] label = {"温度", "湿度", "二氧化碳", "甲醛", "氧气"};
	private float[][] VALUE = {
			{10, 15, 22, 26, 30}
			, {10, 15, 22, 26, 30}
			, {10, 15, 22, 26, 30}
			, {10, 15, 22, 26, 30}
			, {10, 15, 22, 26, 30}};
	private ArrayList<Float> mValue = new ArrayList<>();
	private int GRADE_0 = 0, GRADE_1 = 1, GRADE_2 = 2;
	private int mGrade;

	public AttrTableView(Context context) {
		super(context);
		init();
	}

	public AttrTableView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AttrTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		int maxLabelIndex = label[1].length() < label[4].length() ? 1 : 4;
		mPaintBackGround.setTextSize(pxTextSize);
		float labelWidth = mPaintBackGround.measureText(label[maxLabelIndex]);
		mRadius = (int) (Math.min(mWidth, mHeight) / 2 - labelWidth);
		if (gradient == null) {
			int[] color = new int[colors.size()];
			int size = colors.size();
			for (int i = 0; i < size; i++) {
				color[i] = colors.get(i);
			}
			gradient = new RadialGradient(mWidth / 2, mHeight / 2, mRadius
					, color, null, Shader.TileMode.CLAMP);
//			gradient = new LinearGradient(mWidth / 2, mHeight / 2, mWidth / 2 + mRadius, mWidth / 2
//					, color
//					, null, Shader.TileMode.CLAMP);
//			gradient = new SweepGradient(mWidth / 2, mHeight / 2
//					, color
//					, null);
		}
	}

	private void init() {
		mPaintBackGround.setAntiAlias(true);
		SIN72 = Math.sin(72 * Math.PI / 180);
		COS72 = Math.cos(72 * Math.PI / 180);
		SIN36 = Math.sin(36 * Math.PI / 180);
		COS36 = Math.cos(36 * Math.PI / 180);

		colors.add(Color.parseColor("#54FF9F"));
		colors.add(Color.parseColor("#4EEE94"));

		mValue.add(9f);
		mValue.add(14f);
		mValue.add(23f);
		mValue.add(25f);
		mValue.add(100f);

		for (int i = 0; i < SIZE_ADAPTER; i++) {
			for (int j = 0; j < points[i].length; j++) {
				points[i][j] = new Point(0, 0);
			}
			pointValue[i] = new Point(0, 0);
		}

		mPaintBackGround.setStyle(Paint.Style.FILL);
		mPaintBackGround.setColor(Color.parseColor("#44000000"));

		mPaintFrame.setStrokeWidth(2);
		mPaintFrame.setStyle(Paint.Style.STROKE);
		mPaintFrame.setColor(cFrame);

		mPaintLabel.setTextSize(pxTextSize);
		mPaintLabel.setColor(cTextColor);
		mPaintLabel.setStyle(Paint.Style.FILL);

		startAnimation();
	}

	public void startAnimation() {
		if (animator == null) {
			animator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					mAnimatorOffset = (float) animation.getAnimatedValue();
					postInvalidate();
				}
			});
		}
		animator.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int centerX = (int) mWidth / 2;
		int centerY = (int) mHeight / 2;
		initPoint(centerX, centerY);
		drawBackGround(canvas);
		drawLabel(canvas, centerX, centerY);
		drawFrame(canvas, centerX, centerY);
		drawValue(canvas, centerX, centerY);
	}

	private void drawValue(Canvas canvas, int centerX, int centerY) {
		for (int i = 0; i < SIZE_ADAPTER; i++) {
			int subIndex = getValueSubIndex(i, mValue.get(i), 1);
			setGrade(subIndex);
			pointValue[i] = points[subIndex][i];
			setValueRadius(i, subIndex);
		}
		initValuePoint(centerX, centerY);
		mValuePath.reset();
		for (int i = 0; i < SIZE_ADAPTER; i++) {
			if (i == 0) {
				mValuePath.moveTo(pointValue[0].x, pointValue[0].y);
			} else {
				mValuePath.lineTo(pointValue[i].x, pointValue[i].y);
			}
		}
		mValuePath.close();

		mPaintValue.setStyle(Paint.Style.FILL);
		mPaintValue.setColor(cValue);
		canvas.drawPath(mValuePath, mPaintValue);

		mPaintValue.setStrokeWidth(3);
		mPaintValue.setStyle(Paint.Style.STROKE);
		mPaintValue.setColor(cValueLine);
		canvas.drawPath(mValuePath, mPaintValue);
	}

	private void setValueRadius(int i, int subIndex) {
		if (subIndex == 0) {
			mValueRadius[i] = 0;
		}
		float remainOffset = (mValue.get(i) - VALUE[i][subIndex - 1]) / (VALUE[i][subIndex] - VALUE[i][subIndex - 1]);
		remainOffset = Math.max(0, Math.min(remainOffset, 1));
		mValueRadius[i] = (int) (mRadius * 0.2 * (subIndex - 1 + remainOffset));
	}

	private void setGrade(int subIndex) {
		if (mGrade == GRADE_2) {
			return;
		}
		if (subIndex == 1 || subIndex == 5) {
			mGrade = GRADE_2;
			return;
		}
		if (mGrade == GRADE_1) {
			return;
		}
		if (subIndex == 2 || subIndex == 4) {
			mGrade = GRADE_1;
			return;
		}
		mGrade = GRADE_0;
	}

	public int getValueSubIndex(int valueIndex, float value, int subIndex) {
		if (value < VALUE[valueIndex][subIndex]) {
			return subIndex;
		} else {
			if (subIndex == 4) {
				return subIndex;
			} else {
				return getValueSubIndex(valueIndex, value, subIndex + 1);
			}
		}
	}

	private void drawLabel(Canvas canvas, int centerX, int centerY) {
		mPaintLabel.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(label[0], points[4][0].x, points[4][0].y - (float) pxTextSize / 3, mPaintLabel);
		canvas.drawText(label[2], points[4][2].x, points[4][2].y + pxTextSize, mPaintLabel);
		canvas.drawText(label[3], points[4][3].x, points[4][3].y + pxTextSize, mPaintLabel);

		mPaintLabel.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(label[1], points[4][1].x, points[4][1].y, mPaintLabel);
		mPaintLabel.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(label[4], points[4][4].x, points[4][4].y, mPaintLabel);
	}

	private void drawFrame(Canvas canvas, int centerX, int centerY) {
		for (int i = 0; i < SIZE_ADAPTER; i++) {
			canvas.drawLine(centerX, centerY, points[4][i].x, points[4][i].y, mPaintFrame);
		}

		for (int i = 0; i < 5; i++) {
			mOutBorderPath.moveTo(points[i][0].x, points[i][0].y);
			for (int j = 1; j < SIZE_ADAPTER; j++) {
				mOutBorderPath.lineTo(points[i][j].x, points[i][j].y);
			}
			mOutBorderPath.close();
		}
		canvas.drawPath(mOutBorderPath, mPaintFrame);
	}

	private void drawBackGround(Canvas canvas) {
		mOutBorderPath.reset();
		mOutBorderPath.moveTo(points[4][0].x, points[4][0].y);
		for (int i = 1; i < SIZE_ADAPTER; i++) {
			mOutBorderPath.lineTo(points[4][i].x, points[4][i].y);
		}
		mOutBorderPath.close();
		canvas.drawPath(mOutBorderPath, mPaintBackGround);
	}

	private void initPoint(int centerX, int centerY) {
		for (int i = 0; i < SIZE_ADAPTER; i++) {
			initPoint(i, centerX, centerY, (int) (mRadius * (0.2 * i)));
		}
	}

	private void initPoint(int index, int centerX, int centerY, int radius) {
		points[index][0].set(centerX, centerY - radius);
		points[index][1].set((int) (centerX + radius * SIN72), (int) (centerY - radius * COS72));
		points[index][2].set((int) (centerX + radius * SIN36), (int) (centerY + radius * COS36));
		points[index][3].set((int) (centerX - radius * SIN36), (int) (centerY + radius * COS36));
		points[index][4].set((int) (centerX - radius * SIN72), (int) (centerY - radius * COS72));
	}

	private void initValuePoint(int centerX, int centerY) {
		pointValue[0].set(centerX, centerY - (int) (mValueRadius[0] * mAnimatorOffset));
		pointValue[1].set((int) (centerX + mValueRadius[1] * mAnimatorOffset * SIN72), (int) (centerY - mValueRadius[1] * mAnimatorOffset * COS72));
		pointValue[2].set((int) (centerX + mValueRadius[2] * mAnimatorOffset * SIN36), (int) (centerY + mValueRadius[2] * mAnimatorOffset * COS36));
		pointValue[3].set((int) (centerX - mValueRadius[3] * mAnimatorOffset * SIN36), (int) (centerY + mValueRadius[3] * mAnimatorOffset * COS36));
		pointValue[4].set((int) (centerX - mValueRadius[4] * mAnimatorOffset * SIN72), (int) (centerY - mValueRadius[4] * mAnimatorOffset * COS72));
	}
}
