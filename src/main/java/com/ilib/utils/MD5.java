package com.ilib.utils;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5 {
	private static Log log = LogFactory.getLog(MD5.class);

	public static String encode(String sText, String sCharsetName) throws Exception {
		String s = null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		sCharsetName = (sCharsetName == null) || (sCharsetName.equals("")) ? "UTF-8" : sCharsetName;
		byte[] source = sText.getBytes(sCharsetName);

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(source);
		byte[] tmp = md.digest();

		char[] str = new char[32];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
			str[(k++)] = hexDigits[(byte0 & 0xF)];
		}

		s = new String(str);
		log.info("MD5原串=" + sText + ",加密串=" + s);

		return s.toLowerCase();
	}
}
