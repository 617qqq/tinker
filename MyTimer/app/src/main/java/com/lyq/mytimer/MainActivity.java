package com.lyq.mytimer;

import android.os.Bundle;

import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.resume.MyResumeActivity;
import com.lyq.mytimer.view.MyTimeView;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MyTimeView view = findViewById(R.id.time);
		view.setOnKeyConfirm(new MyTimeView.OnKeyConfirm() {
			@Override
			public void on(boolean isConfirm) {
				if (isConfirm) {
					MyResumeActivity.startAction(MainActivity.this);
				}
			}
		});
	}
}
