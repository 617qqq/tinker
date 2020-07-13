package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyq.mytimer.utils.IpScanner;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

public class WifiTestActivity extends AppCompatActivity {

	private TextView textView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		textView = new TextView(this);
		setContentView(textView);

		//testIpScanner();
		test();
	}

	private void test() {
		try (DatagramSocket dataSocket = new DatagramSocket()) {

			//准备要发送的数据
			byte[] byteCmd = new byte[1024];

			//给byteCmd设置一些约定的信息
			byteCmd[0] = '1';


			//接收消息的超时时间，以毫秒为单位
			dataSocket.setSoTimeout(30000);

			//UDP 创建要发送的数据包  用来将长度为 length 的包发送到指定主机上的指定端口。
			DatagramPacket senddataPacket = new DatagramPacket(
					byteCmd, byteCmd.length, InetAddress.getByName("255.255.255.255"), 5555);

			//设置为广播（此处需要设置）
			dataSocket.setBroadcast(true);

			//发送数据包
			dataSocket.send(senddataPacket);

			boolean isStopReceive = false;

			//下面是开启了接收消息
			byte[] receiveBytes = new byte[1024];
			//创建接受信息的包对象
			DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

			//开启一个死循环，不断接受数据
			while (! isStopReceive) {

				//接收数据，程序会阻塞到这一步，直到收到一个数据包为止
				dataSocket.receive(receivePacket);

				//解析收到的数据
				byte[] recArr = receivePacket.getData();

				setText(String.valueOf(recArr));

				//判断接收到的recArr是否是自己想要的....如果是 可以把isStopReceive改成true跳出循环

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setText(final String valueOf) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(String.format("%s\n%s", textView.getText().toString(), valueOf));
			}
		});
	}

	private void testIpScanner() {
		IpScanner ipScanner = new IpScanner(5555, new IpScanner.ScanCallback() {
			@Override
			public void onFound(final Set<String> ip, final String hostIp, final int port) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						StringBuilder sb = new StringBuilder(hostIp + " " + port + "\n");
						for (String s : ip) {
							sb.append(s).append("\n");
						}
						textView.setText(sb.toString());
					}
				});
			}

			@Override
			public void onNotFound(String hostIp, int port) {
				textView.setText(String.format("fail%s %d", hostIp, port));
			}
		}).setExpendThreadNumber(5).setTimeOut(10 * 1000);
		ipScanner.startScan();
	}
}
