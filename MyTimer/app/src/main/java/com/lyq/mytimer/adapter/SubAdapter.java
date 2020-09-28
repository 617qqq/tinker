package com.lyq.mytimer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData = new ArrayList<>();
	private LayoutInflater mInflater;
	public boolean isOpen;

	public SubAdapter(Context context) {
		this.mContext = new WeakReference<>(context);
		Random random = new Random();
		for (int i = 0; i < random.nextInt(14); i++) {
			mData.add(String.valueOf(i));
		}
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
		return isOpen ? mData.size() : Math.min(3, mData.size());
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView rootText;

		public ViewHolder(View view) {
			super(view);
			rootText = view.findViewById(R.id.rootText);
		}
	}
}
