package com.lyq.mytimer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.AttrTableView;

public class AttrTableViewActivity extends BaseActivity {

	private AttrTableView attrTableView;

	public static void startAction(Context context) {
	    Intent intent = new Intent(context, AttrTableViewActivity.class);
	    context.startActivity(intent);
	    startActivityAnim((Activity)context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attr_table);

		attrTableView = findViewById(R.id.attr_table_view);
		attrTableView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attrTableView.startAnimation();
			}
		});
	}
}
