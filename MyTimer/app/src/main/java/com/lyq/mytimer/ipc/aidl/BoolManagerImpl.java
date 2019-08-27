package com.lyq.mytimer.ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public abstract class BoolManagerImpl extends Binder implements IBookManager {

	/**
	 * Construct the stub at attach it to the interface.
	 */
	public BoolManagerImpl() {
		this.attachInterface(this, DESCRITOR);
	}

	/**
	 * Cast as IBinder object into an IBookManager interface, generating a proxy if needed.
	 */
	public static IBookManager asInterface(IBinder obj) {
		if (obj == null) {
			return null;
		}
		IInterface lin = obj.queryLocalInterface(DESCRITOR);
		if (lin instanceof IBookManager) {
			return (IBookManager) lin;
		}
		return new Proxy(obj);
	}

	@Override
	public IBinder asBinder() {
		return this;
	}

	@Override
	protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
		if (reply == null) {
			return super.onTransact(code, data, reply, flags);
		}
		switch (code) {
			case INTERFACE_TRANSACTION:
				reply.writeString(DESCRITOR);
				return true;
			case TRANSCATION_getBookList:
				data.enforceInterface(DESCRITOR);
				List<BookInfo> result = this.getBookList();
				reply.writeNoException();
				reply.writeTypedList(result);
				return true;
			case TRANSCATION_addBook:
				data.enforceInterface(DESCRITOR);
				BookInfo arg0;
				if (0 != data.readInt()) {
					arg0 = BookInfo.CREATOR.createFromParcel(data);
				} else {
					arg0 = null;
				}
				this.addBook(arg0);
				reply.writeNoException();
				return true;
		}
		return super.onTransact(code, data, reply, flags);
	}

	private static class Proxy implements IBookManager {

		private IBinder mRemote;

		public Proxy(IBinder mRemote) {
			this.mRemote = mRemote;
		}

		@Override
		public List<BookInfo> getBookList() throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			List<BookInfo> result;
			try {
				data.writeInterfaceToken(DESCRITOR);
				mRemote.transact(TRANSCATION_getBookList, data, reply, 0);
				reply.readException();
				result = reply.createTypedArrayList(BookInfo.CREATOR);
			} finally {
				reply.recycle();
				data.recycle();
			}
			return result;
		}

		@Override
		public void addBook(BookInfo info) throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			try {
				data.writeInterfaceToken(DESCRITOR);
				if (info != null) {
					data.writeInt(1);
					info.writeToParcel(data, 0);
				} else {
					data.writeInt(0);
				}
				mRemote.transact(TRANSCATION_addBook, data, reply, 0);
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
