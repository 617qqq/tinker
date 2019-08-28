package com.lyq.mytimer.ipc.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IOnNewBookArrivedListener extends IInterface {

	String DESCRIPTOR = "com.lyq.mytimer.ipc.aidl.IOnNewBookArrivedListener";

	int TRANSCATION_newBookArrived = IBinder.FIRST_CALL_TRANSACTION;

	void newBookArrived(BookInfo book) throws RemoteException;
}
