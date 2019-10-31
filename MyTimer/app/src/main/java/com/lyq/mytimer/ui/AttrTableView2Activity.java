package com.lyq.mytimer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lyq.attr_graph.AttrGraphView;
import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttrTableView2Activity extends BaseActivity {

	private AttrGraphView attrGraphView;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, AttrTableView2Activity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attr_table2);

		attrGraphView = findViewById(R.id.attr_graph);
		Double[] region = {0d, 1d, 2d, 3d, 4d, 5d};
		List<Double> regionList = Arrays.asList(region);
		List<List<Double>> lists = new ArrayList<>();
		lists.add(regionList);
		lists.add(regionList);
		lists.add(regionList);
		lists.add(regionList);
		lists.add(regionList);
		attrGraphView.setRegion(lists);
	}

	public void random(View view) {
		Double[] doubles = {3.4d, 3.4d, 3.4d, 3.4d, 3.4d};
		attrGraphView.mValue.add(Arrays.asList(doubles));
		attrGraphView.startAddAnim();
	}

	public void animation(View view) {
		attrGraphView.startInitAnimation();
	}

	public void onClickReduceFrame(View view) {
		attrGraphView.setFrameSize(attrGraphView.getFrameSize() - 1);
	}

	public void onClickAddAttr(View view) {
		attrGraphView.setAttrSize(attrGraphView.getAttrSize() + 1);
	}

	public void onClickReduceAttr(View view) {
		attrGraphView.setAttrSize(attrGraphView.getAttrSize() - 1);
	}

	public void onClickAddFrame(View view) {
		attrGraphView.setFrameSize(attrGraphView.getFrameSize() + 1);
	}
}
