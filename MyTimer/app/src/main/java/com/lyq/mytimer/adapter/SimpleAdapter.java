package com.lyq.mytimer.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	public SimpleAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.item_simple, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.rootText.setText(String.valueOf(position));
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

		TextView rootText;

		public ViewHolder(View view) {
			super(view);
			rootText = view.findViewById(R.id.rootText);
		}
	}
}
