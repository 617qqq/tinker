package com.lyq.mytimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lyq.mytimer.adapter.MainAdapter;
import com.lyq.mytimer.adapter.OnListClickListener;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.info.ModuleInfo;
import com.lyq.mytimer.resume.MyResumeActivity;
import com.lyq.mytimer.ui.AttrTableViewActivity;
import com.lyq.mytimer.ui.ControlActivity;
import com.lyq.mytimer.ui.DebutActivity;
import com.lyq.mytimer.ui.MusicAnimActivity;
import com.lyq.mytimer.ui.Rotate3dAnimationActivity;
import com.lyq.mytimer.ui.TimeActivity;
import com.lyq.mytimer.view.MyTimeView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

	private MainAdapter mAdapter;
	private RecyclerView rvData;
	private ArrayList<ModuleInfo> mData = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		setData();
	}

	private void setData() {
		mData.add(new ModuleInfo(TimeActivity.class, "时间罗盘", "罗盘样式的时钟"));
		mData.add(new ModuleInfo(AttrTableViewActivity.class, "属性图", "5维属性图"));
		mData.add(new ModuleInfo(DebutActivity.class, "登场动画", "背景灯旋转动画"));
		mData.add(new ModuleInfo(MusicAnimActivity.class, "音乐动画", "网易云孤独星球动画"));
		mData.add(new ModuleInfo(Rotate3dAnimationActivity.class, "3D动画", "翻盘动画效果"));
		mData.add(new ModuleInfo(ControlActivity.class, "控制按钮", "不规则区域点击事件"));

		mAdapter.notifyDataSetChanged();
	}

	private void initView() {
		rvData = findViewById(R.id.rv_data);
		rvData.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new MainAdapter(this, mData);
		rvData.setAdapter(mAdapter);

		mAdapter.setOnClickListener(new OnListClickListener() {
			@Override
			public void onTagClick(int tag, int position, @Nullable Object... object) {
				mData.get(position).start(MainActivity.this);
			}
		});
	}


}
