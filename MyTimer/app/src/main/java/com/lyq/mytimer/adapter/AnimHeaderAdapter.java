package com.lyq.mytimer.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AnimHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	private boolean isOpen;
	private boolean isAnim;

	public AnimHeaderAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == 0) {
			View view = mInflater.inflate(R.layout.item_scene_header, parent, false);
			Header viewHolder = new Header(view);
			return viewHolder;
		} else {
			View view = mInflater.inflate(R.layout.item_scene, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);
			return viewHolder;
		}
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
		if (position == 0 && holder instanceof Header) {
			if (isAnim) {
				ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
				animator.setDuration(4000);
				animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float offset = (float) animation.getAnimatedValue();
						FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((Header) holder).root.getLayoutParams();
						params.height = 800 + (int) (400 * offset);
						((Header) holder).root.setLayoutParams(params);
					}
				});
				animator.start();
				isAnim = false;
				isOpen = ! isOpen;
			}
		}
	}

	public OnListClickListener onClick;

	public void setOnClickListener(OnListClickListener onClick) {
		this.onClick = onClick;
	}

	@Override
	public int getItemCount() {
		return mData.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) return 0;
		else return 1;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView rootText;

		public ViewHolder(View view) {
			super(view);
			rootText = view.findViewById(R.id.rootText);
		}
	}

	class Header extends RecyclerView.ViewHolder {

		ConstraintLayout root;

		public Header(View view) {
			super(view);
			root = view.findViewById(R.id.item_scene);
			view.findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					isAnim = true;
					notifyItemChanged(0);
				}
			});
		}
	}
}
