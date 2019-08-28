package com.lyq.mytimer.ipc.aidl;

import android.os.IBinder;

public interface IoNewBookArrivedListener extends IBinder {

	void newBookArrived(BookInfo book);
}
