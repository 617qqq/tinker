package com.lyq.mytimer.ipc.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.bundle.BundleInfo;

import java.util.List;

public class AIDLActivityMain extends BaseActivity {

	private final String TAG = "AIDLActivityClient";
	private static final String KEY_INFO = "INFO";

	private TextView tvName;
	private BundleInfo mInfo;
	private IBookManager bookManager;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bookManager = BoolManagerImpl.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aidl_main);

		tvName = findViewById(R.id.tv_name);
		connectionService();
	}

	public void connectionService() {
		Intent intent = new Intent(this, BookManagerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}

	public void getList(View view) {
		if (bookManager == null) {
			Toast.makeText(this, "bookManager == null", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			List<BookInfo> list = bookManager.getBookList();
			Log.i(TAG, "onServiceConnected: list type:" + list.getClass().getCanonicalName());
			Log.i(TAG, "onServiceConnected: list content:" + list.toString());
			Log.i(TAG, "onServiceConnected: thread name:" + Thread.currentThread().getName());
			if (Thread.currentThread().getName().equals("main")) {
				StringBuilder bookName = new StringBuilder();
				for (BookInfo bookInfo : list) {
					bookName.append(bookInfo.getName()).append("、");
				}
				tvName.setText(bookName.length() == 0 ? bookName.toString()
						: bookName.deleteCharAt(bookName.length() - 1).toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void register(View view) {

	}

	public void unregister(View view) {
	}
}
