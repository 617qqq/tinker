package com.lyq.mytimer.ipc.aidl;

import android.os.IInterface;

public interface IoNewBookArrivedListener extends IInterface {

	void newBookArrived(BookInfo book);
}
