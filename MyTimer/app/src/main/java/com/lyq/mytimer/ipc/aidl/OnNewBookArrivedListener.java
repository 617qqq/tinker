package com.lyq.mytimer.ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;

public abstract class OnNewBookArrivedListener extends Binder implements IOnNewBookArrivedListener {

	private static final String TAG = "OnNewBookArrivedListener";

	public OnNewBookArrivedListener() {
		this.attachInterface(this, DESCRIPTOR);
	}

	public static IOnNewBookArrivedListener asInterface(IBinder obj) {
		if (obj == null) {
			return null;
		}
		IInterface lin = obj.queryLocalInterface(DESCRIPTOR);
		if (lin instanceof IBookManager) {
			return (IOnNewBookArrivedListener) lin;
		}
		return new Proxy(obj);
	}

	@Override
	public IBinder asBinder() {
		return this;
	}

	@Override
	protected boolean onTransact(int code, @NonNull Parcel data, Parcel reply, int flags) throws RemoteException {
		if (reply == null) {
			return super.onTransact(code, data, reply, flags);
		}
		switch (code) {
			case INTERFACE_TRANSACTION:
				reply.writeString(DESCRIPTOR);
				return true;
			case TRANSCATION_newBookArrived:
				data.enforceInterface(DESCRIPTOR);
				BookInfo arg0;
				if (0 != data.readInt()) {
					arg0 = BookInfo.CREATOR.createFromParcel(data);
				} else {
					arg0 = null;
				}
				this.newBookArrived(arg0);
				reply.writeNoException();
				return true;
		}
		return super.onTransact(code, data, reply, flags);
	}

	private static class Proxy implements IOnNewBookArrivedListener {

		private IBinder mRemote;

		public Proxy(IBinder obj) {
			this.mRemote = obj;
		}

		@Override
		public void newBookArrived(BookInfo book) throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				if (book != null) {
					data.writeInt(1);
					book.writeToParcel(data, 0);
				} else {
					data.writeInt(0);
				}
				mRemote.transact(TRANSCATION_newBookArrived, data, reply, 0);
			} finally {
				reply.recycle();
				data.recycle();
			}
		}

		@Override
		public IBinder asBinder() {
			return mRemote;
		}
	}
}
