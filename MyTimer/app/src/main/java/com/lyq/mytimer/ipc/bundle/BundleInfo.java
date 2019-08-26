package com.lyq.mytimer.ipc.bundle;

import android.os.Parcel;
import android.os.Parcelable;

public class BundleInfo implements Parcelable {

	private String name;
	private String hard;

	public BundleInfo(String name) {
		this.name = name;
	}

	protected BundleInfo(Parcel in) {
		name = in.readString();
		hard = in.readString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHard() {
		return hard;
	}

	public void setHard(String hard) {
		this.hard = hard;
	}

	public static final Creator<BundleInfo> CREATOR = new Creator<BundleInfo>() {
		@Override
		public BundleInfo createFromParcel(Parcel in) {
			return new BundleInfo(in);
		}

		@Override
		public BundleInfo[] newArray(int size) {
			return new BundleInfo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(hard);
	}
}
