package com.lyq.mytimer.resume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;

public class MyResumeActivity extends BaseActivity {

	public static void startAction(Context context) {
		Intent intent = new Intent(context, MyResumeActivity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resume);
	}
}
