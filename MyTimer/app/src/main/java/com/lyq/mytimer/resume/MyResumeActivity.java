package com.lyq.mytimer.resume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dingmouren.layoutmanagergroup.echelon.EchelonLayoutManager;
import com.lyq.mytimer.R;
import com.lyq.mytimer.adapter.MyResumeAdapter;
import com.lyq.mytimer.base.BaseActivity;

import java.util.ArrayList;

public class MyResumeActivity extends BaseActivity {

	private RecyclerView mRv;
	private MyResumeAdapter mAdapter;

	private ArrayList<String> mData = new ArrayList<>();

	public static void startAction(Context context) {
		Intent intent = new Intent(context, MyResumeActivity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resume);

		mRv = findViewById(R.id.rv_data);
		mRv.setLayoutManager(new EchelonLayoutManager(this));
		for (int i = 0; i < 10; i++) {
			mData.add(String.valueOf(i));
		}
		mAdapter = new MyResumeAdapter(this, mData);
		mRv.setAdapter(mAdapter);
	}
}
