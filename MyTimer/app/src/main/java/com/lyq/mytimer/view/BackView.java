package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class BackView extends View {

	/** 宽高 */
	private int width, height, startY;
	/** 顶点位置 */
	private float x;
	/** 画笔 */
	private Paint paintBg, paintArrow;
	/** 路径 */
	private Path pathBg, pathArrow;
	/** 触发时间 */
	private OnBackListener listener;

	public BackView(Context context) {
		super(context);
		init();
	}

	public BackView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		paintBg = new Paint();
		paintBg.setColor(Params.cBg);
		paintBg.setAlpha(Params.startAlpha);
		paintBg.setAntiAlias(true);

		paintArrow = new Paint();
		paintArrow.setColor(Params.cArrow);
		paintArrow.setStyle(Paint.Style.STROKE);
		paintArrow.setStrokeWidth(Params.paintWidth);
		paintArrow.setAntiAlias(true);
		paintArrow.setStrokeCap(Paint.Cap.ROUND);

		pathBg = new Path();
		pathArrow = new Path();
	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		height = startY = measuredHeight / 3;

		initData();
	}

	private void initData() {
		pathArrow.reset();
		int centreX = Params.maxWidth / 2;
		int centreY = startY + height / 2;
		pathArrow.moveTo(Params.wArrow - centreX, centreY - Params.hArrow);
		pathArrow.lineTo(- Params.wArrow - centreX, centreY);
		pathArrow.lineTo(Params.wArrow - centreX, centreY + Params.hArrow);
	}

	private Point[] point1 = new Point[]{new Point(), new Point()};
	private Point[] point2 = new Point[]{new Point(), new Point()};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		pathBg.reset();

		int targetX = Math.min(Params.maxWidth, (int) x);
		point1[0].set(0, startY + height / 3);
		point1[1].set(0, startY + height * 2 / 3);
		point2[0].set(targetX, startY + height * 4 / 12);
		point2[1].set(targetX, startY + height * 8 / 12);

		pathBg.moveTo(0, startY);
		pathBg.cubicTo(point1[0].x, point1[0].y, point2[0].x, point2[0].y, targetX, startY + height / 2);
		pathBg.cubicTo(point2[1].x, point2[1].y, point1[1].x, point1[1].y, 0, startY + height);
		pathBg.close();
		canvas.drawPath(pathBg, paintBg);

		canvas.save();
		canvas.translate(targetX, 0);
		canvas.drawPath(pathArrow, paintArrow);
		canvas.restore();
	}

	static class Params {
		/** 有效点击区域 */
		static int availableWidth = 80;
		/** 有效触发区域 */
		static int backWidth = 50;
		/** 最宽的宽度 */
		static int maxWidth = 60;
		/** 开始的不透明度 */
		static int startAlpha = 180;
		/** 背景颜色 */
		static int cBg = Color.BLACK;
		/** 箭头颜色 */
		static int cArrow = Color.WHITE;
		/** 箭头宽度 */
		static int wArrow = 12;
		/** 箭头长度 */
		static int hArrow = 20;
		/** 箭头画笔宽度 */
		static int paintWidth = 3;
	}

	private float eventX;
	private float lastX;

	private int i = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean isDispatch = false;
		eventX = event.getX();
		Log.e("onTouchEvent", String.valueOf(eventX));
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (eventX < Params.availableWidth) {
					x = 0;
					isDispatch = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float offset = eventX - lastX;
				x += offset;
				isDispatch = true;
				break;
			case MotionEvent.ACTION_UP:
				if (listener != null && x >= Params.backWidth) {
					listener.onBack();
				}
				isDispatch = true;
				x = 0;
				break;
		}
		lastX = eventX;
		if (isDispatch) {
			invalidate();
		}
		return isDispatch;
	}

	public void setListener(OnBackListener listener) {
		this.listener = listener;
	}

	public interface OnBackListener {
		void onBack();
	}
}
