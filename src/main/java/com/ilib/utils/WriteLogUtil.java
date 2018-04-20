package com.ilib.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteLogUtil {
	public static final Logger logger = LoggerFactory.getLogger(WriteLogUtil.class);

	private static String PATH = WriteLogUtil.class.getClassLoader().getResource("").getPath()
			.replaceAll("WEB-INF/classes/", "") + "download_log/";

	// 写文本日志
	public static void writeFileOperLog(final String msg) {
		String serverName = "log";
		String num = "info";
		File filePath = null;
		File logFile = null;
		try {
			filePath = new File(PATH);
			StringBuilder name = new StringBuilder();
			name.append(PATH).append(serverName).append('_');
			if (!StringUtil.get().isNull(num)) {
				name.append(num).append('_');
			}
			name.append(DateTimeUtils.getDate(new Date())).append(".txt");
			logFile = new File(name.toString());
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(logFile, true);
			fileWriter.write(msg);
			// fileWriter.write(DateTimeUtils.getDateTime(new Date()) + " - " +
			// msg);
			fileWriter.write(System.getProperty("line.separator"));
			fileWriter.flush();
		} catch (IOException e) {
			logger.error("写文件异常：", e);
		} finally {
			if (null != fileWriter) {
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}

	// 写文本日志
	public static void writeFileOperLog(final String msg, final String date) {
		String serverName = "log";
		String num = "info";
		File filePath = null;
		File logFile = null;
		try {
			filePath = new File(PATH);
			StringBuilder name = new StringBuilder();
			name.append(PATH).append(serverName).append('_');
			if (!StringUtil.get().isNull(num)) {
				name.append(num).append('_');
			}
			if (!StringUtil.get().isNull(date)) {
				name.append(DateTimeUtils.getDate(new Date())).append("_").append(date).append(".txt");
			} else {
				name.append(DateTimeUtils.getDate(new Date())).append(".txt");
			}
			// name.append(DateTimeUtils.getDate(new Date())).append(".txt");
			logFile = new File(name.toString());
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(logFile, true);
			// fileWriter.write(msg);
			fileWriter.write(DateTimeUtils.getDateTime(new Date()) + " - " + msg);
			fileWriter.write(System.getProperty("line.separator"));
			fileWriter.flush();
		} catch (IOException e) {
			logger.error("写文件异常：", e);
		} finally {
			if (null != fileWriter) {
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}
}