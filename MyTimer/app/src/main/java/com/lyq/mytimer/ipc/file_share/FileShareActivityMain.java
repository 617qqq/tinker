package com.lyq.mytimer.ipc.file_share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.IPCConstants;
import com.lyq.mytimer.ipc.MyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileShareActivityMain extends BaseActivity {

	private TextView tvName;
	private FileShareInfo mInfo = new FileShareInfo("Android");
	private int tag;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_share_main);

		IPCConstants.initSelf(this);
		tvName = findViewById(R.id.tv_name);
		updateNameView();
	}

	public void goRemote(View view) {
		FileShareActivityClient.startAction(this);
	}

	public void updateNameView() {
		tvName.setText(mInfo.getName());
	}

	public void updateData(View view) {
		mInfo.setName("Android" + ++tag);
		updateNameView();

		File dir = new File(IPCConstants.CACHE_DIR_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File cacheFile = new File(IPCConstants.CACHE_FILE_PATH);
		ObjectOutputStream stream = null;
		try {
			stream = new ObjectOutputStream(new FileOutputStream(cacheFile));
			stream.writeObject(mInfo);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			MyUtils.close(stream);
		}
	}
}
