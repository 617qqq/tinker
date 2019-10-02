package com.lyq.mytimer.info;

import android.content.Context;
import android.content.Intent;

public class ModuleInfo {

	private Class<?> clsActivity;
	private String titleName;
	private String desc;

	public ModuleInfo(Class<?> clsActivity, String titleName, String desc) {
		this.clsActivity = clsActivity;
		this.titleName = titleName;
		this.desc = desc;
	}

	public String getTitleName() {
		return titleName;
	}

	public String getDesc() {
		return desc;
	}

	public void start(Context context){
		Intent intent = new Intent(context, clsActivity);
		context.startActivity(intent);
	}
}
