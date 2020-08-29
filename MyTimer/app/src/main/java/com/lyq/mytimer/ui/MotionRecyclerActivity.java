package com.lyq.mytimer.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.blankj.utilcode.util.LogUtils;
import com.lyq.mytimer.R;
import com.lyq.mytimer.adapter.MotionAdapter;
import com.lyq.mytimer.adapter.OnListClickListener;

import java.util.ArrayList;


public class MotionRecyclerActivity extends AppCompatActivity {

	private RecyclerView rv;
	private MotionAdapter mAdapter;
	private ArrayList<String> mData = new ArrayList<>();
	private boolean isScrollAble = true;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motion_recycler);
		initView();
		initData();
	}

	private class MyLayoutManager extends GridLayoutManager {

		public MyLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
			super(context, attrs, defStyleAttr, defStyleRes);
		}

		public MyLayoutManager(Context context, int spanCount) {
			super(context, spanCount);
		}

		public MyLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
			super(context, spanCount, orientation, reverseLayout);
		}

		@Override
		public boolean canScrollVertically() {
			return isScrollAble;
		}
	}

	private void initView() {
		for (int i = 0; i < 10; i++) {
			mData.add(String.valueOf(i));
		}

		mAdapter = new MotionAdapter(this, mData);
		mAdapter.setOnClickListener(new OnListClickListener() {
			@Override
			public void onTagClick(int tag, int position, @Nullable Object... object) {
				isScrollAble = position == 1;
				LogUtils.eTag("617233", isScrollAble);
			}
		});
		rv = findViewById(R.id.rv);
		rv.setLayoutManager(new MyLayoutManager(this, 2));
//		rv.setLayoutManager(new GridLayoutManager(this, 2));
		rv.setAdapter(mAdapter);
		rv.post(new Runnable() {
			@Override
			public void run() {
				mAdapter.setHeight(rv.getHeight());
			}
		});

		SnapHelper helper = new SnapHelper() {

			@Nullable
			private OrientationHelper mVerticalHelper;

			@Nullable
			@Override
			public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
				int[] out = new int[]{0, 0};
				if (layoutManager.canScrollVertically()) {
					out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
				}
				return out;
			}

			@NonNull
			private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
				if (mVerticalHelper == null || mVerticalHelper.getLayoutManager() != layoutManager) {
					mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
				}
				return mVerticalHelper;
			}

			//targetView的start坐标与RecyclerView的paddingStart之间的差值
			private int distanceToStart(View targetView, OrientationHelper helper) {
				return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
			}

			@Nullable
			@Override
			public View findSnapView(RecyclerView.LayoutManager layoutManager) {
				if (layoutManager instanceof LinearLayoutManager) {
					int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
					if (first == 0) {
						OrientationHelper helper = getVerticalHelper(layoutManager);
						View view = layoutManager.getChildAt(0);
						if (helper.getDecoratedEnd(view) >= helper.getDecoratedMeasurement(view) / 2 && helper.getDecoratedEnd(view) > 0) {
							return view;
						} else {
							return layoutManager.findViewByPosition(first + 1);
						}
					}
				}
				return null;
			}

			@Override
			public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
				return RecyclerView.NO_POSITION;
			}
		};
		helper.attachToRecyclerView(rv);
	}

	private void initData() {
	}

}
