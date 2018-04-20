package com.ilib.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class SecurityUtil {

	public static String SYSTEM_OLD_CHARSET = "UTF-8";
	public static String urlEncodeECB(String key,String data)throws Exception{
		data = URLEncoder.encode(data, SYSTEM_OLD_CHARSET);
		data = des3EncodeECB(key, data);
		return data;
	}
	public static String urlDecodeECB(String key,String data)throws Exception{
		data = des3DecodeECB(key, data);
		data = URLDecoder.decode(data, SYSTEM_OLD_CHARSET);
		return data;
	}
	/**
	 * ECB加密,不要IV
	 * 
	 * @param key 密钥
	 * @param message 明文
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static String des3EncodeECB(String key, String message) throws Exception {

		Key deskey = null;
		// byte[] keyBytes=new BASE64Decoder().decodeBuffer(key);
		byte[] keyBytes = key.getBytes(SYSTEM_OLD_CHARSET);
		byte[] data = message.getBytes(SYSTEM_OLD_CHARSET);

		DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return new BASE64Encoder().encode(bOut);
	}

	/**
	 * ECB解密,不要IV
	 * 
	 * @param key 密钥
	 * @param message Base64编码的密文
	 * @return 明文
	 * @throws Exception
	 */
	public static String des3DecodeECB(String key, String message) throws Exception {
		Key deskey = null;
		// byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
		byte[] keyBytes = key.getBytes(SYSTEM_OLD_CHARSET);
		byte[] data = new BASE64Decoder().decodeBuffer(message);
		// byte[] data =
		// message.getBytes(InitContextConstants.SYSTEM_OLD_CHARSET);

		DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return new String(bOut, SYSTEM_OLD_CHARSET);
	}

	/**
	 * CBC加密
	 * 
	 * @param key 密钥
	 * @param data 明文
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public byte[] des3EncodeCBC(byte[] key, byte[] data) throws Exception {

		Key deskey = null;
		byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };

		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

	/**
	 * CBC解密
	 * 
	 * @param key 密钥
	 * @param data Base64编码的密文
	 * @return 明文
	 * @throws Exception
	 */
	public byte[] des3DecodeCBC(byte[] key, byte[] data) throws Exception {

		Key deskey = null;
		byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

}
