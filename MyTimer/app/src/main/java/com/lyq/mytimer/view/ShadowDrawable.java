package com.lyq.mytimer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ShadowDrawable extends Drawable {

	private Paint mPaint;
	private boolean isInited;
	private int width, height;

	private int pTop, pLeft, pRight, pBottom;

	private int radius;
	private int x, y;

	/** 0矩形，1圆形 */
	private int type;
	private Path path;
	private int cornerLeftTop, cornerRightBootom, cornerRightTop, cornerLeftBottom;

	public ShadowDrawable() {
		this(null);
	}

	public ShadowDrawable(View target) {
		this(target, 0);
	}

	public ShadowDrawable(final View target, int type) {
		initPaint();
		this.type = type;
		if (target != null) {
			target.post(new Runnable() {
				@Override
				public void run() {
					if (radius == 0) {
						pLeft = target.getPaddingLeft();
						pTop = target.getPaddingTop();
						pRight = target.getPaddingRight();
						pBottom = target.getPaddingBottom();
						radius = Math.min(pLeft, pTop);
						radius = Math.min(radius, pRight);
						radius = Math.min(radius, pBottom);
					}
				}
			});
			target.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}

	public void setCorner(int leftTop, int leftBottom, int rightTop, int rightBottom) {
		cornerLeftTop = leftTop;
		cornerLeftBottom = leftBottom;
		cornerRightTop = rightTop;
		cornerRightBootom = rightBottom;
	}

	public void setCorner(int corner) {
		cornerLeftTop = corner;
		cornerLeftBottom = corner;
		cornerRightTop = corner;
		cornerRightBootom = corner;
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#FFFFFF"));
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(100);
		mPaint.setTextAlign(Paint.Align.LEFT);

		path = new Path();
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
		Rect rect = getBounds();
		width = rect.width();
		height = rect.height();
		mPaint.setShadowLayer(radius, x, y, Color.BLACK);

		path.reset();
		if (type == 0) {
			path.moveTo(pLeft + cornerLeftTop, pTop);
			path.lineTo(width - pRight - cornerRightTop, pTop);
			path.arcTo(new RectF(
							width - pRight - cornerRightTop * 2,
							pTop,
							width - pRight,
							pTop + cornerRightTop * 2
					),
					- 90, 90
			);

			path.lineTo(width - pRight, height - pBottom - cornerRightBootom);
			path.arcTo(new RectF(
							width - pRight - cornerRightBootom * 2,
							height - pBottom - cornerRightBootom * 2,
							width - pRight,
							height - pBottom
					),
					0, 90
			);

			path.lineTo(pLeft + cornerLeftBottom, height - pBottom);
			path.arcTo(new RectF(
							pLeft,
							height - pBottom - cornerLeftBottom * 2,
							pLeft + cornerLeftBottom * 2,
							height - pBottom
					),
					90, 90);

			path.lineTo(pLeft, pTop + cornerLeftTop);
			path.arcTo(new RectF(
					pLeft,
					pTop,
					pLeft + cornerLeftTop * 2,
					pTop + cornerLeftTop * 2
			), 180, 90);
			path.close();
			canvas.drawPath(path, mPaint);
		} else {
			int cenX = (width - pLeft - pRight) / 2;
			int cenY = (height - pTop - pBottom) / 2;
			int circleRadius = Math.min(width - pLeft - pRight, height - pTop - pBottom) / 2;
			canvas.drawCircle(cenX, cenY, circleRadius, mPaint);
		}
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
