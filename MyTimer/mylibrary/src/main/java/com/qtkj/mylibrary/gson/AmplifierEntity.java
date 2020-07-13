package com.qtkj.mylibrary.gson;

import androidx.annotation.Nullable;

public class AmplifierEntity {

	/** 当前音源:  MP3，收音机，AUX，关闭等（当前系统状态） */
	public String CurPlayer;
	public String Player;
	public int SongNum;
	/**
	 * 播放进度： 
	 * MP3     单位% (如：50à50%)
	 * AUX等   无此数据
	 * 收音机(高16位中的低8位)  
	 * 3字节频率 单位KHz 如87.00MHz 则表示为 87000
	 *          522Khz表示为 522
	 */
	public String PlayPerm;
	/**
	 * 播放时间 
	 * MP3     (单位：秒)
	 * AUX等   无此数据
	 * 收音机   (低16位 小端 )
	 * 3字节频率 单位KHz 如87.00MHz 则表示为 87000
	 *          522Khz表示为 522
	 */
	public int Playtime;
	/**
	 * 播放状态： 
	 *  MP3：   播放/停止等
	 *  收音机： FM，AM
	 *  AUX、关闭：无此数据     
	 */
	public String PlayState;
	public String PlayMode;
	@Nullable
	public String Volume;
	public String Treble;
	public String scBass;
	/** 播放总时间 */
	public int PlayTotalTime;
	/** 总曲目 */
	public int PlayTotalSong;
	public String SongName;
}
