package com.lyq.mytimer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DebutDrawable extends Drawable {

	private Paint mPaint;
	private int radius;
	private float x, y;
	private RectF rectF;
	private long startTime;

	public DebutDrawable() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		startTime = System.currentTimeMillis();
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (radius == 0) {
			Rect rect = getBounds();
			x = rect.width();
			y = rect.height();
			radius = (int) Math.sqrt(x * x + y * y);
			float offsetX = radius - x / 2;
			float offsetY = radius - y / 2;
			rectF = new RectF(- offsetX, - offsetY, x + offsetX, y + offsetY);
		}
		float angle = 360f * (float) (System.currentTimeMillis() - startTime) / 8000;
		canvas.translate(x / 2, y / 2);
		canvas.rotate(angle);
		canvas.translate(- x / 2, - y / 2);

		mPaint.setColor(Color.parseColor("#AAFFFFFF"));
		for (int i = 0; i < 36; i++) {
			canvas.save();
			canvas.translate(x / 2, y / 2);
			canvas.rotate(10 * i);
			canvas.translate(- x / 2, - y / 2);
			canvas.drawArc(rectF, 90, 5, true, mPaint);
			canvas.restore();
		}

		mPaint.setColor(Color.parseColor("#00FFFFFF"));
		for (int i = 0; i < 36; i++) {
			canvas.save();
			canvas.translate(x / 2, y / 2);
			canvas.rotate(10 * i + 5);
			canvas.translate(- x / 2, - y / 2);
			canvas.drawArc(rectF, 90, 5, true, mPaint);
			canvas.restore();
		}

		invalidateSelf();
	}

	@Override
	public void setAlpha(int alpha) {
		if (mPaint != null) {
			mPaint.setAlpha(alpha);
			invalidateSelf();
		}
	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {
		if (mPaint != null) {
			mPaint.setColorFilter(colorFilter);
			invalidateSelf();
		}
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}
