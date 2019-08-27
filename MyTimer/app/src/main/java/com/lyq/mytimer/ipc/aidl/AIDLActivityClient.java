package com.lyq.mytimer.ipc.aidl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.IPCConstants;
import com.lyq.mytimer.ipc.bundle.BundleInfo;

public class AIDLActivityClient extends BaseActivity {

	private static final String KEY_INFO = "INFO";

	private TextView tvName;
	private BundleInfo mInfo;

	public static void startAction(Context context, BundleInfo info) {
		Intent intent = new Intent(context, AIDLActivityClient.class);
		intent.putExtra(KEY_INFO, info);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_share_client);

		IPCConstants.initSelf(this);

		tvName = findViewById(R.id.tv_name);
		mInfo = getIntent().getParcelableExtra(KEY_INFO);
		if (mInfo != null) {
			tvName.setText(String.format("%s\n%s", mInfo.getName(), mInfo.getHard()));
		}
	}
}
