package com.lyq.mytimer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MotionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	private int height;
	private int itemHeight;
	private TextView footer;
	private MotionLayout header;
	private int spanCount = 1;

	public MotionAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);
	}

	public void setHeight(int height) {
		this.height = height;
		if (height != 0 && header != null) {
			header.getConstraintSet(R.id.end).getConstraint(R.id.bg)
					.layout.mHeight = height;
		}
		setFooterHeight();
	}

	private void setFooterHeight() {
		if (footer != null && itemHeight != 0 && height != 0) {
			int line = mData.size() / spanCount;
			int offset = Math.max(0, height - itemHeight * line);
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) footer.getLayoutParams();
			params.height = offset;
			footer.setLayoutParams(params);
		}
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager) {
			final GridLayoutManager gridManager = ((GridLayoutManager) manager);
			spanCount = gridManager.getSpanCount();
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
				@Override
				public int getSpanSize(int position) {
					// 如果当前是footer或者头部的位置，那么该item占据一行的单元格，正常情况下占据1个单元格
					return getItemViewType(position) == 0 || getItemViewType(position) == - 1 ? spanCount : 1;
				}
			});
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == 0) {
			View view = mInflater.inflate(R.layout.item_motion_layout, parent, false);
			Header viewHolder = new Header(view);
			return viewHolder;
		} else if (viewType == 1) {
			View view = mInflater.inflate(R.layout.item_scene, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);
			return viewHolder;
		} else {
			return new Footer(mInflater.inflate(R.layout.item_empty_footer, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
	}

	public OnListClickListener onClick;

	public void setOnClickListener(OnListClickListener onClick) {
		this.onClick = onClick;
	}

	@Override
	public int getItemCount() {
		return mData.size() + 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) return 0;
		else if (position == getItemCount() - 1) return - 1;
		else return 1;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView rootText;

		public ViewHolder(final View view) {
			super(view);
			rootText = view.findViewById(R.id.rootText);
			view.post(new Runnable() {
				@Override
				public void run() {
					if (itemHeight == 0 && view.getHeight() != 0) {
						itemHeight = view.getHeight();
						setFooterHeight();
					}
				}
			});
		}
	}

	class Footer extends RecyclerView.ViewHolder {

		public Footer(View view) {
			super(view);
			footer = view.findViewById(R.id.footer);
		}
	}

	class Header extends RecyclerView.ViewHolder {

		boolean isStop = true;

		public Header(View view) {
			super(view);
			header = view.findViewById(R.id.motionLayout);
			view.findViewById(R.id.bg).setOnTouchListener(new View.OnTouchListener() {

				float y;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if (isStop) {
								y = event.getY();
								return true;
							}
							break;
						case MotionEvent.ACTION_MOVE:
							if (header.getCurrentState() == R.id.start && event.getY() > y) {
								header.transitionToEnd();
								return true;
							} else if (header.getCurrentState() == R.id.end && event.getY() < y) {
								header.transitionToStart();
								return true;
							}
							break;
					}
					return false;
				}
			});
			header.addTransitionListener(new MotionLayout.TransitionListener() {
				@Override
				public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
					onClick.onTagClick(OnListClickListener.TAG_1, 0);
					isStop = false;
				}

				@Override
				public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

				}

				@Override
				public void onTransitionCompleted(MotionLayout motionLayout, int i) {
					LogUtils.eTag("617233", motionLayout.getCurrentState());
					isStop = true;
					if (motionLayout.getCurrentState() == R.id.start) {
						onClick.onTagClick(OnListClickListener.TAG_1, 1);
					}
				}

				@Override
				public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

				}
			});
			if (height != 0) {
				header.getConstraintSet(R.id.end).getConstraint(R.id.bg)
						.layout.mHeight = height;
			}
		}
	}
}
