package com.lyq.mytimer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.adapter.MainAdapter;
import com.lyq.mytimer.adapter.OnListClickListener;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.info.ModuleInfo;
import com.lyq.mytimer.ipc.socket.TCPClientActivity;
import com.lyq.mytimer.ui.AttrTableView2Activity;
import com.lyq.mytimer.ui.AttrTableViewActivity;
import com.lyq.mytimer.ui.ControlActivity;
import com.lyq.mytimer.ui.DebutActivity;
import com.lyq.mytimer.ui.MusicAnimActivity;
import com.lyq.mytimer.ui.Rotate3dAnimationActivity;
import com.lyq.mytimer.ui.RvAnimActivity;
import com.lyq.mytimer.ui.SceneChangeBoundsActivity;
import com.lyq.mytimer.ui.ShadowActivity;
import com.lyq.mytimer.ui.TimeActivity;
import com.lyq.mytimer.ui.VectorActivity;
import com.lyq.mytimer.ui.WebActivity;
import com.lyq.mytimer.ui.WebX5Activity;

import java.util.ArrayList;

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
		mData.add(new ModuleInfo(AttrTableView2Activity.class, "属性图", "5维属性图"));
		mData.add(new ModuleInfo(DebutActivity.class, "登场动画", "背景灯旋转动画"));
		mData.add(new ModuleInfo(MusicAnimActivity.class, "音乐动画", "网易云孤独星球动画"));
		mData.add(new ModuleInfo(Rotate3dAnimationActivity.class, "3D动画", "翻盘动画效果"));
		mData.add(new ModuleInfo(ControlActivity.class, "控制按钮", "不规则区域点击事件"));
		mData.add(new ModuleInfo(ShadowActivity.class, "阴影", "阴影Drawable"));
		mData.add(new ModuleInfo(TCPClientActivity.class, "TCP", "IPC-socket"));
		mData.add(new ModuleInfo(SceneChangeBoundsActivity.class, "TransitionManager", "change bounds"));


		mData.add(new ModuleInfo(WebActivity.class, "WebActivity", "WebActivity"));
		mData.add(new ModuleInfo(WebX5Activity.class, "WebX5Activity", "WebX5Activity"));

		mData.add(new ModuleInfo(VectorActivity.class, "Vector动画", "路径动画"));
		mData.add(new ModuleInfo(RvAnimActivity.class, "RecyclerView的选中动画", "RecyclerView的选中动画"));

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
