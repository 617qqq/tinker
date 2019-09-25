package com.lyq.mytimer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.DebutDrawable;

public class DebutActivity extends BaseActivity {

	public static void start(Context context) {
	    Intent starter = new Intent(context, DebutActivity.class);
	    context.startActivity(starter);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debut);

		TextView textView = findViewById(R.id.tv_content);
		textView.setBackground(new DebutDrawable());
	}
}
