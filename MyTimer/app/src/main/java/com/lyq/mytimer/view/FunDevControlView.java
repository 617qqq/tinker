package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyq.mytimer.R;

public class FunDevControlView extends View {

	//绘制相关参数

	//中心坐标
	private int x, y;
	//扇面上的点在坐标系的偏差，out是外轮廓，in是内轮廓
	private float sinOutRadius, sinInRadius, cosOutRadius, cosInRadius;
	//Bitmap半径，内轮廓半径，外轮廓半径
	private float radius, inRadius, outRadius;
	private Path[] path = new Path[4];
	//点击范围
	private Region[] regions = new Region[4];
	private Paint paint;
	private Bitmap bitmap;
	private Matrix matrix;


	//数据相关
	private OnIndexChangeListener listener;
	private int touchIndex = - 1;


	public FunDevControlView(Context context) {
		super(context);
		init();
	}

	public FunDevControlView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FunDevControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);

		for (int i = 0; i < 4; i++) {
			path[i] = new Path();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int width = getWidth();
		int height = getHeight();

		int pStart = getPaddingLeft();
		int pTop = getPaddingTop();
		int pBottom = getPaddingBottom();
		int pEnd = getPaddingRight();

		//当padding大于实际宽高时失效
		if (pStart + pEnd > width) {
			pStart = 0;
			pEnd = 0;
		}
		if (pTop + pBottom > height) {
			pTop = 0;
			pBottom = 0;
		}

		initParams(width, height, pStart, pTop, pBottom, pEnd);
	}

	private void initParams(int width, int height, int pStart, int pTop, int pBottom, int pEnd) {
		radius = Math.min((width - pStart - pEnd) / 2, (height - pTop - pBottom) / 2);
		outRadius = radius * 23 / 25;
		inRadius = (int) (radius / 2f);

		x = (width - pStart - pEnd) / 2 + pStart;
		y = (height - pTop - pBottom) / 2 + pTop;

		double angle = 41;
		sinOutRadius = (float) (Math.sin(angle * Math.PI / 180) * outRadius);
		cosOutRadius = (float) (Math.cos(angle * Math.PI / 180) * outRadius);
		sinInRadius = (float) (Math.sin(angle * Math.PI / 180) * inRadius);
		cosInRadius = (float) (Math.cos(angle * Math.PI / 180) * inRadius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (bitmap == null) {

			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_dev_control);
			matrix = new Matrix();
			matrix.postScale(radius * 2f / bitmap.getWidth(), radius * 2f / bitmap.getHeight());
			matrix.postTranslate(x - radius, y - radius);

			initPath();
			initRegion();
		}
		//绘制点击区域
		if (touchIndex != - 1) {
			canvas.drawPath(path[touchIndex], paint);
		}
		//drawTest(canvas);
		canvas.drawBitmap(bitmap, matrix, paint);
	}

	/**
	 * 测试
	 */
	private void drawTest(Canvas canvas) {
		for (int i = 0; i < 4; i++) {
			canvas.drawPath(path[i], paint);
		}
	}

	private void initPath() {
		RectF rectF = new RectF(x - outRadius, y - outRadius, x + outRadius, y + outRadius);
		RectF rectFIn = new RectF(x - inRadius, y - inRadius, x + inRadius, y + inRadius);

		int sweepAngle = 82;
		float angleOffset = 4f;

		path[0].moveTo(x - sinOutRadius, y - cosOutRadius);
		path[0].arcTo(rectF, - 135 + angleOffset, sweepAngle);
		path[0].lineTo(x + sinInRadius, y - cosInRadius);
		path[0].arcTo(rectFIn, - 45 - angleOffset, - sweepAngle);
		path[0].close();

		path[1].moveTo(x + cosOutRadius, y - sinOutRadius);
		path[1].arcTo(rectF, - 45 + angleOffset, sweepAngle);
		path[1].lineTo(x + cosInRadius, y + sinInRadius);
		path[1].arcTo(rectFIn, 45 - angleOffset, - sweepAngle);
		path[1].close();

		path[2].moveTo(x + sinOutRadius, y + cosOutRadius);
		path[2].arcTo(rectF, 45 + angleOffset, sweepAngle);
		path[2].lineTo(x - sinInRadius, y + cosInRadius);
		path[2].arcTo(rectFIn, 135 - angleOffset, - sweepAngle);
		path[2].close();

		path[3].moveTo(x - cosOutRadius, y + sinOutRadius);
		path[3].arcTo(rectF, 135 + angleOffset, sweepAngle);
		path[3].lineTo(x - cosInRadius, y - sinInRadius);
		path[3].arcTo(rectFIn, 225 - angleOffset, - sweepAngle);
		path[3].close();
	}

	private void initRegion() {
		Region region = new Region(
				(int) (x - radius),
				(int) (y - radius),
				(int) (x + radius),
				(int) (y + radius));
		for (int i = 0; i < path.length; i++) {
			regions[i] = new Region();
			regions[i].setPath(path[i], region);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				updateCurrentIndex(event);
				postInvalidate();
				return true;
			case MotionEvent.ACTION_MOVE:
				updateCurrentIndex(event);
				postInvalidate();
				return super.onTouchEvent(event);
			case MotionEvent.ACTION_UP:
				touchIndex = - 1;
				postInvalidate();
				return super.onTouchEvent(event);
			default:
				return super.onTouchEvent(event);
		}
	}

	private void updateCurrentIndex(MotionEvent event) {
		for (int i = 0; i < regions.length; i++) {
			if (regions[i].contains((int) event.getX(), (int) event.getY())) {
				touchIndex = i;
				break;
			} else if (i == regions.length - 1) {
				touchIndex = - 1;
			}
		}
		if (listener != null) {
			listener.onChange(touchIndex);
		}
	}

	public interface OnIndexChangeListener {

		void onChange(@IntRange (from = - 1, to = 3) int index);
	}

	public void setListener(OnIndexChangeListener listener) {
		this.listener = listener;
	}
}
