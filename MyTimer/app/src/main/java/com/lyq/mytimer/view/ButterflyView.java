package com.lyq.mytimer.view;

import android.animation.TimeInterpolator;
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

	private Path pathTrack;
	private Path path;
	private Path pathTexture;
	private PathMeasure pathMeasure;
	private float trackLength;
	private float distance;
	private Paint paint;
	private float[] pos = new float[2];
	private float[] tan = new float[2];

	private String[] color = {"#f9d8f9", "#ae63ef", "#00FFFFFF", "#FFFFFF"};
	/** 蝴蝶，纹理 */
	private Shader butterflyShader, textureShader;
	private Paint paintButterfly, paintTexture;
	/** 蝴蝶翅膀的16个点，4 * （三角三个点+内心） */
	private float[] point = new float[32];

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
		pathTexture = new Path();

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
		paintButterfly.setStyle(Paint.Style.FILL_AND_STROKE);

		//画纹理时，中心点坐标为0，0
		textureShader = new RadialGradient(0, 0, 100
				, Color.parseColor(color[3])
				, Color.parseColor(color[2])
				, Shader.TileMode.CLAMP);
		paintTexture = new Paint();
		paintTexture.setAntiAlias(true);
		paintTexture.setShader(textureShader);
//		paintTexture.setColor(Color.parseColor("#FFFFFF"));
		paintTexture.setStrokeWidth(1);
		paintTexture.setStyle(Paint.Style.STROKE);


		initAnimator();
	}

	private float degrees1, degrees2;

	private void initAnimator() {
		ValueAnimator animator = ValueAnimator.ofFloat(0f, 40f);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degrees1 = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		TimeInterpolator interpolator = new TimeInterpolator() {
			/**
			 * 0.0 - 0.3   -> 0-1
			 * 0.3 - 0.45   -> 1-0.2
			 * 0.45 - 0.6  -> 0.2-1
			 * 0.6 - 0.9    -> 1-0
			 * 0.9 - 1  -> 0
			 */
			@Override
			public float getInterpolation(float input) {
				if (input < 0.3f) {
					return input / 0.3f;
				} else if (input < 0.45f) {
					return 1 - 0.8f * (input - 0.3f) / 0.15f;
				} else if (input < 0.6f) {
					return 0.2f + 0.8f * (input - 0.45f) / 0.15f;
				} else if (input < 0.9f) {
					return 1f - (input - 0.6f) / 0.3f;
				} else {
					return 0;
				}
			}
		};
		animator.setInterpolator(interpolator);
		animator.setDuration(2000);
		animator.setRepeatCount(- 1);
		animator.setRepeatMode(ValueAnimator.REVERSE);
		animator.start();

		ValueAnimator animator2 = ValueAnimator.ofFloat(150f, 110f);
		animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degrees2 = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		animator2.setDuration(2000);
		animator2.setRepeatCount(- 1);
		animator2.setInterpolator(interpolator);
		animator2.setRepeatMode(ValueAnimator.REVERSE);
		animator2.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//drawTrack(canvas);
		//drawTest(canvas);

		//drawFrame(canvas);
		drawButterfly(canvas);
	}

	private void drawFrame(Canvas canvas) {
		canvas.drawRect(0, 0, 400, 350, paint);
		canvas.drawLine(0, 200, 400, 200, paint);
		canvas.drawLine(200, 0, 200, 350, paint);
		canvas.drawLine(0, 0, 350, 350, paint);
		canvas.drawLine(50, 350, 400, 0, paint);
	}

	private void drawButterfly(Canvas canvas) {
		drawButterflyHalf(canvas, true);
		drawButterflyHalf(canvas, false);
	}

	Camera camera = new Camera();
	Matrix matrix = new Matrix();

	private void drawSystem(Canvas canvas) {
		canvas.drawLine(0, height / 2, width, height / 2, paint);
		canvas.drawLine(width / 2, 0, width / 2, height, paint);
	}

	private void drawButterflyHalf(Canvas canvas, boolean isLeft) {

		canvas.save();
		canvas.translate(width / 2, height / 2);
		canvas.scale(0.5f, 0.5f);
		canvas.translate(-200,-200);
		canvas.rotate(30);

		camera.save();
		matrix.reset();
		canvas.save();

		camera.rotate(20, isLeft ? degrees1 : degrees2, 0);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(- 200, - 200);
		matrix.postTranslate(200, 200);
		canvas.concat(matrix);

		canvas.drawPath(path, paintButterfly);
		canvas.restore();

		canvas.restore();
	}

	private void initButterflyPath() {
		int cx = 200;
		float[] moveX = {cx - 5, cx - 10, cx - 10, cx - 5,};
		float[] moveY = {cx - 10, cx - 5, cx + 5, cx + 10,};
		float[] lineC = {100, 190, 125, 100,};
		float[] lineB = {200, 120, 150, 130,};
		float[] agree1 = {110, 138, 190, 225,};
		float[] agree2 = {135, 165, 220, 250,};

		for (int i = 0; i < moveX.length; i++) {
			int offset = 8 * i;
			point[offset] = moveX[i];
			point[offset + 1] = moveY[i];

			point[offset + 2] = cx + getCos(lineC[i], agree1[i], i);
			point[offset + 3] = cx - getSin(lineC[i], agree1[i], i);

			point[offset + 4] = cx + getCos(lineB[i], agree2[i], i);
			point[offset + 5] = cx - getSin(lineB[i], agree2[i], i);

			float lineA = (float) Math.sqrt(
					Math.pow(point[offset + 2] - point[offset + 4], 2)
							+ Math.pow(point[offset + 3] - point[offset + 5], 2));
			point[offset + 6] = getInnerPoint(offset, lineA, lineC[i], lineB[i]);
			point[offset + 7] = getInnerPoint(offset + 1, lineA, lineC[i], lineB[i]);
		}

		path.reset();
		pathTexture.reset();

		for (int i = 0; i < point.length; ) {
			path.moveTo(point[i], point[i + 1]);
			path.lineTo(point[i + 2], point[i + 3]);
			path.lineTo(point[i + 4], point[i + 5]);
			path.close();

			pathTexture.moveTo(point[i + 6], point[i + 7]);
			pathTexture.lineTo(point[i], point[i + 1]);
			pathTexture.moveTo(point[i + 6], point[i + 7]);
			pathTexture.lineTo(point[i + 2], point[i + 3]);
			pathTexture.moveTo(point[i + 6], point[i + 7]);
			pathTexture.lineTo(point[i + 4], point[i + 5]);

			i += 8;
		}
	}

	private float getInnerPoint(int i, float lineA, float lineC, float lineB) {
		return (point[i] * lineA + point[i + 2] * lineB + point[i + 4] * lineC)
				/ (lineA + lineB + lineC);
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
