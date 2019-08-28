package com.lyq.mytimer.ipc.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

public interface IBookManager extends IInterface {

	String DESCRIPTOR = "com.lyq.mytimer.ipc.aidl.IBookManager";

	int TRANSCATION_getBookList = IBinder.FIRST_CALL_TRANSACTION;
	int TRANSCATION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
	int TRANSCATION_registerNewBookArrivedListener = IBinder.FIRST_CALL_TRANSACTION + 2;
	int TRANSCATION_unregisterNewBookArrivedListener = IBinder.FIRST_CALL_TRANSACTION + 3;

	List<BookInfo> getBookList() throws RemoteException;

	void addBook(BookInfo info) throws RemoteException;

	void registerNewBookArrivedListener(IoNewBookArrivedListener listener) throws RemoteException;

	void unregisterNewBookArrivedListener(IoNewBookArrivedListener listener) throws RemoteException;
}
