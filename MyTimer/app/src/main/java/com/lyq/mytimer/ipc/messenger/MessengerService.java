package com.lyq.mytimer.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.lyq.mytimer.ipc.IPCConstants;
import com.lyq.mytimer.ipc.messenger.BookInfo;

import java.util.ArrayList;

public class MessengerService extends Service {

	private static final String TAG = "MessengerService";

	private static ArrayList<BookInfo> bookInfo = new ArrayList<>();

	private static class MessengerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case IPCConstants.MESSAGE_GET_LIST:
					Message replyGetListMessage = Message.obtain(null, IPCConstants.MESSAGE_GET_LIST);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("book_list", bookInfo);
					bundle.putString("msg", "获取成功");
					replyGetListMessage.setData(bundle);
					try {
						msg.replyTo.send(replyGetListMessage);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case IPCConstants.MESSAGE_ADD:
					Message replyAddMessage = Message.obtain(null, IPCConstants.MESSAGE_ADD);
					Bundle getBundle = msg.getData();
					//android.os.BadParcelableException: ClassNotFoundException when unmarshalling
					getBundle.setClassLoader(BookInfo.class.getClassLoader());
					BookInfo info = getBundle.getParcelable("book");
					Bundle replyData = new Bundle();
					if (info != null) {
						bookInfo.add(info);
						replyData.putString("msg", getBundle.getString("msg"));
					} else {
						replyData.putString("msg", "添加失败");
					}
					replyAddMessage.setData(replyData);
					try {
						msg.replyTo.send(replyAddMessage);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

	/**
	 * 创建一个Service来处理客户端请求，同时创建一个Handler并通过它来创建一个
	 * Messenger，然后再Service的onBind中返回Messenger对象底层的Binder即可。
	 */
	private final Messenger mMessenger = new Messenger(new MessengerHandler());

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
}
