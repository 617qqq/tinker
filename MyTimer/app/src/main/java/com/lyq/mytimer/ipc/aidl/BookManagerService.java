package com.lyq.mytimer.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {

	private static final String TAG = "BMS";

	private CopyOnWriteArrayList<BookInfo> mBookList = new CopyOnWriteArrayList<BookInfo>();

	private Binder mBinder = new BoolManagerImpl() {
		@Override
		public List<BookInfo> getBookList() throws RemoteException {
			return mBookList;
		}

		@Override
		public void addBook(BookInfo info) throws RemoteException {
			mBookList.add(info);
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
