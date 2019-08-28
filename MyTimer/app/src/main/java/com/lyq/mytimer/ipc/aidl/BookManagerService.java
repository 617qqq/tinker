package com.lyq.mytimer.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {

	private static final String TAG = "BMS";

	private CopyOnWriteArrayList<BookInfo> mBookList = new CopyOnWriteArrayList<BookInfo>();

	private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();

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
			if (listeners.contains(listener)) {

			}
		}

		@Override
		public void unregisterNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mBookList.add(new BookInfo("Android"));
		mBookList.add(new BookInfo("IOS"));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
