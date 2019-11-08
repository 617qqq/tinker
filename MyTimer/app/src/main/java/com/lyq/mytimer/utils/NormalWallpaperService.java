package com.lyq.mytimer.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class NormalWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	private class MyWallpaperEngine extends Engine {
		int counter;
		Paint paint;

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);

			Observable.interval(1000, TimeUnit.MILLISECONDS)
					.subscribe(new Consumer<Long>() {
						@Override
						public void accept(Long aLong) {
							drawRunner.run();
						}
					}).isDisposed();
		}

		{
			paint = new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setTextSize(50);
		}

		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}

			private void draw() {
				Canvas canvas = getSurfaceHolder().lockCanvas();
				if (canvas != null) {
					canvas.drawColor(Color.BLACK);
					canvas.save();
					canvas.drawText(String.valueOf(counter++)
							, canvas.getWidth() / 2
							, canvas.getHeight() / 2
							, paint
					);
					getSurfaceHolder().unlockCanvasAndPost(canvas);
				}
			}
		};
	}
}
