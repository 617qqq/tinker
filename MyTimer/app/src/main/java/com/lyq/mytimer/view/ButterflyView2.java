package com.lyq.mytimer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.lyq.mytimer.R;

public class ButterflyView2 extends View {

	private Bitmap bitmap;
	private float degrees1, degrees2;

	public ButterflyView2(Context context) {
		super(context);
		init();
	}

	public ButterflyView2(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ButterflyView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
		bWidth = bitmap.getWidth();
		bHeight = bitmap.getHeight();

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);

		ValueAnimator animator = ValueAnimator.ofFloat(-0f, -30f);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degrees1 = (float) animation.getAnimatedValue();
				Log.e("617233", "onAnimationUpdate: " + degrees1 );
				postInvalidate();
			}
		});
		animator.setDuration(1500);
		animator.setRepeatCount(-1);
		animator.setInterpolator(new ButterflyInterpolator());
		animator.setRepeatMode(ValueAnimator.REVERSE);
		animator.start();

		ValueAnimator animator2 = ValueAnimator.ofFloat(-150f, -120f);
		animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degrees2 = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		animator2.setDuration(1500);
		animator2.setRepeatCount(- 1);
		animator2.setInterpolator(new ButterflyInterpolator());
		animator2.setRepeatMode(ValueAnimator.REVERSE);
		animator2.start();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}

	private Camera camera = new Camera();
	private Matrix matrix = new Matrix();
	private int width, height;
	private int bWidth, bHeight;
	private Paint paint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.rotate(35, width/2, height/2);

		canvas.save();
		camera.save();
		matrix.reset();

		camera.rotateY(degrees1);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-width/2, -height/2);
		matrix.postTranslate(width/2, height/2);
		canvas.concat(matrix);
		canvas.drawBitmap(bitmap, width/2 - bWidth, height/2 - bHeight/2, paint);
		canvas.restore();

		canvas.save();
		camera.save();
		matrix.reset();

		camera.rotateY(degrees2);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-width/2, -height/2);
		matrix.postTranslate(width/2, height/2);
		canvas.concat(matrix);
		canvas.drawBitmap(bitmap, width/2 - bWidth, height/2 - bHeight/2, paint);
		canvas.restore();
	}
}
