package com.lyq.mytimer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.AttrTableView;

import java.util.ArrayList;
import java.util.Random;

public class AttrTableViewActivity extends BaseActivity {

	private AttrTableView attrTableView;
	private TextView tvGrade;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, AttrTableViewActivity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
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

		tvGrade = findViewById(R.id.tv_grade);
		tvGrade.setText(attrTableView.getGrade());
	}

	public void random(View view) {
		ArrayList<Float> data = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			data.add(random.nextInt(20) + random.nextFloat() + 10);
		}
		tvGrade.setText(attrTableView.setData(data));
	}

	public void animation(View view) {
		attrTableView.startInitAnimation();
	}
}
