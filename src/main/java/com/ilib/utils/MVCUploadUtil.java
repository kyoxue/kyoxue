package com.ilib.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.web.multipart.MultipartFile;

public class MVCUploadUtil {

	private MVCUploadUtil() {
	}
	private static class Inner{
		final static MVCUploadUtil util = new MVCUploadUtil();
	}
	public static MVCUploadUtil get(){
		return Inner.util;
	}
	/**
	 * upload file
	 * controller接受方式：@RequestParam(value = "file", required = false) MultipartFile file
	 * jsp表单域注意实现：
	 * form加enctype="multipart/form-data" 
	 * file控件：<input type="file" id="file" name="file"/>
	 * @param file  
	 * @param uploadDir 上传的目录名字
	 * @param maxSize 最大M数
	 * @param context servlet上下文环境
	 * @return
	 * @throws Exception
	 */
	public String upload(MultipartFile file,String uploadDir,Double maxSize,ServletContext context)throws Exception{
		if (null == file || file.getSize() == 0) {
			throw new Exception("upload file is empty..");
		}
		if (null == uploadDir || "".equals(uploadDir.trim())) {
			uploadDir = "tmp_";
		}
		if (maxSize == null || maxSize.doubleValue()==0) {
			maxSize = new Double(10);//默认10M
		}
		File targetFile = null;
		double size = (double) file.getSize();
		double fileSize = size / 1024 / 1024;
		if (fileSize > maxSize){
			throw new Exception("out of range size:".concat(maxSize.toString()));
		}
		String fileName = file.getOriginalFilename();// 上传文件名
		String path = "";
		try {
			path = context.getRealPath("/".concat(uploadDir).concat("/"));// 上传文件目录
		} catch (Exception e) {
			throw new Exception(e);
		}
		if (null == path || path.trim().equals("") || path.trim().equals("null")) {
			throw new Exception("upload path is null or has error...");
		}
		targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);// 将文件上传到服务器上
		} catch (IllegalStateException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}
		return context.getContextPath().concat("/").concat(uploadDir).concat("/").concat(fileName);
		//return targetFile.getPath();
	}
}
