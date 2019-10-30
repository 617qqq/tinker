package com.lyq.mytimer.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	private int targetWidth;
	private SelectAnimWidthHelper helper;

	public AnimAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);

		targetWidth = context.getResources().getDimensionPixelOffset(R.dimen.dp_300);
		helper = new SelectAnimWidthHelper();
		helper.setDefault(0, targetWidth);
		helper.setTargetWidth(targetWidth);
		helper.setDuration(1000);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.item_select, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.root.setTag(position);

		holder.tvTag.setTag(position);
		ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.tvTag.getLayoutParams();
		params.width = helper.getValue(position);
		holder.tvTag.setLayoutParams(params);
	}

	public OnListClickListener onClick;

	public void setOnClickListener(OnListClickListener onClick) {
		this.onClick = onClick;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvTag;
		View root;

		public ViewHolder(View view) {
			super(view);
			tvTag = view.findViewById(R.id.tv_tag);
			helper.addView(tvTag);

			root = view.findViewById(R.id.root_item_select);
			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = (int) v.getTag();
					helper.setSelected(position);
				}
			});
		}
	}
}
