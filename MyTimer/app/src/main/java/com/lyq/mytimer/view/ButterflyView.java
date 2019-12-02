package com.lyq.mytimer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ButterflyView extends View {

	private int width, height;

	private Path pathTrack, path;
	private PathMeasure pathMeasure;
	private float trackLength;
	private float distance;
	private Paint paint;
	private float[] pos = new float[2];
	private float[] tan = new float[2];

	private String[] color = {"#f9d8f9", "#ae63ef", "#22FFFFFF", "#AAFFFFFF"};
	/** 蝴蝶，纹理 */
	private Shader butterflyShader, textureShader;
	private Paint paintButterfly, paintTexture;

	public ButterflyView(Context context) {
		super(context);
		init();
	}

	public ButterflyView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ButterflyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		pathTrack = new Path();
		pathMeasure = new PathMeasure();

		path = new Path();

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();
		height = getMeasuredHeight();

		initData();
	}

	private void initData() {
		initButterflyPath();


		pathTrack.addCircle(width / 2, height / 2, height / 4, Path.Direction.CW);

		pathMeasure.setPath(pathTrack, false);
		trackLength = pathMeasure.getLength();

		butterflyShader = new RadialGradient(200, 200, 150
				, new int[]{
				Color.parseColor("#f5d4f8")
				, Color.parseColor("#ddaff6")
				, Color.parseColor("#c78af3")
				, Color.parseColor("#b36af0")
				, Color.parseColor("#ab5def")}
				, new float[]{0, 0.25f, 0.5f, 0.75f, 1f}
				, Shader.TileMode.CLAMP
		);

		paintButterfly = new Paint();
		paintButterfly.setAntiAlias(true);
		paintButterfly.setShader(butterflyShader);
		paintButterfly.setStyle(Paint.Style.FILL);

		textureShader = new RadialGradient(200, 200, 200
				, Color.parseColor(color[3])
				, Color.parseColor(color[2])
				, Shader.TileMode.CLAMP);
		paintTexture = new Paint();
		paintTexture.setAntiAlias(true);
		paintTexture.setShader(textureShader);
		paintTexture.setStrokeWidth(1);
		paintTexture.setStyle(Paint.Style.STROKE);

		ValueAnimator animator = ValueAnimator.ofFloat(50f, 75f);
		animator.setDuration(2000);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degreesOffset = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		animator.setRepeatMode(ValueAnimator.REVERSE);
		animator.setRepeatCount(- 1);
		animator.start();

		pointPaint.setStyle(Paint.Style.FILL);
		pointPaint.setColor(Color.RED);
		pointPaint.setStrokeWidth(20);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//drawTrack(canvas);
		//drawTest(canvas);

		//drawFrame(canvas);
		//canvas.drawColor(Color.BLACK);
		drawButterfly(canvas);
//		canvas.drawPoint(width / 2, height / 2, paint);
//		canvas.drawPoint(0,0, paint);
//
//		canvas.save();
//		canvas.scale(0.5f, 0.5f, width / 2, height / 2);
//		canvas.translate(100, 100);
//
//		canvas.drawPoint(width / 2 - 100, height / 2 - 100, pointPaint);
//		canvas.drawPoint(-width/2 - 100, -height/2 - 100, pointPaint);
//		canvas.rotate(45);
//		canvas.drawRect(width/2 - 200 , height / 2 - 200, width/2, height/2, paint);
//		canvas.restore();
	}

	private void drawFrame(Canvas canvas) {
		canvas.drawRect(0, 0, 400, 350, paint);
		canvas.drawLine(0, 200, 400, 200, paint);
		canvas.drawLine(200, 0, 200, 350, paint);
		canvas.drawLine(0, 0, 350, 350, paint);
		canvas.drawLine(50, 350, 400, 0, paint);
	}

	private Matrix matrix = new Matrix();
	private Camera camera = new Camera();
	private float degreesOffset = 1f;

	private void drawButterfly(Canvas canvas) {
		drawButterflyHalf(canvas, true);
		drawButterflyHalf(canvas, false);
	}

	private Paint pointPaint = new Paint();

	private void drawButterflyHalf(Canvas canvas, boolean isLeft) {
		canvas.save();
		canvas.save();
		camera.save();
		matrix.reset();

		camera.rotate(-45, getDegrees(isLeft), -20);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(- 200, - 200);
		matrix.postTranslate(200, 200);
		canvas.concat(matrix);

		path.reset();
		int start = isLeft ? 0 : 4;
		int end = isLeft ? 4 : 8;
		for (int i = start; i < end; i++) {
			path.moveTo(point[8 * i], point[8 * i + 1]);
			path.lineTo(point[8 * i + 2], point[8 * i + 3]);
			path.lineTo(point[8 * i + 4], point[8 * i + 5]);
			path.close();
		}
		canvas.drawPath(path, paintButterfly);
		for (int i = start; i < end; i++) {
			canvas.drawLine(point[8 * i + 6], point[8 * i + 7], point[8 * i], point[8 * i + 1], paintTexture);
			canvas.drawLine(point[8 * i + 6], point[8 * i + 7], point[8 * i + 2], point[8 * i + 3], paintTexture);
			canvas.drawLine(point[8 * i + 6], point[8 * i + 7], point[8 * i + 4], point[8 * i + 5], paintTexture);
		}
		canvas.restore();
		canvas.restore();
	}

	private float getDegrees(boolean isLeft) {
		return (isLeft ? 1 : - 1) * degreesOffset;
	}

	private float[] point = new float[64];

	private void initButterflyPath() {
		int cx = 200;
		float[] moveX = {cx - 5, cx - 10, cx - 10, cx - 5, cx + 5, cx + 10, cx + 10, cx + 5,};
		float[] moveY = {cx - 10, cx - 5, cx + 5, cx + 10,};
		float[] line1 = {100, 190, 125, 100,};
		float[] line2 = {200, 120, 150, 130,};
		float[] agree1 = {110, 138, 190, 225,};
		float[] agree2 = {135, 165, 220, 250,};

		for (int i = 0; i < moveX.length; i++) {
			point[8 * i] = moveX[i];
			point[8 * i + 1] = moveY[i % 4];

			point[8 * i + 2] = cx + getCos(line1[i % 4], agree1[i % 4], i);
			point[8 * i + 3] = cx - getSin(line1[i % 4], agree1[i % 4], i);

			point[8 * i + 4] = cx + getCos(line2[i % 4], agree2[i % 4], i);
			point[8 * i + 5] = cx - getSin(line2[i % 4], agree2[i % 4], i);

			float lineTemp = (line1[i % 4] + line2[1 % 4]) / 2 - (i % 4 == 1 ? 35 : 15);
			float agreeTemp = (agree1[i % 4] + agree2[i % 4]) / 2;
			point[8 * i + 6] = cx + getCos(lineTemp, agreeTemp, i);
			point[8 * i + 7] = cx - getSin(lineTemp, agreeTemp, i);
		}
	}

	private float getSin(float line01, float agree01, int i) {
		if (i > 3) {
			return (float) (line01 * Math.sin((540 - agree01) % 360 * Math.PI / 180));
		} else {
			return (float) (line01 * Math.sin(agree01 * Math.PI / 180));
		}
	}

	private float getCos(float line01, float agree01, int i) {
		if (i > 3) {
			return (float) (line01 * Math.cos((540 - agree01) % 360 * Math.PI / 180));
		} else {
			return (float) (line01 * Math.cos(agree01 * Math.PI / 180));
		}
	}

	private void drawTrack(Canvas canvas) {
		canvas.drawPath(pathTrack, paint);

		pathMeasure.getPosTan(distance, pos, tan);
		canvas.save();
		//计算方位角
		float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
		canvas.translate(pos[0], pos[1]);
		canvas.rotate(degrees);
		canvas.translate(- 25, - 25);
		canvas.drawPath(path, paint);
		canvas.restore();
	}

	private void drawTest(Canvas canvas) {
		canvas.drawRect(0, 0, width, height, paintButterfly);
	}
}
