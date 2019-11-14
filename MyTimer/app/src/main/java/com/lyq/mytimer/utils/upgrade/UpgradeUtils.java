package com.lyq.mytimer.utils.upgrade;

import android.content.Context;

import com.lyq.mytimer.BuildConfig;
import com.tencent.bugly.beta.Beta;

public class UpgradeUtils {

	public static void init(Context context) {
		//自动检查更新开关
		Beta.autoCheckUpgrade = false;
		Beta.upgradeDialogLayoutId = 0;
		Beta.init(context, BuildConfig.DEBUG);
	}
}
