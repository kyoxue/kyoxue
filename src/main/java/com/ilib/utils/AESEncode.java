package com.ilib.utils;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * 本程序生成一个AES\DES密钥，并且转换它为RAW字节，
 * 然后根据这个密钥重新创建一个AES\DES密钥，
 * 这个新构建的密钥用于初始化一个加密解密的AES\DES密码
 * 加密后 会生成提取加密文本的KEY 通过KEY解密获取内容
 * @author kyoxue
 * @version 1.0
 */

public class AESEncode {
	public enum Encode{
		/**
		 * 提取KEY
		 */
		KEY,
		/**
		 * 被加密的内容
		 */
		VALUE;
	}
	/** 
    * 将二进制转换成16进制 
    * 
    * @param buf 
    * @return 
    */ 
    private static String parseByte2HexStr(byte buf[]) { 
    	StringBuffer sb = new StringBuffer(); 
    	for (int i = 0; i < buf.length; i++) { 
    		String hex = Integer.toHexString(buf[i] & 0xFF); 
			if (hex.length() == 1) { 
				hex = '0' + hex; 
			} 
			sb.append(hex.toUpperCase()); 
    	} 
    	return sb.toString(); 
    } 

    /** 
    * 将16进制转换为二进制 
    * 
    * @param hexStr 
    * @return 
    */ 
    private static byte[] parseHexStr2Byte(String hexStr) { 
    	if (hexStr.length() < 1) 
    		return null; 
    	byte[] result = new byte[hexStr.length() / 2]; 
    	for (int i = 0; i < hexStr.length() / 2; i++) { 
    		int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16); 
    		int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),16); 
    		result[i] = (byte) (high * 16 + low); 
    	} 
    	return result; 
    } 
    /**
     * aes加密内容 
     * @param content 内容
     * @param keyword 提取串
     * @return
     */
    public static String aes_encrypt(String content, String keyword) {
        try {
            byte[] keyBytes = Arrays.copyOf(keyword.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes)).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } 
        return null;
    }
    public static Map<Encode, String> aes_encrypt(String content) {
    	Map<Encode, String> r = new HashMap<Encode, String>();
    	if (null==content||content.trim().equals("")) {
			return r;
		}
    	try {
        	String keyword = SerialID.getInstance().getStrId();
            byte[] keyBytes = Arrays.copyOf(keyword.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            r.put(Encode.KEY, keyword);
            r.put(Encode.VALUE, new String(Hex.encodeHex(ciphertextBytes)).toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } 
    	return r;
    }
    /**
     * aes解密加密过的内容
     * @param encryptContent 被加密的内容
     * @param keyword 提取串
     * @return
     */
    public static String aes_decrypt(String encryptContent, String keyword) {
        try {
            byte[] keyBytes = Arrays.copyOf(keyword.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Hex.decodeHex(encryptContent.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        } return null;
    }
    
    /**
     * des加密内容 
     * @param content 内容
     * @param keyword 提取串
     * @return
     */
    public static String des_encrypt(String content, String keyword) {
        try {
            byte[] keyBytes = Arrays.copyOf(keyword.getBytes("ASCII"),8);
            SecretKey key = new SecretKeySpec(keyBytes, "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes)).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } return null;
    }
    /**
     * des解密加密过的内容
     * @param encryptContent 被加密的内容
     * @param keyword 提取串
     * @return
     */
    public static String des_decrypt(String encryptContent, String keyword) {
        try {
            byte[] keyBytes = Arrays.copyOf(keyword.getBytes("ASCII"),8);
            SecretKey key = new SecretKeySpec(keyBytes, "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Hex.decodeHex(encryptContent.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        } return null;
    }
    
    public static void main(String[] args) {
//        String content = "薛正辉";
//        String password = "0123456789012345";
//        // 加密
//        System.out.println("加密前：" + content);
//        String encryptResult = des_encrypt(content, password);
//        System.out.println("加密后：" + encryptResult);
//        // 解密
//        String decryptResult = des_decrypt(encryptResult, password);
//        System.out.println("解密后：" + decryptResult);

      String content = "薛正辉是回家：）";
      // 加密
      System.out.println("加密前：" + content);
      Map<Encode, String> encryptResult = aes_encrypt(content);
      String key = encryptResult.get(Encode.KEY);
      String aescontent = encryptResult.get(Encode.VALUE);
      System.out.println("加密后：" + aescontent+"，提取KEY："+key);
      // 解密
      String decryptResult = aes_decrypt(aescontent, key);
      System.out.println("解密后：" + decryptResult);
    	
        //msql
        //SELECT HEX(des_encrypt("薛正辉", "0123456789012345"));
        //SELECT des_decrypt(UNHEX("2AB825CD33FF647AF60DCAE935537445"), "0123456789012345");
    }

}
