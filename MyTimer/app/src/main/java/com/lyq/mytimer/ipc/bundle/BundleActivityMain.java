package com.lyq.mytimer.ipc.bundle;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.ipc.IPCConstants;

public class BundleActivityMain extends BaseActivity {

	private TextView tvName;
	private BundleInfo mInfo = new BundleInfo("Android");
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
		BundleActivityClient.startAction(this, mInfo);
	}

	public void updateNameView() {
		tvName.setText(mInfo.getName());
	}

	public void updateData(View view) {
		mInfo.setName("Android" + ++tag);
		mInfo.setHard(String.valueOf(tag));
		updateNameView();
	}
}
