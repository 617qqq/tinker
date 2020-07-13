package com.qtkj.mylibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileTest {

	public static void main(String[] args) {
		String dir = "/Users/liuyq/Downloads/百度云/底片";
		String targetPath = "/Users/liuyq/Downloads/百度云/temp";
		File file = new File(dir);
		int length = file.listFiles().length;
		int groupNum = length / 20 + 1;
		for (int i = 0; i < groupNum; i++) {
			File dirFile = new File(targetPath + File.separator + i);
			dirFile.mkdir();
			for (int j = 0; j < 20; j++) {
				int index = i * 20 + j;
				if (index >= length) {
					break;
				}
				File srcFile = file.listFiles()[index];
				File targetFile = new File(dirFile.getPath() + File.separator + srcFile.getName());
				try {
					targetFile.createNewFile();
					copyFileUsingFileChannels(srcFile, targetFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
}
