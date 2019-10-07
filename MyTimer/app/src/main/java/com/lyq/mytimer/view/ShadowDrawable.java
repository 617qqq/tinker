package com.lyq.mytimer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ShadowDrawable extends Drawable {

	private Paint mPaint;
	private boolean isInited;
	private int width, height;

	private int radius;
	private int x, y;

	public ShadowDrawable() {
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#000000"));
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(100);
		mPaint.setTextAlign(Paint.Align.LEFT);
	}

	public void setRadius(int radius) {
		this.radius = radius;
		invalidateSelf();
	}

	public void setX(int x) {
		this.x = x;
		invalidateSelf();
	}

	public void setY(int y) {
		this.y = y;
		invalidateSelf();
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (! isInited) {
			Rect rect = getBounds();
			width = rect.width();
			height = rect.height();
			radius = 0;
			isInited = true;
		}
		mPaint.setShadowLayer(radius, x, y, Color.BLACK);
		canvas.drawText("AAAA", width / 2 - 200, height / 2 - 200, mPaint);
		canvas.drawRect(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100, mPaint);
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
