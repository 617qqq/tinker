package com.lyq.mytimer.ipc.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * 模块标志<br/>
 * CountDownLatch<br/>
 * 单例，获取Context<br/>
 * 绑定BinderPoolService<br/>
 * BinderPoolService连接监听：获取IBinderPool，且设置死亡代理，CountDownLatch.countDown()<br/>
 * 死亡代理:断开连接时重新链接BinderPoolService<br/>
 * 实现IBinderPool.Stub
 */
public class BinderPool {

	//模块标志
	private static final int BINDER_COMPUTE = 0;

	private Context context;
	private IBinderPool mBinderPool;
	private static volatile BinderPool sInstance;

	//CountDownLatch
	private CountDownLatch mConnect;

	//单例，获取Context
	private BinderPool(Context context) {
		this.context = context.getApplicationContext();
		connectBinderPoolService();
	}

	public static BinderPool getInstance(Context context) {
		if (sInstance == null) {
			synchronized (BinderPool.class) {
				if (sInstance == null) {
					sInstance = new BinderPool(context);
				}
			}
		}
		return sInstance;
	}

	//绑定BinderPoolService
	private void connectBinderPoolService() {
		//创建CountDownLatch
		mConnect = new CountDownLatch(1);
		//绑定BinderPoolService
		Intent intent = new Intent(context, BinderPoolService.class);
		context.bindService(intent, mBinderPoolServiceConnection, Context.BIND_AUTO_CREATE);
		//CountDownLatch.await();
		try {
			mConnect.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//BinderPoolService连接监听：获取IBinderPool，且设置死亡代理，CountDownLatch.countDown()
	private ServiceConnection mBinderPoolServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinderPool = IBinderPool.Stub.asInterface(service);
			try {
				mBinderPool.asBinder().linkToDeath(mRecipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mConnect.countDown();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	//死亡代理:断开连接时重新链接BinderPoolService
	private IBinder.DeathRecipient mRecipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			mBinderPool.asBinder().unlinkToDeath(mRecipient, 0);
			mBinderPool = null;
			connectBinderPoolService();
		}
	};

	//实现IBinderPool.Stub
	public static class BinderPoolImpl extends IBinderPool.Stub {

		@Override
		public IBinder queryBinder(int code) throws RemoteException {
			IBinder binder = null;
			switch (code) {
				case BINDER_COMPUTE:
					binder = new ComputeImpl();
					break;
			}
			return binder;
		}
	}
}
