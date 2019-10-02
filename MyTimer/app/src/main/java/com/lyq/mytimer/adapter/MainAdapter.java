package com.lyq.mytimer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.info.ModuleInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<ModuleInfo> mData;
	private LayoutInflater mInflater;

	public MainAdapter(Context context, ArrayList<ModuleInfo> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.item_module, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ModuleInfo info = mData.get(position);
		holder.tvTitle.setText(info.getTitleName());
		holder.tvDesc.setText(info.getDesc());

		holder.root.setTag(position);
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

		View root;
		TextView tvTitle;
		TextView tvDesc;

		public ViewHolder(View view) {
			super(view);
			root = view.findViewById(R.id.root_item_module);
			tvTitle = view.findViewById(R.id.tv_title);
			tvDesc = view.findViewById(R.id.tv_desc);

			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onClick != null) {
						int position = (int) v.getTag();
						onClick.onTagClick(OnListClickListener.TAG_1, position);
					}
				}
			});
		}
	}
}
