package com.lyq.mytimer.ipc.aidl;

import android.os.IInterface;

public interface IOnNewBookArrivedListener extends IInterface {

	void newBookArrived(BookInfo book);
}
