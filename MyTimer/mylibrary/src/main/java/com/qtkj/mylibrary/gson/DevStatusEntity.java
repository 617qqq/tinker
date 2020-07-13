package com.qtkj.mylibrary.gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 中央处理器上报给中控屏的状态信息
 */
public class DevStatusEntity<T> implements Serializable {

	public String command;

	public String deviceId;
	public String typeId;
	public String seq;

	public String value;
	public List<T> status;

	public String date;

	public String returnCode;
	public String dev_rev;


	public List<SensorType> sensorType = new ArrayList<>();

	public static class SensorType {

		public String subType;
		public String value;
		public String level;
		public String unit;
		public String unitTxt;

	}


	public String batLevel;
	public String batLow;
	public String roomId;

	public String emergency;
	public String message;
	public List<String> filesList = new ArrayList<>();

	public int UsrDataSN;

	public String updateStatu;
	public String describe;
	public String fileName;
	public String fileSize;


	public String cmdId;
	public String sourceId;
	public String destinationId;
	public List<Report> report;

	public static class Report {

		public String subType;
		public String value;
		public String level;
		public String unit;
	}

	/**
	 * 把萤石云报警信息提取到DevStatusEntity的message中统一使用
	 */
	public void setUpYsMessage() {
		//TODO 617 获取到报警信息，解析报警信息，保存到message并更新
	}
}
