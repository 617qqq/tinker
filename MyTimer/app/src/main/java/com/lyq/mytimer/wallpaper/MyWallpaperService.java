package com.lyq.mytimer.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class MyWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new MyEngine();
	}

	class MyEngine extends WallpaperService.Engine {

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(Color.RED);
			holder.unlockCanvasAndPost(canvas);
		}
	}
}
