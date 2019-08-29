package com.lyq.mytimer.ipc.messenger;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.IPCConstants;

import java.util.ArrayList;

public class MessengerActivityMain extends BaseActivity {

	private final String TAG = "AIDLActivityClient";

	private TextView tvName;

	private Messenger mService;

	private int size;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = new Messenger(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	@SuppressLint("HandlerLeak")
	private Messenger mGetReplyMessenger = new Messenger(new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			bundle.setClassLoader(BookInfo.class.getClassLoader());
			switch (msg.what) {
				case IPCConstants.MESSAGE_GET_LIST:
					ArrayList<BookInfo> list = bundle.getParcelableArrayList("book_list");
					if (list != null) {
						StringBuilder bookName = new StringBuilder();
						for (BookInfo bookInfo : list) {
							bookName.append(bookInfo.getName()).append("、");
						}
						tvName.setText(bookName.length() == 0 ? bookName.toString()
								: bookName.deleteCharAt(bookName.length() - 1).toString());
					}
					Toast.makeText(MessengerActivityMain.this, bundle.getString("msg"), Toast.LENGTH_SHORT).show();
					break;
				case IPCConstants.MESSAGE_ADD:
					Toast.makeText(MessengerActivityMain.this, bundle.getString("msg"), Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	});

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger_main);

		tvName = findViewById(R.id.tv_name);
	}

	public void connect(View view) {
		if (mService == null) {
			Intent intent = new Intent(this, MessengerService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	public void add(View view) {
		if (mService != null) {
			Message msg = Message.obtain(null, IPCConstants.MESSAGE_ADD);
			Bundle data = new Bundle();
			data.putParcelable("book", new BookInfo("Android#" + ++size));
			data.putString("msg", "添加成功");
			msg.setData(data);
			msg.replyTo = mGetReplyMessenger;
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "Please connect service first", Toast.LENGTH_SHORT).show();
		}
	}

	public void getList(View view) {
		if (mService != null) {
			Message msg = Message.obtain(null, IPCConstants.MESSAGE_GET_LIST);
			Bundle data = new Bundle();
			msg.setData(data);
			msg.replyTo = mGetReplyMessenger;
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "Please connect service first", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}
}
