package com.ilib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
public class HttpUtil {
	public static String get_http_connect(String url, int connTimeout, int readTimeout, String recvEncoding) {
		String res = "";
		HttpURLConnection connection = null;
		InputStream in = null;
		BufferedReader br = null;
		try {
			URL urlObj = new URL(url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setConnectTimeout(1000 * connTimeout);// 超时时间
			connection.setReadTimeout(1000 * readTimeout);// 超时时间
			connection.connect();
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, recvEncoding));
			StringBuffer sb = new StringBuffer();
			int charCount = -1;
			while ((charCount = br.read()) != -1) {
				sb.append((char) charCount);
			}
			res = sb.toString();
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			} catch (Exception e2) {
			}
		}
		return res;
	}
	public static String post_http_connect(String url, String sendMsg, int connTimeout, int readTimeout,String recvEncoding) {
		String res = "";
		HttpURLConnection connection = null;
		InputStream in = null;
		BufferedReader br = null;
		OutputStreamWriter reqOut = null;
		try {
			URL urlObj = new URL(url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setConnectTimeout(1000 * connTimeout);// 超时时间
			connection.setReadTimeout(1000 * readTimeout);// 超时时间
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.connect();
			int charCount = -1;
			reqOut = new OutputStreamWriter(connection.getOutputStream());
			reqOut.write(sendMsg);
			reqOut.flush();
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, recvEncoding));
			StringBuffer sb = new StringBuffer();
			while ((charCount = br.read()) != -1) {
				sb.append((char) charCount);
			}
			res = sb.toString();
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (null != reqOut) {
					reqOut.close();
				}
				if (null != br) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			} catch (Exception e2) {
			}
		}
		return res;
	}


	public static String sendGet(String url, String param, String ecound, int times) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setReadTimeout(times * 1000);// 设置超时时间
			connection.setConnectTimeout(times * 1000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// // 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// //System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), ecound));
			String line;
			while ((line = in.readLine()) != null) {
				result += (line+"\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGet(String url, String ecound, int times) {

		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setReadTimeout(times * 1000);// 设置超时时间
			connection.setConnectTimeout(times * 1000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// // 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// //System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), ecound));
			String line;
			while ((line = in.readLine()) != null) {
				result += (line+"\r\n");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPost(String url, String param, String ecound, int times) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setReadTimeout(times * 1000);// 设置超时时间
			conn.setConnectTimeout(times * 1000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 建立实际的连接
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), ecound));
			String line;
			while ((line = in.readLine()) != null) {
				result += (line+"\r\n");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String executeGet(String url) throws Exception {
		GetMethod method = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			HttpClient client = new HttpClient();
			method = new GetMethod(url);
			client.executeMethod(method);
			is = method.getResponseBodyAsStream();
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null) {
				stringBuffer.append(str);
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			if (is != null) {
				is.close();
			}
			if (br != null) {
				br.close();
			}
		}
	}


	public static String sendZip(String url, String zipPath) {
		PostMethod method = null;
		InputStream stream = null;
		BufferedReader br = null;
		try {
			HttpClient client = new HttpClient();
			method = new PostMethod(url);
			method.setRequestEntity(new ByteArrayRequestEntity(
					process(new BufferedInputStream(new FileInputStream(zipPath))), "multipart/form-data"));
			client.executeMethod(method);

			stream = method.getResponseBodyAsStream();

			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				buf.append(line);
			}

			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != method) {
				method.releaseConnection();
			}
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		return "";

	}

	public static byte[] process(InputStream is) {
		byte[] a = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf, 0, buf.length)) != -1) {
				os.write(buf, 0, len);
			}
			a = os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return a;
	}

	/**
	 * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
	 * 
	 * @param url 请求地址 form表单url地址
	 * @param filePath 文件在服务器保存路径
	 * @return String url的响应信息返回值
	 * @throws IOException
	 */
	public static String sendFile(String url, String filePath) throws IOException {
		String result = null;
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}
		/**
		 * 第一部分
		 */
		URL urlObj = new URL(url);
		// 连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		/**
		 * 设置关键值
		 */
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存
		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		// 请求正文信息
		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"uploadfile\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");
		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);
		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return result;
	}

}
