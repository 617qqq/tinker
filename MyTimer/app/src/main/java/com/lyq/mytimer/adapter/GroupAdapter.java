package com.lyq.mytimer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private LayoutInflater mInflater;

	public GroupAdapter(Context context) {
		this.mContext = new WeakReference<>(context);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.item_recycler, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.tvClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.subAdapter.isOpen = ! holder.subAdapter.isOpen;
				holder.subAdapter.notifyDataSetChanged();
			}
		});
	}

	public OnListClickListener onClick;

	public void setOnClickListener(OnListClickListener onClick) {
		this.onClick = onClick;
	}

	@Override
	public int getItemCount() {
		return 5;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvClick;
		RecyclerView itemRv;
		SubAdapter subAdapter;

		public ViewHolder(View view) {
			super(view);
			tvClick = view.findViewById(R.id.tvClick);
			itemRv = view.findViewById(R.id.itemRv);
			subAdapter = new SubAdapter(mContext.get());
			itemRv.setLayoutManager(new LinearLayoutManager(mContext.get()));
			itemRv.setNestedScrollingEnabled(false);
			itemRv.setHasFixedSize(true);
			itemRv.setAdapter(subAdapter);
		}
	}
}
