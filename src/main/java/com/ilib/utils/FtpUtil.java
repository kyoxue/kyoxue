package com.ilib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpUtil {

	private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	private FTPClient ftpClient;
	private String ip = "219.134.185.8"; // 服务器IP地址
	private String userName = "fd_public"; // 用户名
	private String userPwd = "fd12345c"; // 密码
	private int port = 21; // 端口号
	private String path = "/"; // 读取文件的存放目录

	public FtpUtil() {
		init();
		connectServer(ip, port, userName, userPwd, path);
	}

	/**
	 * 指定FTP服务器地址
	 * 
	 * @param ip IP地址
	 * @param port 端口号
	 * @param userName 用户名
	 * @param userPwd 密码
	 * @param path FTP文件夹路径 如：/resource/img/
	 */
	public FtpUtil(String ip, int port, String userName, String userPwd, String path) {
		this.ip = ip;
		this.userName = userName;
		this.userPwd = userPwd;
		this.port = port;
		this.path = path;
		connectServer(ip, port, userName, userPwd, path);
	}

	/**
	 * init ftp server parameters
	 */
	void init() {
		ResourceBundle bundle = ResourceBundle.getBundle("ftp", Locale.getDefault());
		this.ip = bundle.getString("ftp_ip");
		this.userName = bundle.getString("ftp_username");
		this.userPwd = bundle.getString("ftp_password");
		this.port = Integer.parseInt(bundle.getString("ftp_port"));
		this.path = bundle.getString("ftp_path");
	}

	/**
	 * 连接服务器
	 * 
	 * @param ip ip地址
	 * @param port 端口号
	 * @param userName 登录名
	 * @param userPwd 登陆密码
	 * @param path FTP文件夹目录 如：/soruces/img/
	 */
	public void connectServer(String ip, int port, String userName, String userPwd, String path) {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ip, port); // 连接
			ftpClient.enterLocalPassiveMode(); // 设置为被动模式
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置为二进制传输
			ftpClient.login(userName, userPwd);
			if (path != null && path.length() > 0) {
				ftpClient.changeWorkingDirectory(path); // 跳转到FTP指定目录
			}
		} catch (SocketException e) {
			logger.error("连接异常：{}", e.getMessage());
		} catch (IOException e) {
			logger.error("IO异常：{}", e.getMessage());
		}
	}

	/**
	 * 关闭连接
	 */
	public void closeServer() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error("关闭连接异常：{}", e.getMessage());
			}
		}
	}

	/**
	 * 获取FTP指定目录下所有的文件名
	 * 
	 * @return 目录下所有的文件名
	 */
	public List<String> getFileList() {
		List<String> fileNames = new ArrayList<String>();
		FTPFile[] ftpFiles = null; // 获得指定目录下所有文件名
		try {
			ftpFiles = ftpClient.listFiles(path); // path为读取文件的路径，非FTP全路径
			for (FTPFile f : ftpFiles) {
				if (f.isFile()) {
					fileNames.add(f.getName());
				}
			}
		} catch (IOException e) {
			logger.error("获取文件列表异常：", e.getMessage());
		}
		return fileNames;
	}

	/**
	 * 直接读取FTP文件内容（不下载）
	 * 
	 * @param fileName 文件名
	 * @return 文件内容
	 */
	public String readFile(String fileName) {
		// logger.info("获取文件内容，文件名：{}", fileName);
		InputStream ins = null;
		StringBuilder builder = null;
		BufferedReader reader = null;
		try {
			// 从服务器上读取指定的文件
			ins = ftpClient.retrieveFileStream(fileName);
			if (null == ins) {
				// logger.warn("该文件名:{}不存在", fileName);
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(ins, "GBK"));
			String line;
			builder = new StringBuilder(150);
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			// 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
			ftpClient.getReply();
		} catch (IOException e) {
			e.printStackTrace();
			// logger.error("读取文件异常：{}", e);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(ins);
		}
		if ("".equals(builder.toString())) {
			return null;
		}
		return builder.toString();
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName 文件名
	 */
	public boolean deleteFile(String fileName) {
		logger.info("删除文件，文件名：", fileName);
		try {
			return ftpClient.deleteFile(fileName);
		} catch (IOException e) {
			logger.error("删除文件异常：", e.getMessage());
			return false;
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteFile 下载文件名
	 * @param localFile 本地存放路径
	 */
	public String download(String remoteFile, String localFile) {
		InputStream is = null;
		FileOutputStream os = null;
		String filePath = null;
		try {
			is = ftpClient.retrieveFileStream(remoteFile);
			if (is == null) {
				logger.info("无法读取文件下载文件");
				return filePath;
			}
			File file = new File(localFile + remoteFile);
			os = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
			ftpClient.completePendingCommand();// 多文件下载需要调用这个方法
			filePath = localFile + remoteFile;
			return filePath;
		} catch (IOException ex) {
			//System.out.println("download exception : " + ex.getMessage());
			return filePath;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (os != null) {
						os.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public String getPath() {
		return path;
	}

}
