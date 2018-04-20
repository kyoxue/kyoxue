package com.ilib.utils;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.Project;

import org.apache.tools.ant.taskdefs.Expand;

import org.apache.tools.zip.ZipEntry;
/**
 * 压缩解压ZIP<br>可以取中文名压缩Activiti bpmn文件来使用<br>用rar软件压缩的引入activiti会有问题。
 * @author Kyoxue
 */
public class ZipUtil {

	private static final String ENCODE = "UTF-8";
	public static void main(String[] args) {
		zip("C:\\Users\\Administrator\\Desktop\\测试完毕线程池CODE\\QunarThread.txt", "C:\\Users\\Administrator\\Desktop\\测试完毕线程池CODE\\QunarThread.zip");
	}
	public static void zip(String inputFilePath, String zipFileName) {

		File inputFile = new File(inputFilePath);

		if (!inputFile.exists())

			throw new RuntimeException("原始文件不存在!!!");

		File basetarZipFile = new File(zipFileName).getParentFile();

		if (!basetarZipFile.exists() && !basetarZipFile.mkdirs())

			throw new RuntimeException("目标文件无法创建!!!");

		String ignore = zipFileName.substring(zipFileName.lastIndexOf(File.separator)+1);
		BufferedOutputStream bos = null;

		FileOutputStream out = null;

		ZipOutputStream zOut = null;

		try {

			// 中文支持取文件名

			out = new FileOutputStream(new String(zipFileName.getBytes(ENCODE)));

			bos = new BufferedOutputStream(out);

			zOut = new ZipOutputStream(bos);

			zip(zOut, inputFile, inputFile.getName(),ignore);

			zOut.flush();
			zOut.close();
			bos.close();
			out.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static void zip(ZipOutputStream zOut, File file, String base,String ignore) {

		try {

			if (file.isDirectory()) {

				File[] listFiles = file.listFiles();

				if (listFiles != null && listFiles.length > 0)

					for (File f : listFiles)

					zip(zOut, f, f.getName(),ignore);

			}else {

				if (base == "") {

					base = file.getName();

				}
				if (!base.equals(ignore)) {
					zOut.putNextEntry(new ZipEntry(base));

					writeFile(zOut, file);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static void writeFile(ZipOutputStream zOut, File file)

			throws IOException {

		FileInputStream in = null;

		BufferedInputStream bis = null;

		in = new FileInputStream(file);

		bis = new BufferedInputStream(in);

		int len = 0;

		byte[] buff = new byte[2048];

		while ((len = bis.read(buff)) != -1)

			zOut.write(buff, 0, len);

		zOut.flush();

		bis.close();
		in.close();

	}


	public static void unZip(String zipPath, String destinationPath) {

		File zipFile = new File(zipPath);

		if (!zipFile.exists())

			throw new RuntimeException("zip file " + zipPath

					+ " 压缩文件不存在.");

		Project proj = new Project();

		Expand expand = new Expand();

		expand.setProject(proj);

		expand.setTaskType("unzip");

		expand.setTaskName("unzip");

		expand.setSrc(zipFile);

		expand.setDest(new File(destinationPath));

		expand.setEncoding(ENCODE);

		expand.execute();

		System.out.println("解压完成!!!");

	}
}
