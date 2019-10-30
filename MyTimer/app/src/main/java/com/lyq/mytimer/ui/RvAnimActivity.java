package com.lyq.mytimer.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.adapter.AnimAdapter;

import java.util.ArrayList;

public class RvAnimActivity extends AppCompatActivity {

	RecyclerView rvData;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rv_anim);

		rvData = findViewById(R.id.rv_data);
		rvData.setLayoutManager(new LinearLayoutManager(this));
		ArrayList<String> mData = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			mData.add(String.valueOf(i));
		}
		AnimAdapter animAdapter = new AnimAdapter(this, mData);

		rvData.setAdapter(animAdapter);
	}
}
