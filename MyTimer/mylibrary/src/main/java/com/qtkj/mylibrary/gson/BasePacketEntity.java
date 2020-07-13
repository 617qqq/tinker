package com.qtkj.mylibrary.gson;

public class BasePacketEntity<T> {

	/**
	 * 信息编号
	 * 用于中央处理器与中控通讯；假如同一时间紧接着发生两条读指令，回复的内容相同有可能会错乱，
	 * 加入的一个信息编号，以区分请求后返回的状态唯一
	 * 回应：的同时将发送方的SN原样返回
	 * 发送：序列号、截取时间戳末三位，毫秒
	 */
	public String SN;
	public String SysId;
	/**
	 * 包属性
	 * echo:需要发送目标回应ACK，以确认指令是否下发成功
	 * noEcho:不需回应
	 * ack:发送方的回应
	 */
	public String Att;
	/** 目标; CP:中央处理器  GW:网关  CC:中控屏  ALL:广播 */
	public String Dst;
	/**
	 * CC 下发面板开指令<br/>
	 * CP 中央处理器回应中控屏下发指令成功，上报是否向协议装换器(设备)下发指令成功，上报面板状态
	 * GW 网关
	 * ALL 广播
	 */
	public String Src;
	public String Cmd;
	/**
	 * RPT:设备主动上报状态<br/>
	 * REQ:中控屏或APP下发指令触发设备上报状态
	 */
	public String msgCode;
	public String IP;
	public T Data;

}
