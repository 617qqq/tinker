package com.lyq.mytimer.ui.recycler;

import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemAnimator extends DefaultItemAnimator {

	@Override
	public boolean animateAdd(RecyclerView.ViewHolder holder) {
		Log.e("animateAdd", String.valueOf(holder.getAdapterPosition()));
		return super.animateAdd(holder);
	}

	@Override
	public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
		Log.e("animateChange", String.valueOf(oldHolder.getAdapterPosition()) + " newHolder: " + newHolder.getAdapterPosition());
		return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
	}

	@Override
	public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
		Log.e("animateMove", String.valueOf(holder.getAdapterPosition()));
		return super.animateMove(holder, fromX, fromY, toX, toY);
	}

	@Override
	public boolean animateRemove(RecyclerView.ViewHolder holder) {
		Log.e("animateRemove", String.valueOf(holder.getAdapterPosition()));
		return super.animateRemove(holder);
	}
}
