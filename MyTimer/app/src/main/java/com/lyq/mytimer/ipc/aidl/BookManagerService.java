package com.lyq.mytimer.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {

	private static final String TAG = "BMS";

	private CopyOnWriteArrayList<BookInfo> mBookList = new CopyOnWriteArrayList<BookInfo>();

	private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

	private Binder mBinder = new BoolManagerImpl() {
		@Override
		public List<BookInfo> getBookList() throws RemoteException {
			Log.i(TAG, "getBookList");
			return mBookList;
		}

		@Override
		public void addBook(BookInfo info) throws RemoteException {
			mBookList.add(info);
		}

		@Override
		public void registerNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
			listeners.register(listener);
		}

		@Override
		public void unregisterNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
			listeners.unregister(listener);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mBookList.add(new BookInfo("Android"));
		mBookList.add(new BookInfo("IOS"));
		new Thread(new ServiceWorker()).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private class ServiceWorker implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int bookId = mBookList.size() + 1;
				BookInfo info = new BookInfo("new book#" + bookId);
				try {
					onNewBookArrived(info);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void onNewBookArrived(BookInfo info) throws RemoteException {
		mBookList.add(info);
		int listenersSize = listeners.beginBroadcast();
		for (int i = 0; i < listenersSize; i++) {
			IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
			if (listener != null) {
				listener.newBookArrived(info);
			}
		}
		listeners.finishBroadcast();
	}
}
