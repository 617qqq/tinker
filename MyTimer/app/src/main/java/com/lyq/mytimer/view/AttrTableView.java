package com.lyq.mytimer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lyq.attr_graph.AttrGraphView;

import java.util.ArrayList;

public class AttrTableView extends View {

	private int ATTR_SIZE = 5;
	private int FRAME_SIZE = 5;

	private float mWidth, mHeight;
	private int mRadius;
	private Paint mPaintBackGround = new Paint();
	private Paint mPaintFrame = new Paint();
	private Paint mPaintLabel = new Paint();
	private Paint mPaintValue = new Paint();
	private Path mOutBorderPath = new Path();
	private Path mValuePath = new Path();
	private int[] mValueRadius = new int[ATTR_SIZE];
	private Point[][] points = new Point[FRAME_SIZE][ATTR_SIZE];
	private Point[] pointValue = new Point[ATTR_SIZE];
	//不规则数据图形动画
	private ValueAnimator animator;
	private float mAnimatorOffset = 1f;
	//边框动画
	private ValueAnimator initAnimator;
	private Bitmap initBitmap;
	private Paint initPaint = new Paint();
	private int initAnimatorOffset;
	private RectF initRectF;
	private Shader initGradient;

	private int cFrame = Color.parseColor("#44FFFFFF");
	private int cValue = Color.parseColor("#4400E5EE");
	private int cValueLine = Color.parseColor("#00e4ee");
	private int pxTextSize = 39;
	private int cTextColor = Color.BLACK;

	private String[] label = {"温度", "湿度", "二氧化碳", "甲醛", "氧气"};
	private float[][] VALUE = {
			{10, 15, 22, 26, 28, 30}
			, {10, 15, 22, 26, 28, 30}
			, {10, 15, 22, 26, 28, 30}
			, {10, 15, 22, 26, 28, 30}
			, {10, 15, 22, 26, 28, 30}};
	private ArrayList<Float> mValue = new ArrayList<>();
	private final int GRADE_0 = 0, GRADE_1 = 1, GRADE_2 = 2;
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
	}

	private void init() {


		mPaintBackGround.setAntiAlias(true);

		mValue.add(25f);
		mValue.add(25f);
		mValue.add(25f);
		mValue.add(25f);
		mValue.add(25f);

		for (int i = 0; i < ATTR_SIZE; i++) {
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

		initPaint.setStyle(Paint.Style.FILL);

		startInitAnimation();
	}

	public String setData(ArrayList<Float> data) {
		mValue.clear();
		mValue.addAll(data);
		mGrade = GRADE_0;
		for (int i = 0; i < ATTR_SIZE; i++) {
			int subIndex = getValueSubIndex(i, mValue.get(i), 1);
			setGrade(subIndex);
		}
		startAnimation();
		return getGrade();
	}

	public String getGrade() {
		switch (mGrade) {
			case GRADE_0:
				return "优";
			case GRADE_1:
				return "良";
			case GRADE_2:
				return "差";
		}
		return "优";
	}

	public void startInitAnimation() {
		if (initAnimator == null) {
			initAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000);
			initAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					initAnimatorOffset = (int) animation.getAnimatedValue();
					postInvalidate();
				}
			});
			initAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					initBitmap.recycle();
					initBitmap = null;
					startAnimation();
				}
			});
		}
		initAnimator.start();
	}

	public void startAnimation() {
		if (animator == null) {
			animator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					mAnimatorOffset = (float) animation.getAnimatedValue();
					invalidate();
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
		if (initBitmap == null && initAnimatorOffset != 360) {
			initBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
			Canvas canvasTemp = new Canvas(initBitmap);
			drawBackGround(canvasTemp);
			drawFrame(canvasTemp, centerX, centerY);
			initGradient = new BitmapShader(initBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			initPaint.setShader(initGradient);
		} else if (initAnimatorOffset < 360) {
			if (initRectF == null) {
				initRectF = new RectF(centerX - mRadius, centerY - mRadius
						, centerX + mRadius, centerY + mRadius);
			}
			canvas.drawArc(initRectF, - 90, initAnimatorOffset, true, initPaint);
		} else {
			drawBackGround(canvas);
			drawLabel(canvas, centerX, centerY);
			drawFrame(canvas, centerX, centerY);
			//drawValue(canvas, centerX, centerY);
		}
	}

	private void drawValue(Canvas canvas, int centerX, int centerY) {
		mGrade = GRADE_0;
		for (int i = 0; i < ATTR_SIZE; i++) {
			int subIndex = getValueSubIndex(i, mValue.get(i), 1);
			setGrade(subIndex);
			setValueRadius(i, subIndex);
		}
		initValuePoint(centerX, centerY);
		mValuePath.reset();
		for (int i = 0; i < ATTR_SIZE; i++) {
			if (i == 0) {
				mValuePath.moveTo(pointValue[0].x, pointValue[0].y);
			} else {
				mValuePath.lineTo(pointValue[i].x, pointValue[i].y);
			}
		}
		mValuePath.close();

		mPaintValue.setStyle(Paint.Style.FILL);
		mPaintValue.setColor(getValueColor(false));
		canvas.drawPath(mValuePath, mPaintValue);

		mPaintValue.setStrokeWidth(3);
		mPaintValue.setStyle(Paint.Style.STROKE);
		mPaintValue.setColor(getValueColor(true));
		canvas.drawPath(mValuePath, mPaintValue);
	}

	public int getValueColor(boolean isLine) {
		switch (mGrade) {
			case GRADE_0:
				return Color.parseColor(isLine ? "#00EE76" : "#4400CD66");
			case GRADE_1:
				return isLine ? cValueLine : cValue;
			case GRADE_2:
				return Color.parseColor(isLine ? "#FF4040" : "#44EE3B3B");
		}
		return isLine ? cValueLine : cValue;
	}

	private void setValueRadius(int i, @IntRange (from = 1) int subIndex) {
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

	@IntRange (from = 1)
	public int getValueSubIndex(int valueIndex, float value, int subIndex) {
		if (value < VALUE[valueIndex][subIndex]) {
			return subIndex;
		} else {
			if (subIndex == VALUE[valueIndex].length - 1) {
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
		for (int i = 0; i < ATTR_SIZE; i++) {
			canvas.drawLine(centerX, centerY, points[4][i].x, points[4][i].y, mPaintFrame);
		}

		for (int i = 0; i < FRAME_SIZE; i++) {
			mOutBorderPath.moveTo(points[i][0].x, points[i][0].y);
			for (int j = 1; j < ATTR_SIZE; j++) {
				mOutBorderPath.lineTo(points[i][j].x, points[i][j].y);
			}
			mOutBorderPath.close();
		}
		canvas.drawPath(mOutBorderPath, mPaintFrame);
	}

	private void drawBackGround(Canvas canvas) {
		mOutBorderPath.reset();
		mOutBorderPath.moveTo(points[4][0].x, points[4][0].y);
		for (int i = 1; i < ATTR_SIZE; i++) {
			mOutBorderPath.lineTo(points[4][i].x, points[4][i].y);
		}
		mOutBorderPath.close();
		canvas.drawPath(mOutBorderPath, mPaintBackGround);
	}

	private void initPoint(int centerX, int centerY) {
		for (int i = 0; i < ATTR_SIZE; i++) {
			initPoint(points[i], centerX, centerY, (int) (mRadius * (0.2 + 0.2 * i)));
		}
	}

	private void initPoint(Point[] pointList, int centerX, int centerY, int radius) {
		for (int i = 0; i < ATTR_SIZE; i++) {
			int agree = 360 / ATTR_SIZE * i;
			double x = centerX + radius * Math.sin(agree * Math.PI / 180);
			double y = centerY - radius * Math.cos(agree * Math.PI / 180);
			pointList[i].set((int) x, (int) y);
		}
	}

	private void initValuePoint(int centerX, int centerY) {
		for (int i = 0; i < ATTR_SIZE; i++) {
			int agree = 360 / ATTR_SIZE * i;
			double radius = mValueRadius[i] * mAnimatorOffset;
			double x = centerX + radius * Math.sin(agree * Math.PI / 180);
			double y = centerY - radius * Math.cos(agree * Math.PI / 180);
			pointValue[i].set((int) x, (int) y);
		}
	}
}
