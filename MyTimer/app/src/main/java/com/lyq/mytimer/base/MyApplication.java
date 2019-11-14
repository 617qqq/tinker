package com.lyq.mytimer.base;

import android.app.Activity;
import android.app.Application;

import com.billy.android.swipe.SmartSwipeBack;
import com.lyq.mytimer.MainActivity;
import com.lyq.mytimer.utils.upgrade.UpgradeUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		initDelay();
	}

	private void initDelay() {
		Bugly.init(this, "e8ad3789e6", false);
		UpgradeUtils.init(this);

		SmartSwipeBack.activitySlidingBack(this, new SmartSwipeBack.ActivitySwipeBackFilter() {
			@Override
			public boolean onFilter(Activity activity) {
				return ! (activity instanceof MainActivity);
			}
		});
	}
}
