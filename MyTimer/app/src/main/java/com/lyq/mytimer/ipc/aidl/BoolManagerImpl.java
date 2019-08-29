package com.lyq.mytimer.ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public abstract class BoolManagerImpl extends Binder implements IBookManager {

	private static final String TAG = "BoolManagerImpl";

	/**
	 * Construct the stub at attach it to the interface.
	 */
	public BoolManagerImpl() {
		this.attachInterface(this, DESCRIPTOR);
	}

	/**
	 * Cast as IBinder object into an IBookManager interface, generating a proxy if needed.
	 * 用于将服务端的Binder对象转换成客户端所需的AIDL接口类型对象。<br/>
	 * 客户端和服务端于同一进程，那么此方法返回的就是服务端的Stub本身,<br/>
	 * 不在同一进程，返回系统封装后的Stub.proxy对象。
	 */
	public static IBookManager asInterface(IBinder obj) {
		if (obj == null) {
			return null;
		}
		IInterface lin = obj.queryLocalInterface(DESCRIPTOR);
		if (lin instanceof IBookManager) {
			return (IBookManager) lin;
		}
		return new Proxy(obj);
	}

	/**
	 * @return 此方法用于返回当前Binder对象
	 */
	@Override
	public IBinder asBinder() {
		return this;
	}

	/**
	 * - 服务端通过code确定客户端请求的目标方法是什么，<br/>
	 * - 接着从data取出目标方法所需的参数，然后执行目标方法。<br/>
	 * - 执行完毕后向reply写入返回值（如果有返回值）。<br/>
	 * - 如果这个方法返回值为false，那么服务端的请求会失败，利用这个特性我们可以来做权限验证。
	 */
	@Override
	protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
		if (reply == null) {
			return super.onTransact(code, data, reply, flags);
		}
		switch (code) {
			case INTERFACE_TRANSACTION:
				reply.writeString(DESCRIPTOR);
				return true;
			case TRANSCATION_getBookList:
				data.enforceInterface(DESCRIPTOR);
				List<BookInfo> result = this.getBookList();
				reply.writeNoException();
				/** 返回List<Book> */
				reply.writeTypedList(result);
				Log.i(TAG, "onTransact");
				return true;
			case TRANSCATION_addBook:
				data.enforceInterface(DESCRIPTOR);
				BookInfo arg0;
				if (0 != data.readInt()) {
					/** 获取Book */
					arg0 = BookInfo.CREATOR.createFromParcel(data);
				} else {
					arg0 = null;
				}
				this.addBook(arg0);
				reply.writeNoException();
				return true;
			case TRANSCATION_registerNewBookArrivedListener:
				data.enforceInterface(DESCRIPTOR);
				/** 获取IOnNewBookArrivedListener */
				IOnNewBookArrivedListener registerNewBookListener = OnNewBookArrivedListener.asInterface(data.readStrongBinder());
				this.registerNewBookArrivedListener(registerNewBookListener);
				reply.writeNoException();
				return true;
			case TRANSCATION_unregisterNewBookArrivedListener:
				data.enforceInterface(DESCRIPTOR);
				IOnNewBookArrivedListener unregisterNewBookListener = OnNewBookArrivedListener.asInterface(data.readStrongBinder());
				this.unregisterNewBookArrivedListener(unregisterNewBookListener);
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

		/**
		 * - 这个方法运行在客户端，首先该方法所需要的输入型对象Parcel对象data，输出型Parcel对象reply和返回值对象List。<br/>
		 * - 然后把该方法的参数信息写入data（如果有参数），<br/>
		 * - 接着调用transact方法发起RPC（远程过程调用），同时当前线程挂起<br/>
		 * - 然后服务端的onTransact方法会被调用知道RPC过程返回后，当前线程继续执行，<br/>
		 * 并从reply中取出RPC过程的返回结果，最后返回_reply中的数据。
		 * @throws RemoteException
		 */
		@Override
		public List<BookInfo> getBookList() throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			List<BookInfo> result;
			Log.i(TAG, "getBookList");
			try {
				data.writeInterfaceToken(DESCRIPTOR);
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
				data.writeInterfaceToken(DESCRIPTOR);
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
		public void registerNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				if (listener != null) {
					data.writeStrongBinder(listener.asBinder());
				} else {
					data.writeInt(0);
				}
				mRemote.transact(TRANSCATION_registerNewBookArrivedListener, data, reply, 0);
			} finally {
				reply.recycle();
				data.recycle();
			}
		}

		@Override
		public void unregisterNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				if (listener != null) {
					data.writeStrongBinder(listener.asBinder());
				} else {
					data.writeInt(0);
				}
				mRemote.transact(TRANSCATION_unregisterNewBookArrivedListener, data, reply, 0);
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
