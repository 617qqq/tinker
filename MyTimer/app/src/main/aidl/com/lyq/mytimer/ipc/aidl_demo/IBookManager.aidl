package com.lyq.mytimer.ipc.aidl_demo;

import com.lyq.mytimer.ipc.aidl_demo.Book;
import com.lyq.mytimer.ipc.aidl_demo.IOnNewBookArrivedListener;

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}