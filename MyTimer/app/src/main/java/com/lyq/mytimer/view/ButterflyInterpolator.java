package com.lyq.mytimer.view;

import android.annotation.SuppressLint;
import android.view.animation.BaseInterpolator;

@SuppressLint ("NewApi")
public class ButterflyInterpolator extends BaseInterpolator {

	@Override
	public float getInterpolation(float input) {
		return (float) Math.tan(0.4f * input * Math.PI) / 3;
	}
}
