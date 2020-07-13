package com.lyq.mytimer.ui.recycler;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class MyLayoutManager extends LinearLayoutManager {
	public MyLayoutManager(Context context) {
		super(context);
	}

	public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	public MyLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	private float MILLISECONDS_PER_INCH = 25f;  //修改可以改变数据,越大速度越慢

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
		LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
			@Override
			public PointF computeScrollVectorForPosition(int targetPosition) {
				return MyLayoutManager.this.computeScrollVectorForPosition(targetPosition);
			}

			@Override
			protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
				return MILLISECONDS_PER_INCH / displayMetrics.density; //返回滑动一个pixel需要多少毫秒
			}

		};
		linearSmoothScroller.setTargetPosition(position);
		startSmoothScroll(linearSmoothScroller);
	}
}











