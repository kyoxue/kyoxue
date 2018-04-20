package com.ilib.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
/**
 * 生成订单号
 * @author Kyouxe
 *
 */
public class NoCreate {

	public static  String getOrderNo(String pre){
	 String orderNo = "" ;        
	 String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
	 String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
	 orderNo = trandNo.toString().substring(0, 4);
	 orderNo = orderNo + sdf ;
	 if (null!=pre&&!"".equals(pre.trim())) {
		 return pre+orderNo;
	 }
	 return orderNo ;
	 }
	/**
	 * 生成16位唯一随机数
	 */
	public static String getSessionId(String sCharsetName)throws Exception{
		String sText = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong();
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		sCharsetName = (sCharsetName == null) || (sCharsetName.equals("")) ? "UTF-8" : sCharsetName;
		byte[] source = sText.getBytes(sCharsetName);

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(source);
		byte[] tmp = md.digest();

		char[] str = new char[16];
		int k = 0;
		for (int i = 0; i < 8; i++) {
			byte byte0 = tmp[i];
			str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
			str[(k++)] = hexDigits[(byte0 & 0xF)];
		}
		return new String(str).toLowerCase();
		//return new String(Hex.encodeHex(DigestUtils.md5(str)));
	}
	public static void main(String[] args)throws Exception {
		System.out.println(getSessionId("UTF-8"));
	}
 }