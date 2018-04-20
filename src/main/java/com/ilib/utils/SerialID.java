package com.ilib.utils;


import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class SerialID
{
  private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7','8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static Map<Character, Integer> rDigits = new HashMap(16);
  
  static { for (int i = 0; i < digits.length; i++) {
      rDigits.put(Character.valueOf(digits[i]), Integer.valueOf(i));
    }
  }
  
  private static SerialID me = new SerialID();
  private String hostAddr;
  private Random random = new SecureRandom();
  private MessageDigest mHasher;
  private UniqTimer timer = new UniqTimer();
  private static long tag = 1L;
  
  private ReentrantLock opLock = new ReentrantLock();
  
  private SerialID() {
    try {
      InetAddress addr = InetAddress.getLocalHost();
      
      this.hostAddr = addr.getHostAddress();
    } catch (IOException e) {
      this.hostAddr = String.valueOf(System.currentTimeMillis());
    }
    
    if ((this.hostAddr == null) || (this.hostAddr.length() == 0) || 
      ("127.0.0.1".equals(this.hostAddr))) {
      this.hostAddr = String.valueOf(System.currentTimeMillis());
    }
    try
    {
      this.mHasher = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException nex) {
      this.mHasher = null;
    }
  }
  
  public static SerialID getInstance()
  {
    return me;
  }
  
  public long getUniqTime()
  {
    return this.timer.getCurrentTime();
  }
  
  public String getUniqID()
  {
    StringBuffer sb = new StringBuffer();
    long t = this.timer.getCurrentTime();
    sb.append(t);
    sb.append("-");
    sb.append(this.random.nextInt(8999) + 1000);
    sb.append("-");
    sb.append(this.hostAddr);
    sb.append("-");
    sb.append(Thread.currentThread().hashCode());
    return sb.toString();
  }
  
  public String getUniqIDHashString()
  {
    return hashString(getUniqID());
  }
  
  public byte[] getUniqIDHash()
  {
    return hash(getUniqID());
  }
  
  public byte[] hash(String str)
  {
    this.opLock.lock();
    try {
      byte[] bt = this.mHasher.digest(str.getBytes("UTF-8"));
      if ((bt == null) || (bt.length != 16)) {
        throw new IllegalArgumentException("md5 need");
      }
      return bt;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("unsupported utf-8 encoding", e);
    } finally {
      this.opLock.unlock();
    }
  }
  
  public byte[] hash(byte[] data)
  {
    this.opLock.lock();
    try {
      byte[] bt = this.mHasher.digest(data);
      if ((bt == null) || (bt.length != 16)) {
        throw new IllegalArgumentException("md5 need");
      }
      return bt;
    } finally {
      this.opLock.unlock();
    }
  }
  
  public String hashString(String str)
  {
    byte[] bt = hash(str);
    return bytes2string(bt);
  }
  
  public String hashBytes(byte[] str)
  {
    byte[] bt = hash(str);
    return bytes2string(bt);
  }
  
  public String bytes2string(byte[] bt)
  {
    int l = bt.length;
    
    char[] out = new char[l << 1];
    
    int i = 0; for (int j = 0; i < l; i++) {
      out[(j++)] = digits[((0xF0 & bt[i]) >>> 4)];
      out[(j++)] = digits[(0xF & bt[i])];
    }
    
    return new String(out);
  }
  
  public byte[] string2bytes(String str)
  {
    if (str == null) {
      throw new NullPointerException("参数不能为空");
    }
    if (str.length() != 32) {
      throw new IllegalArgumentException("字符串长度必须是32");
    }
    byte[] data = new byte[16];
    char[] chs = str.toCharArray();
    for (int i = 0; i < 16; i++) {
      int h = ((Integer)rDigits.get(Character.valueOf(chs[(i * 2)]))).intValue();
      int l = ((Integer)rDigits.get(Character.valueOf(chs[(i * 2 + 1)]))).intValue();
      data[i] = ((byte)((h & 0xF) << 4 | l & 0xF));
    }
    return data;
  }
  
  private static class UniqTimer
  {
    private AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());
    
    public long getCurrentTime() {
      return this.lastTime.incrementAndGet();
    }
  }
  
  private String getBaseId() {
    tag = tag > 99999L ? 1L : tag + 1L;
    return System.currentTimeMillis() + String.format("%05d", new Object[] { Integer.valueOf(0) });
  }
  
  public String getStrId() {
    return getLongId().toString();
  }
  
  public Long getLongId() {
    int sj = this.random.nextInt(9);
    BigDecimal id =  new BigDecimal(getBaseId());
    BigDecimal tg =  new BigDecimal(tag);
    BigDecimal sjl = new BigDecimal(sj);
    BigDecimal r= id.add(tg).add(sjl);
    return Long.valueOf(r.longValue());
  }
  
  public BigDecimal getDecimalId() {
    return new BigDecimal(getStrId());
  }
  
  public static void main(String[] args) {
    System.out.println(getInstance().getStrId());
  }
}
