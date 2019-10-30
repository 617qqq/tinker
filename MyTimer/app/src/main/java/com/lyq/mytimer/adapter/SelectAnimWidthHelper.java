package com.lyq.mytimer.adapter;

import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.TextView;

public class SelectAnimWidthHelper extends SelectAnimHelper<TextView, Integer> {

	private int targetWidth;
	private int duration;

	public void setTargetWidth(int width) {
		targetWidth = width;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@NonNull
	@Override
	public Integer getValue(int position) {
		Integer result = super.getValue(position);
		return result == null ? 0 : result;
	}

	@Override
	protected ValueAnimator buildUnSelectedAnim(int last) {
		Integer viewWidth = getValue(last);
		ValueAnimator unAnim = ValueAnimator.ofInt(viewWidth, 0);
		unAnim.setDuration(getDuration(viewWidth, false));
		unAnim.addUpdateListener(getListener(last));
		return unAnim;
	}

	@Override
	protected ValueAnimator buildSelectedAnim(int position) {
		Integer viewWidth = getValue(position);
		ValueAnimator anim = ValueAnimator.ofInt(viewWidth, targetWidth);
		anim.setDuration(getDuration(viewWidth, true));
		anim.addUpdateListener(getListener(position));
		return anim;
	}

	private long getDuration(int width, boolean isSelected) {
		float offset = (float) (isSelected ? (targetWidth - width) : width) / (float) targetWidth;
		return (long) (duration * offset);
	}

	private ValueAnimator.AnimatorUpdateListener getListener(final int targetIndex) {
		return new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				TextView target = getView(targetIndex);
				int animatedValue = (int) animation.getAnimatedValue();
				setValue(targetIndex, animatedValue);
				if (target != null) {
					ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) target.getLayoutParams();
					params.width = animatedValue;
					target.setLayoutParams(params);
				}
			}
		};
	}
}
