package com.lyq.mytimer.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.adapter.AnimHeaderAdapter;
import com.lyq.mytimer.adapter.SceneAdapter;

import java.util.ArrayList;


public class SceneRecyclerActivity extends AppCompatActivity {

	private RecyclerView rv;
	private AnimHeaderAdapter mAdapter;
	private ArrayList<String> mData = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_recycle);
		initView();
		initData();
	}

	private void initView() {
		for (int i = 0; i < 100; i++) {
			mData.add(String.valueOf(i));
		}
		mAdapter = new AnimHeaderAdapter(this, mData);
		rv = findViewById(R.id.rv);
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(mAdapter);
	}

	private void initData() {
	}

}
