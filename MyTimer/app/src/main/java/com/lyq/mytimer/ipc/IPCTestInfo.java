package com.lyq.mytimer.ipc;

import java.io.Serializable;

public class IPCTestInfo implements Serializable {

	private String name;

	public IPCTestInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
