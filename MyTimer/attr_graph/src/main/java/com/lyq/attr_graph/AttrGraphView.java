package com.lyq.attr_graph;

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
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AttrGraphView extends View {

	/** 属性数量 */
	private int mAttrSize = 5;
	/** 边框行数 */
	private int mFrameSize = 5;
	/** 属性边框不透明度 */
	private float opacityStorke = 0.8f;
	/** 属性内容不透明度 */
	private float opacityContent = 0.6f;
	/** 未选中不透明度 */
	private float opacityUnSelect = 0.6f;

	/** 宽高 */
	private float mWidth, mHeight;
	/** 图标的中心点 */
	private int centerX, centerY;
	/** 半径 */
	private float mRadius;
	/** 边框画笔 */
	private Paint paintFrame = new Paint();
	/** 字体画笔 */
	private Paint paintLabel = new Paint();
	/** 内容画笔 */
	private Paint paintValueContent = new Paint();
	private Paint paintValueStorke = new Paint();
	/** 背景画笔 */
	private Paint paintBackGround = new Paint();
	/** 内容路径 */
	private ArrayList<Path> mPathList = new ArrayList<>();
	/** 边框Path */
	private Path pathFrame = new Path();

	/** 数值范围 */
	private List<List<Double>> mRegion = new ArrayList<>();
	/** 数值 */
	public List<List<Double>> mValue = new ArrayList<>();
	/** 各组数据颜色 */
	private ArrayList<Integer> colors = new ArrayList<>();

	/** 初始背景动画 */
	private ValueAnimator initAnimator;
	/** 初始背景Bitmap */
	private Bitmap initBitmap;
	/** 初始背景画笔 */
	private Paint initPaint = new Paint();
	/** 初始动画偏移量 */
	private int initAnimatorOffset;
	/** 初始背景的坐标 */
	private RectF initRectF;
	/** 初始背景Shader */
	private Shader initShader;

	/** 属性之间的夹角 */
	private float agree;
	/** 是否同步内容动画 */
	private boolean isDrawTogether;
	/** 内容动画偏移量, isDrawTogether为false时，3.5表示第4组数据0.5的偏移量 */
	private float mAnimatorOffset;
	/** 内容动画 */
	private ValueAnimator valueAnim;

	public AttrGraphView(Context context) {
		super(context);
		init(null);
	}

	public AttrGraphView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public AttrGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		paintBackGround.setStyle(Paint.Style.FILL);
		paintBackGround.setColor(Color.parseColor("#44000000"));

		paintFrame.setStrokeWidth(2);
		paintFrame.setStyle(Paint.Style.STROKE);
		paintFrame.setColor(Color.parseColor("#44FFFFFF"));

		paintLabel.setTextSize(39);
		paintLabel.setColor(Color.BLACK);
		paintLabel.setStyle(Paint.Style.FILL);

		initPaint.setStyle(Paint.Style.FILL);

		agree = 360f / mAttrSize;
		startInitAnimation();

		colors.add(Color.BLUE);
		colors.add(Color.RED);
		colors.add(Color.BLACK);
		colors.add(Color.GREEN);
		colors.add(Color.GRAY);

		paintValueContent.setAntiAlias(true);
		paintValueContent.setStyle(Paint.Style.FILL);

		paintValueStorke.setStyle(Paint.Style.STROKE);
		paintValueStorke.setStrokeWidth(10);
		paintValueStorke.setAntiAlias(true);
	}

	/**
	 * 开始初始化动画
	 */
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
					startAnimTogether();
				}
			});
		}
		if (initBitmap != null) {
			initBitmap.recycle();
			initBitmap = null;
		}
		initAnimator.start();
	}

	public void setAttrSize(int attrSize) {
		this.mAttrSize = attrSize;
		this.agree = 360f / mAttrSize;
		startInitAnimation();
	}

	public void setFrameSize(int frameSize) {
		this.mFrameSize = frameSize;
		startInitAnimation();
	}

	public int getAttrSize() {
		return mAttrSize;
	}

	public int getFrameSize() {
		return mFrameSize;
	}

	/**
	 * 设置各属性的范围
	 */
	public void setRegion(List<List<Double>> region) {
		mRegion.clear();
		mRegion.addAll(region);
		startAnimTogether();
	}

	/**
	 * 开始添加最后一组数据的动画
	 */
	public void startAddAnim() {
		startAnimation(mValue.size() - 1, mValue.size(), false);
	}

	/**
	 * 一起绘制所有数据
	 */
	public void startAnimTogether() {
		startAnimation(0, 1, true);
	}

	/**
	 * @param from           小于from的直接完成
	 * @param to             大于to的不绘制
	 * @param isDrawTogether 是否一起绘制
	 */
	public void startAnimation(float from, float to, boolean isDrawTogether) {
		if (valueAnim != null) {
			valueAnim.cancel();
		}
		valueAnim = ValueAnimator.ofFloat(from, to);
		valueAnim.setDuration(1000);
		valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				mAnimatorOffset = (float) valueAnimator.getAnimatedValue();
				postInvalidate();
			}
		});
		this.isDrawTogether = isDrawTogether;
		valueAnim.start();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		mRadius = (int) (Math.min(mWidth, mHeight) / 2);
		centerX = mWidth > mHeight ? (int) (getPaddingLeft() + mWidth / 2) : (int) (getPaddingLeft() + mRadius);
		centerY = mHeight > mWidth ? (int) (getPaddingTop() + mHeight / 2) : (int) (getPaddingTop() + mRadius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (initBitmap == null && initAnimatorOffset != 360) {
			initBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
			Canvas canvasTemp = new Canvas(initBitmap);
			drawBackGround(canvasTemp);
			drawFrame(canvasTemp);
			initShader = new BitmapShader(initBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			initPaint.setShader(initShader);
		} else if (initAnimatorOffset < 360) {
			if (initRectF == null) {
				initRectF = new RectF(centerX - mRadius, centerY - mRadius
						, centerX + mRadius, centerY + mRadius);
			}
			canvas.drawArc(initRectF, - 90, initAnimatorOffset, true, initPaint);
		} else {
			drawBackGround(canvas);
			drawFrame(canvas);
			drawValue(canvas);
		}
	}

	/**
	 * 绘制背景
	 */
	private void drawBackGround(Canvas canvas) {
		pathFrame.reset();
		pathFrame.moveTo(centerX, centerY - mRadius);
		for (int i = 1; i < mAttrSize; i++) {
			pathFrame.lineTo(pointX(i, mRadius), pointY(i, mRadius));
		}
		pathFrame.close();
		canvas.drawPath(pathFrame, paintBackGround);

	}

	/**
	 * 绘制边框
	 */
	private void drawFrame(Canvas canvas) {
		for (int i = 0; i < mAttrSize; i++) {
			canvas.drawLine(centerX, centerY, pointX(i, mRadius), pointY(i, mRadius), paintFrame);
		}

		for (int i = 0; i < mFrameSize; i++) {
			int radius = (int) (mRadius * (1.0 / mFrameSize * (i + 1)));
			pathFrame.moveTo(pointX(0, radius), pointY(0, radius));
			for (int j = 1; j < mAttrSize; j++) {
				pathFrame.lineTo(pointX(j, radius), pointY(j, radius));
			}
			pathFrame.close();
		}
		canvas.drawPath(pathFrame, paintFrame);
	}

	/**
	 * 绘制数据<br/>
	 * 如果一起绘制，mAnimatorOffset大于1时全部完成绘制<br/>
	 * 如果不一起绘制，mAnimatorOffset大于相应index直接绘制完成
	 */
	private void drawValue(Canvas canvas) {
		for (int valueIndex = 0; valueIndex < mValue.size(); valueIndex++) {
			List<Double> itemValue = mValue.get(valueIndex);
			setValuePaint(valueIndex);

			Path path;
			if (mPathList.size() > valueIndex) {
				path = mPathList.get(valueIndex);
			} else {
				path = new Path();
				mPathList.add(path);
			}
			path.reset();

			float offset = 0;
			if (isDrawTogether) {
				offset = Math.max(0, Math.min(mAnimatorOffset, 1));
			} else if (valueIndex <= ((int) mAnimatorOffset)) {
				offset = mAnimatorOffset - valueIndex;
				offset = Math.max(0, Math.min(offset, 1));
			}
			if (offset != 0) {
				float firstRadius = getValueRadius(0, itemValue.get(0)) * offset;
				path.moveTo(pointX(0, firstRadius), pointY(0, firstRadius));
				for (int attrIndex = 1; attrIndex < itemValue.size(); attrIndex++) {
					float itemRadius = getValueRadius(attrIndex, itemValue.get(attrIndex)) * offset;
					path.lineTo(pointX(attrIndex, itemRadius), pointY(attrIndex, itemRadius));
				}
				path.close();
				canvas.drawPath(path, paintValueContent);
				canvas.drawPath(path, paintValueStorke);
			}
		}
	}

	private void setValuePaint(int index) {
		paintValueContent.setColor(colors.get(index));
		paintValueStorke.setColor(colors.get(index));

		paintValueContent.setAlpha((int) (255 * opacityContent));
		paintValueStorke.setAlpha((int) (255 * opacityStorke));
	}

	private float pointY(int pointIndex, float radius) {
		return (float) (centerY - radius * Math.cos(agree * pointIndex * Math.PI / 180f));
	}

	private float pointX(int pointIndex, float radius) {
		return (float) (centerX + radius * Math.sin(agree * pointIndex * Math.PI / 180f));
	}

	/**
	 * 获取数据的半径
	 *
	 * @param attrIndex 数据在属性的index
	 * @param value     数据
	 */
	private float getValueRadius(int attrIndex, double value) {
		List<Double> region = mRegion.get(attrIndex);
		if (region.size() < 2) {
			return mRadius;
		}
		return getValueRadius(region, value, 1);
	}

	private float getValueRadius(List<Double> region, double value, int subIndex) {
		if (value < region.get(subIndex)) {
			return (float) (mRadius / region.size() * (subIndex - 1 + (value - region.get(subIndex - 1)) / region.get(subIndex)));
		} else if (subIndex == region.size() - 1) {
			return mRadius;
		} else {
			return getValueRadius(region, value, subIndex + 1);
		}
	}

}
