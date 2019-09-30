package com.lyq.mytimer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.FunDevControlView;

public class ControlActivity extends BaseActivity {

	public static void start(Context context) {
	    Intent starter = new Intent(context, ControlActivity.class);
	    context.startActivity(starter);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		FunDevControlView control = findViewById(R.id.fun_dev_control);
		control.setListener(new FunDevControlView.OnIndexChangeListener() {
			@Override
			public void onChange(int index) {
				Log.d("617233", "onChange: " + index);
			}
		});
	}
}
