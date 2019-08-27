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

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.bundle.BundleInfo;

import java.util.List;

public class AIDLActivityMain extends BaseActivity {

	private final String TAG = "AIDLActivityClient";
	private static final String KEY_INFO = "INFO";

	private TextView tvName;
	private BundleInfo mInfo;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			IBookManager manager = BoolManagerImpl.asInterface(service);
			try {
				List<BookInfo> list = manager.getBookList();
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

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_share_main);

		tvName = findViewById(R.id.tv_name);
	}

	public void goRemote(View view) {
	}

	public void updateNameView() {
	}

	public void updateData(View view) {
		Intent intent = new Intent(this, BookManagerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}
}
