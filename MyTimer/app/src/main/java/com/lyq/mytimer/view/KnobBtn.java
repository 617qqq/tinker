package com.lyq.mytimer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import io.reactivex.internal.operators.maybe.MaybeHide;

/**
 * @ProjectName: CC
 * @Package: com.aylson.new_wardrobe
 * @ClassName: KnobBtn
 * @Description:
 * @Author: TaxiDriverSantos
 * @CreateDate: 2019/11/9   14:37
 * @UpdateUser: TaxiDriverSantos
 * @UpdateDate: 2019/11/9   14:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public class KnobBtn extends View {

	private int width;
	private int height;
	private Paint paint;
	private int radius;
	private RectF rectF;
	private VelocityTracker velocityTracker;
	private int maximumFlingVelocity;
	private int scaledTouchSlop;

	private float lastAngle;
	private float targetAngle = 1;
	private float deltaAngle;
	private int cRadius;
	private Paint textPaint;


	public KnobBtn(Context context) {
		this(context, null);
	}

	public KnobBtn(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public KnobBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint();
		ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
		this.maximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
		scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
		Log.d("KnobBtn", "maximumFlingVelocity =" + this.maximumFlingVelocity);
	}

	private void initPaint() {
		paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		textPaint = new Paint();
		textPaint.setColor(Color.CYAN);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(34);
		textPaint.setTextAlign(Paint.Align.CENTER);

		textPaint.setStyle(Paint.Style.FILL);
	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();


		int min = Math.min(Math.abs(width), Math.abs(height));
		radius = min / 2;
		cRadius = radius * 12 / 16;
		rectF = new RectF(- cRadius, - cRadius, cRadius, cRadius);
		paint.setStrokeWidth(5);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				float downX = event.getX();
				float downY = event.getY();
				lastAngle = calcAngle2(downX, downY);
				break;
			case MotionEvent.ACTION_MOVE:


				float currentX = event.getX();
				float currentY = event.getY();
				float currentAngle = calcAngle2(currentX, currentY);


				deltaAngle = currentAngle - lastAngle;
				Log.d("KnobBtn", "  deltaAngle= " + deltaAngle);
				if (
						(lastAngle >= 315 || lastAngle <= 45)) {
					deltaAngle = 0;
				}
				targetAngle = targetAngle + deltaAngle;
				targetAngle = Math.max(0, Math.min(270, targetAngle));

				Log.d("KnobBtn", "    targetAngle  =  " + targetAngle
						+ "     currentAngle =  " + currentAngle
						+ "     lastAngle    =  " + lastAngle
						+ "     deltaAngle   =  " + deltaAngle);


				invalidate();
				lastAngle = currentAngle;


				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return true;
	}

	private float calcAngle2(float targetX, float targetY) {

		float x = (width / 2) - targetX;
		float y = targetY - height / 2;
		double radian;

		Log.d("KnobBtn", "calcAngle  x =  " + x + "    y =" + y);

		if (y != 0) {
			float tan = Math.abs(x / y);
			if (y > 0) {
				if (x >= 0) {
					radian = Math.atan(tan);
				} else {
					radian = 2 * Math.PI - Math.atan(tan);
				}
			} else {
				if (x >= 0) {
					radian = Math.PI - Math.atan(tan);
				} else {
					radian = Math.PI + Math.atan(tan);
				}
			}
		} else {
			if (x > 0) {
				radian = Math.PI / 2;
			} else {
				radian = 1.5 * Math.PI;
			}
		}
		float temp = (float) ((radian * 180) / Math.PI);


		return temp;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (radius <= 0) {
			return;
		}

		drawBtn(canvas);
		drawArc(canvas);
		drawTag(canvas);

		drawText(canvas);
	}

	private void drawBtn(Canvas canvas) {
//        canvas.drawBitmap();
	}

	private void drawText(Canvas canvas) {
		canvas.save();
		canvas.translate(width / 2, height / 2);

		int r = radius * 14 / 16;

		float x = - (float) (r * Math.sin(45 * Math.PI / 180));
		float y = (float) (r * Math.cos(45 * Math.PI / 180));


//        float textWidth = textPaint.measureText(targetAngle + "");
//        float ascent = textPaint.ascent();
//        float descent = textPaint.descent();
//        float textHeight = descent - ascent;
//        LogUtils.e("ascent = " + ascent + "     descent =" + descent + "     textHeight=" + textHeight);

		canvas.rotate(targetAngle);
		canvas.rotate(- 135, x, y);

		canvas.drawText(Math.floor(targetAngle) + "°", x, y, textPaint);
		canvas.restore();
	}

	private void drawTag(Canvas canvas) {
		canvas.save();
		canvas.translate(width / 2, height / 2);
		int r1 = cRadius * 12 / 16;
		int r2 = cRadius * 14 / 16;

		float x1 = (float) (- r1 * Math.sin(45 * Math.PI / 180));
		float y1 = (float) (r1 * Math.cos(45 * Math.PI / 180));

		float x2 = (float) (- r2 * Math.sin(45 * Math.PI / 180));
		float y2 = (float) (r2 * Math.cos(45 * Math.PI / 180));


		canvas.rotate(targetAngle);
		canvas.drawLine(x1, y1, x2, y2, paint);
		canvas.restore();
	}

	private void drawArc(Canvas canvas) {

		if (targetAngle > 270) {
			targetAngle = 270;
		} else if (targetAngle < 1) {
			targetAngle = 1;
		}

		canvas.save();
		canvas.translate(width / 2, height / 2);
		canvas.drawArc(rectF, 135, targetAngle, false, paint);
		canvas.restore();
	}


}
