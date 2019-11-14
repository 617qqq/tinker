package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyq.mytimer.R;
import com.tencent.bugly.beta.Beta;

public class UpgradeActivity extends AppCompatActivity {


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade);

	}

	public void onClickUpgrade(View view) {
		Beta.checkUpgrade();
	}

}
