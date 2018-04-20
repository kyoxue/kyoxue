package com.ilib.utils;


import java.io.BufferedReader;
import java.io.FileReader;
/**
 * 简写sysout
 * 只能本地开发使用，正式环境，需去除这种打印。
 *
 */
public class Ilib
{
  public static void print(Object obj)
  {
    StackTraceElement ste = new java.lang.Throwable().getStackTrace()[1];
    String lineStr = print(ste);
    String name = extract(lineStr);
    String last = toStringSupportArray(obj);
    System.out.println(name+"--->"+last);
  }
  
  private static String toStringSupportArray(Object obj) { if (obj == null) {
      return null;
    }
    if ((obj instanceof int[]))
      return java.util.Arrays.toString((int[])obj);
    if ((obj instanceof long[]))
      return java.util.Arrays.toString((long[])obj);
    if ((obj instanceof double[]))
      return java.util.Arrays.toString((double[])obj);
    if ((obj instanceof boolean[]))
      return java.util.Arrays.toString((boolean[])obj);
    if ((obj instanceof char[]))
      return java.util.Arrays.toString((char[])obj);
    if ((obj instanceof byte[]))
      return java.util.Arrays.toString((byte[])obj);
    if ((obj instanceof short[]))
      return java.util.Arrays.toString((short[])obj);
    if ((obj instanceof float[]))
      return java.util.Arrays.toString((float[])obj);
    if ((obj instanceof Object[])) {
      return java.util.Arrays.toString((Object[])obj);
    }
    return obj.toString();
  }
  
  private static String extract(String lineStr) {
    lineStr = lineStr.trim();
    int start = lineStr.indexOf("De.print(") + 12;
    int end = lineStr.lastIndexOf(");");
    return lineStr.substring(start, end);
  }
  
  private static String print(StackTraceElement ste) { 
	String fileName = ste.getClassName();
    fileName = "src/main/java/" + fileName.replace(".", "/") + ".java";
    int line = ste.getLineNumber();
    return getStrByLine(fileName, line);
  }
  
  private static String getStrByLine(String fileName, int num) { 
	java.io.File file = new java.io.File(fileName);
    BufferedReader reader = null;
    String path; 
    String fullName = file.getAbsolutePath();
    fullName = fullName.replace("\\", "/");
    path = fullName.substring(0, fullName.lastIndexOf("/"));
    file = new java.io.File(path + "/" + file.getName());
    try {
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      int line = 1;
      while ((tempString = reader.readLine()) != null) {
        if (line == num) {
          path = tempString;return path;
        }
        line++;
      }
      reader.close();
    } catch (java.io.IOException e) {
      e.printStackTrace();
      
      if (reader != null) {
        try {
          reader.close();
        }
        catch (java.io.IOException localIOException2) {}
      }
    }
    finally
    {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (java.io.IOException localIOException3) {}
      }
    }
    return null;
  }
}
