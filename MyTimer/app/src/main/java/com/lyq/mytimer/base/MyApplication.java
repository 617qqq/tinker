package com.lyq.mytimer.base;

import android.app.Activity;
import android.app.Application;

import com.billy.android.swipe.SmartSwipeBack;
import com.lyq.mytimer.MainActivity;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		initDelay();
	}

	private void initDelay() {
		SmartSwipeBack.activityBezierBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
			@Override
			public boolean onFilter(Activity activity) {
				return !(activity instanceof MainActivity);
			}
		});
	}
}
