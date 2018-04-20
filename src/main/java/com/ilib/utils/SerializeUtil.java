package com.ilib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @describe序列化工具类
 * @author kyoxue
 * @datetime 2017年8月28日 下午3:47:44
 **/
public class SerializeUtil {
	/**
	 * 正序列成字节数组
	 */
	public static byte[] serialize(Object obj)throws IOException{
		ObjectOutputStream obi = null;
		ByteArrayOutputStream bai = null;
		try {
			bai = new ByteArrayOutputStream();
			obi = new ObjectOutputStream(bai);
			obi.writeObject(obj);
			byte[] byt = bai.toByteArray();
			return byt;
		} catch (IOException e) {
			throw e;
		}finally{
			if (null!=bai) {
				try {
					bai.close();
				} catch (IOException e) {
					bai=null;
				}
			}
			if (null!=obi) {
				try {
					obi.flush();
					obi.close();
				} catch (IOException e) {
					obi =null;
				}
			}
		}
	}
	/**
	 * 反序列成对象
	 */
	public static Object unserizlize(byte[] byt) throws IOException,ClassNotFoundException{
		ObjectInputStream oii = null;
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(byt);
			oii = new ObjectInputStream(bis);
			Object obj = oii.readObject();
			return obj;
		} catch (IOException e) {
			throw e;
		}catch(ClassNotFoundException ce){
			throw ce;
		}finally{
			if (null!=bis) {
				try {
					bis.close();
				} catch (IOException e) {
					bis=null;
				}
			}
			if (null!=oii) {
				try {
					oii.close();
				} catch (IOException e) {
					oii=null;
				}
			}
		}
	}
}
