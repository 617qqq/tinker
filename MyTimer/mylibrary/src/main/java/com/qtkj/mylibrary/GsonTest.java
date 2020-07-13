package com.qtkj.mylibrary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.qtkj.mylibrary.gson.AmplifierEntity;
import com.qtkj.mylibrary.gson.BasePacketEntity;
import com.qtkj.mylibrary.gson.DevStatusEntity;
import com.qtkj.mylibrary.gson.InfoTest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GsonTest {

	public static void main(String[] args) {
//		String name = "{\"SN\":\"9132\",\"SysId\":\"2\",\"Att\":\"echo\",\"Dst\":\"ALL\",\"Src\":\"CP\",\"Cmd\":\"DevStatus\",\"msgCode\":\"RPT\",\"Data\":{\"command\":\"EQUIPMENT_AMP_MP3\",\"typeId\":\"60\",\"deviceId\":\"00c8\",\"seq\":\"00\",\"date\":\"2020-04-30 16:22:51\",\"status\":[{\"CurPlayer\":\"MP3\",\"Player\":\"MP3\",\"SongNum\":17,\"PlayPerm\":\"0\",\"Playtime\":0,\"PlayState\":\"播放\",\"PlayMode\":\"顺序播放\",\"Volume\":\"13\",\"Treble\":\"7\",\"scBass\":\"14\",\"PlayTotalTime\":0,\"SongName\":\"017-起风了 (Cover_ 买辣椒也用券).mp3\",\"PlayTotalSong\":51}],\"cmdId\":\"000100c86000\"}}";
//
//		BasePacketEntity<DevStatusEntity<AmplifierEntity>> entity = new Gson().fromJson(name, new TypeToken<BasePacketEntity<DevStatusEntity<AmplifierEntity>>>() {
//		}.getType());
//
//		System.out.println(entity.Data.status.get(0).SongName);

//		String name = "[{CurPlayer=MP3, Player=MP3, SongNum=17.0, PlayPerm=0, Playtime=0.0, PlayState=播放, PlayMode=顺序播放, Volume=10, Treble=7, scBass=14, PlayTotalTime=0.0, SongName=017-起风了 (Cover_ 买辣椒也用券).mp3, PlayTotalSong=51.0}]";
//		ArrayList<AmplifierEntity> list = new Gson().fromJson(name,
//				new TypeToken<ArrayList<AmplifierEntity>>() {
//				}.getType());
//		System.out.println(list.get(0).SongName);

		String name = "{\"info\":{\"20\":2,\"50\":4}}";
		InfoTest test = new Gson().fromJson(name, InfoTest.class);
//		JsonObject object = new JsonParser().parse(new Gson().toJson(test.info)).getAsJsonObject();
		JsonObject object = JsonParser.parseString(new Gson().toJson(test.info)).getAsJsonObject();
		System.out.println(object.get("20"));
	}


}
