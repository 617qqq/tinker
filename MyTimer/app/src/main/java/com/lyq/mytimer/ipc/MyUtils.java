package com.lyq.mytimer.ipc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyUtils {

	public static void close(OutputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
