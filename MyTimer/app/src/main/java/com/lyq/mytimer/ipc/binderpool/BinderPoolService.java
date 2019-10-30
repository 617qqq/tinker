package com.lyq.mytimer.ipc.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class BinderPoolService extends Service {

	private BinderPool.BinderPoolImpl binderPool = new BinderPool.BinderPoolImpl();

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binderPool;
	}
}
