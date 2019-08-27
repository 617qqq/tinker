package com.lyq.mytimer.ipc.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

public interface IBookManager extends IInterface {

	String DESCRITOR = "com.lyq.mytimer.ipc.aidl.IBookManager";

	int TRANSCATION_getBookList = IBinder.FIRST_CALL_TRANSACTION;
	int TRANSCATION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

	List<BookInfo> getBookList() throws RemoteException;

	void addBook(BookInfo info) throws RemoteException;

}
