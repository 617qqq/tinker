package com.lyq.mytimer.ipc;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class IPCConstants {

	private static final String TAG = "IPCConstants";

	public static String CACHE_DIR_PATH;
	public static String CACHE_FILE_PATH;

	/**___________________Messenger___________________*/
	public static final int MESSAGE_GET_LIST = 1;
	public static final int MESSAGE_ADD = 2;
	/**___________________Messenger___________________*/

	public static void initSelf(Context context) {
		if (TextUtils.isEmpty(CACHE_DIR_PATH)) {
			init(context);
		}
	}

	public static void init(Context context) {
		CACHE_DIR_PATH = context.getExternalCacheDir() + File.separator + "IPCData";
		CACHE_FILE_PATH = CACHE_DIR_PATH + File.separator + "ipcData";
		Log.e(TAG, "init: " + context.getApplicationInfo().processName);
	}
}
