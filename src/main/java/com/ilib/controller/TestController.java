package com.ilib.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.URI;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.ilib.model.TestBeanA;
import com.ilib.model.TestBeanC;
import com.ilib.service.redis.RedisCacheService;
import com.ilib.utils.DownloadUtil;
import com.ilib.utils.JsonUtil;
import com.ilib.utils.MVCUploadUtil;

@Controller
public class TestController {
	Logger log = LoggerFactory.getLogger(TestController.class);
	@RequestMapping(value = "/test", method = {RequestMethod.GET})
	public String test(HttpServletRequest request) {
		return "test/test";
	}
	@RequestMapping(value = "/test2", method = {RequestMethod.GET})
	public String test2(HttpServletRequest request) {
		return "test/test2";
	}
	@RequestMapping(value = "/test3", method = {RequestMethod.GET})
	public String test3(HttpServletRequest request) {
		return "test/test3";
	}
	@RequestMapping(value = "/test4", method = {RequestMethod.GET})
	public String test4(HttpServletRequest request) {
		return "test/test4";
	}
	@RequestMapping(value="/convert",method={RequestMethod.POST},produces={"application/json;charset=UTF-8"},consumes={"application/json;charset=UTF-8"})
	@ResponseBody
	public String convert(@RequestBody TestBeanC test,HttpServletResponse res){
		if (null == test) {
			return "";
		}
		return JsonUtil.fmtObj2JsonStr(test);
	}
	@RequestMapping(value = "/getData",method = { RequestMethod.GET, RequestMethod.POST })
	public void getDatas(HttpServletRequest req,HttpServletResponse response){
		// 返回头部设置
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-type", "application/x-javascript;charset=utf-8");
		response.setDateHeader("Expires", 0);
		PrintWriter out = null;
		String str ="";
		try {
			out = response.getWriter();
			String function = req.getParameter("callback");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", "薛正辉");
			result.put("age", 30);
			result.put("birth", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			str = function + "(" + JSONObject.toJSONString(result) + ")";
		}catch (Exception e) {
		}finally{
			if (null != out) {
				out.print(str);
				out.flush();
				out.close();
			}
		}
	}
	private static final Long REDIS_EXPIRED_MIN = new Long(30);
	@Autowired
	private RedisCacheService<List> redisCache;
	@RequestMapping(value = "/testRedis",method = { RequestMethod.GET, RequestMethod.POST })
	public void testRedis(HttpServletRequest req,HttpServletResponse response){
		// 返回头部设置
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-type", "application/x-javascript;charset=utf-8");
		response.setDateHeader("Expires", 0);
		PrintWriter out = null;
		String str ="";
		try {
			out = response.getWriter();
			List list = null;
			String redisKey = "kyoxue20171111";
			Object readisValue = redisCache.getCacheList(redisKey);
			if (readisValue != null&&readisValue instanceof List && ((List) readisValue).size() > 0){
				log.info("缓存存在!");
				list = (List) readisValue;
			}else {
				log.info("缓存不存在!");
				list = new ArrayList();
				TestBeanA c = new TestBeanA();
				c.setName("222");
				list.add(c);
				redisCache.setCacheList(redisKey, list, REDIS_EXPIRED_MIN);
			}
			if (null!=list && list.size()>0) {
				str = list.size()+"";
			}
		}catch (Exception e) {
		}finally{
			if (null != out) {
				out.print(str);
				out.flush();
				out.close();
			}
		}
	}
	@RequestMapping(value = "/uploadMultipartTest",method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadMultipart(HttpServletRequest request,@RequestParam(value = "file", required = false) MultipartFile[] files){
		String uploadDir = "download";
		final Double maxSize = new Double(10.0d);
		final ServletContext context = request.getSession().getServletContext();
		if (null!=files && files.length >0) {
			Map<String, String> result = new HashMap<>();
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					try {
						String uploadPath = MVCUploadUtil.get().upload(file, uploadDir, maxSize, context);
						result.put(file.getOriginalFilename(),uploadPath);
					} catch (Exception e) {
						log.error("上传失败",e);
						request.setAttribute("error", "上传失败！"+e.getMessage());
						return "test/test";
					}
				}
			}
			request.setAttribute("resultMap", result);
		}
		return  "test/test";
	}
	@RequestMapping(value="/uploadtest")
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest req
			){
		String uploadDir = "download";
		final Double maxSize = new Double(10.0d);
		final ServletContext context = req.getSession().getServletContext();
		String uploadPath = null;
		try {
			uploadPath = MVCUploadUtil.get().upload(file, uploadDir, maxSize, context);
		} catch (Exception e) {
			log.error("上传失败",e);
			req.setAttribute("error", "上传失败！"+e.getMessage());
			return "test/test";
		}
		if (null ==uploadPath) {
			req.setAttribute("error", "上传失败！未获取到上传文件目录！");
			return "test/test";
		}
		req.setAttribute("fileName", file.getOriginalFilename());
		req.setAttribute("filePath", uploadPath);
		return  "test/test";
	}
	/**
	 * SPRING MVC框架下载 同寻找上传的目录+传递的文件名，这里可以把上传的文件目录作为前台参数传进来，这样上传新建目录可以动态。
	 * @param downFile
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value ="/downloadWithMVC",method = { RequestMethod.GET, RequestMethod.POST },consumes="application/json;charset=UTF-8")  
    public ResponseEntity<byte[]> download(@RequestBody DownFile downFile,HttpServletRequest request) throws IOException { 
		String name = downFile.getName();
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(name,"UTF-8")); 
        String uploadDir = "download";
        String filenpath = request.getSession().getServletContext().getRealPath("/"+uploadDir+"/")+"/"+name;
        File file = new File(filenpath);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);  
    }
    /**
     * HTTP下载
     * @param downFile
     * @param response
     * @param request
     */
	@RequestMapping(value = "/download",method = { RequestMethod.GET, RequestMethod.POST },consumes="application/json;charset=UTF-8")
	public void download(@RequestBody DownFile downFile,HttpServletResponse response,HttpServletRequest request){
		String path = downFile.getPath();
		String name = downFile.getName();
		path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		DownloadUtil.download(path, response,name);
	}
}
class DownFile implements Serializable{
	private String path;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
class ProcessInfo{    
    public long totalSize = 1;    
    public long readSize = 0;    
    public String show = "";    
    public int itemNum = 0;    
    public int rate = 0;    
}    