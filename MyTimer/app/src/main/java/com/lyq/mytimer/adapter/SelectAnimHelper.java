package com.lyq.mytimer.adapter;

import android.animation.ValueAnimator;
import androidx.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;

/**
 * 通过View的tag区分动画目标
 */
public abstract class SelectAnimHelper<T extends View, H> {

	/** 保存取消选中动画，用来在重新选中后退出 */
	private SparseArray<ValueAnimator> animMap = new SparseArray<>();
	/** 保存相关变化信息，比如目标View的长度 */
	private SparseArray<H> valueMap = new SparseArray<>();
	/** 选中动画，启动前先退出前一个选中动画 */
	private ValueAnimator anim;
	/** 当前选中Index */
	private int selectedIndex = - 1;
	/** 保存所有目标View，setTag(position)，用tag中的index区分动画目标 */
	private ArrayList<T> viewList = new ArrayList<>();

	public SelectAnimHelper() {
	}

	public SelectAnimHelper(int defaultSelectedIndex, H defaultValue) {
		selectedIndex = defaultSelectedIndex;
		valueMap.put(selectedIndex, defaultValue);
	}

	public void setDefault(int defaultSelectedIndex, H defaultValue) {
		selectedIndex = defaultSelectedIndex;
		valueMap.put(selectedIndex, defaultValue);
	}

	/**
	 * 在Adapter的ViewHolder构造方法中调用
	 */
	public void addView(T view) {
		viewList.add(view);
	}

	@Nullable
	public H getValue(int position) {
		return valueMap.get(position);
	}

	public void setSelected(int position) {
		int last = selectedIndex;
		selectedIndex = position;
		if (position == last) {
			return;
		}

		cancelRemoveUnSelectedAnimIfExist(position);
		startSelectedAnim(position);
		addUnSelectedAnim(last);
	}

	@Nullable
	T getView(int targetIndex) {
		for (T view : viewList) {
			int position = (int) view.getTag();
			if (position == targetIndex) {
				return view;
			}
		}
		return null;
	}

	void setValue(int position, H value) {
		valueMap.put(position, value);
	}

	/**
	 * 退出选中项的取消选中动画，如果存在的话
	 */
	private void cancelRemoveUnSelectedAnimIfExist(int selected) {
		ValueAnimator unSelectAnim = animMap.get(selected);
		if (unSelectAnim != null) {
			unSelectAnim.cancel();
			animMap.remove(selected);
		}
	}

	private void startSelectedAnim(int position) {
		if (anim != null) {
			anim.cancel();
		}
		anim = buildSelectedAnim(position);
		if (anim != null) {
			anim.start();
		}
	}

	private void addUnSelectedAnim(int last) {
		ValueAnimator unAnim = buildUnSelectedAnim(last);
		if (unAnim != null) {
			unAnim.start();
			animMap.put(last, unAnim);
		}
	}

	protected abstract ValueAnimator buildUnSelectedAnim(int last);

	protected abstract ValueAnimator buildSelectedAnim(int position);
}
