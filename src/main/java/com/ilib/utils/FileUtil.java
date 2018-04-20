package com.ilib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String readAllString(String filePath) {
		FileInputStream fis = null;
		String txtString = null;
		try {
			// 创建流对象
			fis = new FileInputStream(filePath);
			// 读取数据，并将读取到的数据存储到数组中
			byte[] data = new byte[1024]; // 数据存储的数组
			int i = 0; // 当前下标
			// 读取流中的第一个字节数据
			int n = fis.read();
			// 依次读取后续的数据
			while (n != -1) { // 未到达流的末尾
				// 将有效数据存储到数组中
				data[i] = (byte) n;
				// 下标增加
				i++;
				// 读取下一个字节的数据
				n = fis.read();
			}
			// 解析数据
			txtString = new String(data, 0, i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流，释放资源
				fis.close();
			} catch (Exception e) {
			}
		}
		return txtString;
	}

	/**
	 * @describe 新建目录
	 * @auto zwliao
	 * @date 2013-12-19 下午3:42:55
	 * @param folderPath
	 */
	public static void creatFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			logger.info("文件创建失败！");
		}
	}

	/**
	 * @describe 指定文件夹路径如果不存在就创建一个新的
	 * @auto zwliao
	 * @date 2014-8-5 上午10:23:03
	 * @return
	 */
	public static void isExistFilePath(String folderPath) {

		File file = new File(folderPath);

		if (!file.exists()) {
			creatFolder(folderPath);
		}
	}

	/**
	 * @describe 新建文件
	 * @auto zwliao
	 * @date 2013-12-19 下午3:42:48
	 * @param filePath
	 * @param fileContext
	 */
	public static void creatFile(String filePath, String fileContext) {
		try {
			if (filePath != null && !"".equals(filePath)) {
				String folder = filePath.substring(0, filePath.lastIndexOf("/"));
				creatFolder(folder);
			}
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (fileContext != null && !"".equals(fileContext)) {
				FileWriter fileWriter = new FileWriter(file);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.println(fileContext);
				fileWriter.close();
				printWriter.close();
			}
		} catch (Exception e) {
			logger.info("文件创建失败！");
		}
	}

	/**
	 * @describe 删除文件
	 * @auto zwliao
	 * @date 2013-12-19 下午3:42:38
	 * @param filePath
	 * @return
	 */
	public static Boolean deleteFile(String filePath) {
		File file = new File(filePath);
		Boolean flag = false;
		if (file.isFile()) {
			flag = file.delete();
		}
		return flag;
	}

	/**
	 * @describe 根据文件夹下面文件的总数大小来决定是否要删除所有文件
	 * @auto zwliao
	 * @date 2013-12-19 下午3:47:57
	 * @param filePath
	 * @param fileLength
	 * @return
	 */
	public static boolean deleteByFileLength(String filePath, int fileLength) {

		File file = new File(filePath);
		if (!file.isDirectory() || !file.exists()) {
			return false;
		}
		File[] fileList = file.listFiles();

		// 如果文件的总数小于指定的fileLength，则不做删除
		if (fileList.length < fileLength) {
			return false;
		}

		for (int i = 0; i < fileList.length; i++) {
			if (!fileList[i].isDirectory()) {
				File fileSon = fileList[i];
				if (fileSon.isFile()) {
					deleteFile(fileSon.toString());
				}
			}
		}
		return true;
	}

	/**
	 * @describe 删除空文件夹
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:09
	 * @param folderPath
	 * @return
	 */
	public static Boolean deleteNullFolder(String folderPath) {
		File file = new File(folderPath);
		if (!file.isDirectory() || !file.exists()) {
			//System.out.println("路径不存在或者路径不是目录！");
			return false;
		}
		File[] fileList = file.listFiles();
		if (fileList.length > 0) {
			return false;
		}
		file.delete();
		return true;
	}

	/**
	 * @describe 删除目录下所有目录
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:19
	 * @param folderPath
	 * @return
	 */
	public static Boolean deleteAllFolder(String folderPath) {
		File file = new File(folderPath);
		if (!file.isDirectory() || !file.exists()) {
			return false;
		}
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				File fileSon = fileList[i];
				// 遍历判断是否为空目录，不是空目录删除目录下文件或目录
				if (!deleteNullFolder(fileSon.toString())) {
					deleteAllFolder(fileSon.toString());
					deleteAllfiles(fileSon.toString());
					fileSon.delete();
				} else {
					fileSon.delete();
				}
			}
		}
		return true;
	}

	/**
	 * @describe 删除目录下所有文件
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:29
	 * @param filePath
	 * @return
	 */
	public static Boolean deleteAllfiles(String filePath) {
		File file = new File(filePath);
		if (!file.isDirectory() || !file.exists()) {
			return false;
		}
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (!fileList[i].isDirectory()) {
				File fileSon = fileList[i];
				if (fileSon.isFile()) {
					deleteFile(fileSon.toString());
				}
			}
		}
		return true;
	}

	/**
	 * @describe 删除目录下所有文件和目录
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:38
	 * @param filePath
	 */
	public static void deleteAll(String filePath) {
		deleteAllfiles(filePath);
		deleteAllFolder(filePath);
	}

	/**
	 * @describe 复制目录下单个文件
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:47
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static Boolean copyFile(String oldPath, String newPath) {
		try {
			File file = new File(oldPath);
			if (file.isDirectory()) {
				return false;
			}
			if (file.exists()) {
				FileInputStream inputStream = new FileInputStream(file);
				FileOutputStream outputStream = new FileOutputStream(newPath);
				BufferedInputStream reader = new BufferedInputStream(inputStream);
				int len = inputStream.available();
				byte[] buffer = new byte[len];
				reader.read(buffer, 0, len);
				outputStream.write(buffer);
				inputStream.close();
				outputStream.close();
				reader.close();
			}
		} catch (Exception e) {
			logger.info("文件复制失败！");
		}
		return true;
	}

	/**
	 * @describe 复制目录下所有文件和目录
	 * @auto zwliao
	 * @date 2013-12-19 下午3:43:57
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Boolean copyAllFile(String oldPath, String newPath) {
		File file = new File(oldPath);
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			copyFile(oldPath, newPath);
			return false;
		}
		File[] fileList = file.listFiles();
		File mkdir = new File(newPath);
		if (!mkdir.exists()) {
			mkdir.mkdirs();
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				copyAllFile(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator
						+ fileList[i].getName());
			} else {
				copyFile(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator
						+ fileList[i].getName());
			}
		}
		return true;
	}

	/**
	 * @describe 把字符串写到txt文件中，flag为true可以不覆盖原有TXT文件内容 续写
	 * @auto ouyuexing
	 * @date 2014-08-04 下午3:43:57
	 * @param filePath 文件路径
	 * @param content 写入的字符串
	 * @param flag 是否为续写
	 * @return
	 */
	public static void writerText(String filePath, String content, Boolean flag) {
		try {
			if (filePath != null && !"".equals(filePath)) {
				String folder = filePath.substring(0, filePath.lastIndexOf("/"));
				creatFolder(folder);
			}
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			// new FileWriter(path + "t.txt", true) 这里加入true 可以不覆盖原有TXT文件内容 续写
			FileWriter filewrite = new FileWriter(file, flag);
			BufferedWriter bw1 = new BufferedWriter(filewrite);
			bw1.write(content);
			bw1.flush();
			bw1.close();
			filewrite.close();
		} catch (Exception e) {
			logger.info("写入字符串失败！");
		}
	}

	/**
	 * 文件上传
	 * @param file
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean uploadFile(MultipartFile file, String path, String fileName)
	{
		try{
			if (!StringUtil.get().isNull(file.getOriginalFilename()))
			{
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				file.transferTo(targetFile);
				return true;
			}
		}catch(Exception e){
			logger.error("文件上传失败！");
		}
		return false;
	}
	public static void deleteAll(File file){  
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i]);
				files[i].delete();
			}
		}
	}
	public static void deleteAll(File file,boolean delroot){  
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i]);
				files[i].delete();
			}
			if (delroot) {
				if (file.exists()) // 如果文件本身就是目录 ，就要删除目录
				file.delete();
			}
		}
	}
	public static void main(String[] args) {
	}
}