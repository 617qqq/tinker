package com.lyq.mytimer.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyq.mytimer.R;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * animMap保存取消选中动画，用来在重新选中后退出
 * widthMap保存宽度，用来设置动画初始状态
 * anim选中动画，启动前先退出前一个选中动画
 * tagList保存所有目标View，setTag(position)，用tag中的index区分动画目标
 */
public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.ViewHolder> {

	private WeakReference<Context> mContext;
	private ArrayList<String> mData;
	private LayoutInflater mInflater;

	private int selectedIndex = - 1;
	private SparseArray<ValueAnimator> animMap = new SparseArray<>();
	private SparseIntArray widthMap = new SparseIntArray();
	private ValueAnimator anim;
	private ArrayList<TextView> tagList = new ArrayList<>();
	private int targetWidth;

	public AnimAdapter(Context context, ArrayList<String> data) {
		this.mContext = new WeakReference<>(context);
		this.mData = data;
		mInflater = LayoutInflater.from(context);

		targetWidth = context.getResources().getDimensionPixelOffset(R.dimen.dp_300);
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
		if (position == 0 && selectedIndex == - 1) {
			params.width = targetWidth;
			widthMap.put(0, targetWidth);
			selectedIndex = 0;
		} else {
			params.width = widthMap.get(position, 0);
		}
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
			tagList.add(tvTag);

			root = view.findViewById(R.id.root_item_select);
			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int last = selectedIndex;
					selectedIndex = (int) v.getTag();
					int selected = selectedIndex;
					if (selectedIndex == last) {
						return;
					}

					cancelRemoveUnSelectedAnimIfExist(selected);
					startSelectedAnim(selected);
					addUnSelectedAnim(last);
				}

				private void cancelRemoveUnSelectedAnimIfExist(int selected) {
					ValueAnimator unSelectAnim = animMap.get(selected);
					if (unSelectAnim != null) {
						unSelectAnim.cancel();
						animMap.remove(selected);
					}
				}

				private void startSelectedAnim(int selected) {
					if (anim != null) {
						anim.cancel();
					}
					anim = ValueAnimator.ofInt(tvTag.getWidth(), targetWidth);
					anim.setDuration(getDuration(tvTag.getWidth(), true));
					anim.addUpdateListener(getListener(selected));
					anim.start();
				}

				private void addUnSelectedAnim(int last) {
					int lastViewWidth = widthMap.get(last, 0);
					ValueAnimator unAnim = ValueAnimator.ofInt(lastViewWidth, 0);
					unAnim.setDuration(getDuration(lastViewWidth, false));
					unAnim.addUpdateListener(getListener(last));
					unAnim.start();
					animMap.put(last, unAnim);
				}

				private long getDuration(int width, boolean isSelected) {
					float offset = (float) (isSelected ? (targetWidth - width) : width) / (float) targetWidth;
					return (long) (1000 * offset);
				}

				@NotNull
				private ValueAnimator.AnimatorUpdateListener getListener(final int targetIndex) {
					return new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							TextView target = null;
							for (TextView textView : tagList) {
								int position = (int) textView.getTag();
								if (position == targetIndex) {
									target = textView;
									break;
								}
							}
							int animatedValue = (int) animation.getAnimatedValue();
							widthMap.put(targetIndex, animatedValue);
							if (target != null) {
								ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) target.getLayoutParams();
								params.width = animatedValue;
								target.setLayoutParams(params);
							}
						}
					};
				}
			});
		}
	}
}
