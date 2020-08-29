package com.lyq.mytimer.adapter;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lyq.mytimer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SceneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	private boolean isOpen;
	private boolean isAnim;
	private Scene mSceneStart;
	private Scene mSceneEnd;

	public SceneAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == 0) {
			View view = mInflater.inflate(R.layout.item_scene_header, parent, false);
			Header viewHolder = new Header(view);
			mSceneStart = Scene.getSceneForLayout(viewHolder.root, R.layout.item_scene_header, mContext.get());
			mSceneEnd = Scene.getSceneForLayout(viewHolder.root, R.layout.item_scene_header_open, mContext.get());
			return viewHolder;
		} else {
			View view = mInflater.inflate(R.layout.item_scene, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);
			return viewHolder;
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (position == 0 && holder instanceof Header) {
			if (isAnim) {
				TransitionManager.go(isOpen ? mSceneEnd : mSceneStart, new ChangeBounds());
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
			root.findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					isAnim = true;
					notifyItemChanged(0);
				}
			});
		}
	}
}
