package com.lyq.mytimer.ipc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileShareActivityClient extends BaseActivity {

	private TextView tvName;
	private IPCTestInfo mInfo;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, FileShareActivityClient.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_share_client);

		IPCConstants.initSelf(this);

		tvName = findViewById(R.id.tv_name);

		File cacheFile = new File(IPCConstants.CACHE_FILE_PATH);
		if (cacheFile.exists()) {
			ObjectInputStream stream = null;
			try {
				stream = new ObjectInputStream(new FileInputStream(cacheFile));
				mInfo = (IPCTestInfo) stream.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				MyUtils.close(stream);
			}
		}
		if (mInfo != null) {
			tvName.setText(mInfo.getName());
		}
	}
}
