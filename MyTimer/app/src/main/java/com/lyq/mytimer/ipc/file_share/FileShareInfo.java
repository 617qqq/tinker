package com.lyq.mytimer.ipc.file_share;

import java.io.Serializable;

public class FileShareInfo implements Serializable {

	private String name;

	public FileShareInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
