package com.lyq.mytimer.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyq.mytimer.R;

public class GoToFileManagerActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goto_file_manager);
	}

	public void onClickFile(View view) {
		PackageManager packageManager = this.getPackageManager();
		//Intent intent= packageManager.getLaunchIntentForPackage("com.dfzt.filemanager.main");
		Intent intent= packageManager.getLaunchIntentForPackage("com.android.rockchip");
		startActivity(intent);
	}
}
