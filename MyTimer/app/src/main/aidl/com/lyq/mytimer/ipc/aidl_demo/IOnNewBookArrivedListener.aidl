package com.lyq.mytimer.ipc.aidl_demo;

import com.lyq.mytimer.ipc.aidl_demo.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
