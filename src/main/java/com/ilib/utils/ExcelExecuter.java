package com.ilib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilib.model.TestBeanA;
//import com.ilib.model.Person;

/**
 * Excel辅助工具类<br>
 * 使用2003 2007的接口类WorkBook来向下兼容<br>
 * 如果模板的列标题比实际数据的列数要多，不需要填充的列设置临时值，比如blank，注意每个列里blank还得不同用blank1,blank2以此这样来设置
 * @author Kyoxue
 */
public class ExcelExecuter {
	public static final Logger log = LoggerFactory.getLogger(ExcelExecuter.class);
	/**
	 * 默认每页读取条数
	 */
	public static final int DEFAULT_PAGESIZE = 3000;
	/**
	 * 如果读取的时候，没用设置rowIndex，则默认从第二行开始读取报表，第一行一般是标题行。
	 */
	public static final int DEFAULT_ROW_INDEX = 1;
	/**
	 * 线程读取和写出，最大不允许超过30个线程
	 */
	public static final int MAX_THREAD_NUM = 30;
	/**
	 * 一般数据的列数随标题行的列数固定，一次读取，后续不再读
	 */
	public static ThreadLocal<Integer> totalCells = new ThreadLocal<Integer>();
	/**
	 * 当前读取的页数
	 */
	public static ThreadLocal<Integer> pageInteger = new ThreadLocal<Integer>();
	/**
	 * 用户设置读取的分页大小
	 */
	public static ThreadLocal<Integer> pageSizeInteger = new ThreadLocal<Integer>();
	/**
	 * 用户设置从第几行开始读取
	 */
	public static ThreadLocal<Integer> rowIndexInteger = new ThreadLocal<Integer>();
	/**
	 * 分页取Excel数据
	 */
	public static LinkedList<LinkedList<String>> read(int page,String excel)throws Exception{
		if (!validateExcel(excel)) {
			throw new Exception("excel file is unright !");
		}
		boolean isExcel2003 = isVersion2003(excel);
		InputStream is = null;
		Workbook workbook = null;
		try {
			File file = new File(excel);
			is = new FileInputStream(file);
			workbook = initWorkbook(is, isExcel2003);
		} catch (Exception ex) {
			throw new Exception("load excel file exception !".concat(ex.toString()));
		}
		pageInteger.set(page);
		long startTime = System.currentTimeMillis();// 获取开始时间
		LinkedList<LinkedList<String>> datas = null;
		try {
			datas = pagingExcelData(page,workbook);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		long endTime = System.currentTimeMillis();// 获取结束时间
		log.info("total time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
		return datas;
	}
	/**
	 * 整张Excel一次性读取
	 */
	public static List<List<String>> read(String excel)throws Exception{
		if (!validateExcel(excel)) {
			throw new Exception("excel file is unright !");
		}
		boolean isExcel2003 = isVersion2003(excel);
		InputStream is = null;
		Workbook workbook = null;
		try {
			File file = new File(excel);
			is = new FileInputStream(file);
			workbook = initWorkbook(is, isExcel2003);
		} catch (Exception ex) {
			throw new Exception("load excel file exception !".concat(ex.toString()));
		}
		long startTime = System.currentTimeMillis();// 获取开始时间
		List<List<String>> datas = null;
		try {
			datas = getExcelData(workbook);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		long endTime = System.currentTimeMillis();// 获取结束时间
		log.info("total time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
		return datas;
	}
	public static boolean isVersion2003(String excel){
		boolean isExcel2003 = true;
		if (isExcel2007(excel)) {
			isExcel2003 = false;
		}
		return isExcel2003;
	}
	public static void setPageSize(int pageSize){
		pageSizeInteger.set(pageSize);
	}
	public static void setRowIndex(int rowIndex){
		rowIndexInteger.set(rowIndex);
	}
	/**
	 * 鉴别excel版本
	 */
	public static Workbook initWorkbook(InputStream in, boolean isExcel2003)throws Exception{
		Workbook wb = null;
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (isExcel2003) {
        	boolean v2003 = false;
        	try {
				v2003 = POIFSFileSystem.hasPOIFSHeader(in);
			} catch (IOException e) {
				if (null!=in) {
					try {
						in.close();
					} catch (IOException ioe) {
						in=null;
					}
				}
				throw new IOException(e.toString());
			}
        	if (v2003) {
        		try {
					wb = new HSSFWorkbook(in);
				} catch (IOException e) {
					throw new Exception(e.toString());
				}catch(POIXMLException pxe){//检测是否2003版本文件读入
					throw new POIXMLException("检查你的Excel文件后缀是否和文件本身一致，比如：本身是2007版本，光改文件后缀为.xls是没用的，它还是2007版本 ! ".concat(pxe.toString()));
				}catch(Exception e){
					throw new Exception("load excel Exception ! ".concat(e.toString()));
				}finally{
					if (null != in) {
						try {
							in.close();
						} catch (IOException ioe) {
							in =null;
						}
					}
				}
			}
        }
        if (!isExcel2003) {
        	boolean v2007 = false;
        	try {
				v2007 = POIXMLDocument.hasOOXMLHeader(in);
			} catch (IOException e) {
				if (null != in) {
					try {
						in.close();
					} catch (IOException ioe) {
						in =null;
					}
				}
				throw new IOException(e.toString());
			}
        	if (v2007) {
        		try {
					wb = new XSSFWorkbook(OPCPackage.open(in));
				} catch (InvalidFormatException e) {
					throw new Exception(e.toString());
				} catch (IOException e) {
					throw new Exception(e.toString());
				}catch(POIXMLException pxe){//检测是否2007版本文件读入
					throw new POIXMLException("检查你的Excel文件后缀是否和文件本身一致，比如：本身是2003版本，光改文件后缀为.xlsx是没用的，它还是2003版本 ! ".concat(pxe.toString()));
				}catch(Exception e){
					throw new Exception("load excel Exception ! ".concat(e.toString()));
				}finally{
					if (null != in) {
						try {
							in.close();
						} catch (IOException ioe) {
							in =null;
						}
					}
				}
			}
        }
        return wb;
    }
	public static LinkedList<LinkedList<String>> pagingExcelData(int page,Workbook excel)throws Exception{
		int pageSize = (pageSizeInteger.get() == null ? DEFAULT_PAGESIZE : pageSizeInteger.get().intValue());
		int rowIndex = (rowIndexInteger.get() == null ? DEFAULT_ROW_INDEX : rowIndexInteger.get().intValue());
		if (null == excel) {
			throw new Exception("Workbook instance is null !");
		}
		//起始页小于1，返回空
		if (page<1) {
			return null;
		}
		Sheet sheet = excel.getSheetAt(0);
		if (null == sheet) {
			throw new Exception("Workbook sheet is null !");
		}
		LinkedList<LinkedList<String>> currentDatas = new LinkedList<LinkedList<String>>();
		//EXCEL工作薄总记录条数
		int totalRows = sheet.getPhysicalNumberOfRows();
		//初步估计页数
		int tempPage  = totalRows/pageSize;
		//总页数
		int totalPage = ((totalRows % pageSize == 0)?(tempPage):(tempPage+1));
		//超过总页数，返回空
		if (page>totalPage) {
			return null;
		}
		//当前分页执行的记录条数
		int currentPage = (page > 1 ? (page -1) * pageSize : 0);
		//根据传入的起始页算出的实际获取的总记录条数
		int realTotalRows = totalRows - page + 1;
		//存在数据，取出总列数（有一条线程计算出列数，其余线程直接取，列数读一次。）
		if (realTotalRows >= (rowIndex+1) && sheet.getRow(rowIndex) != null) {
			int totalCellsCount = sheet.getRow(rowIndex).getPhysicalNumberOfCells();
			totalCells.set(totalCellsCount);
		}else{
			throw new Exception("check your row index is override !");
		}
		int startRow = (page ==1 ? rowIndex : 0);
		//开始取当前也的行数据
        for (int i = startRow; i < pageSize && i < totalRows - currentPage; i++) {
        	//当前页的行
        	Row row = sheet.getRow(currentPage + i);
        	if (row == null) {
				throw new Exception("error row data in excel !");
			}
        	LinkedList<String> eachRowDatas = getPagedData(row);
        	if (isNotBlank(eachRowDatas)) {
				currentDatas.add(eachRowDatas);
			}
        }
        return currentDatas;
	}
	public static List<List<String>> getExcelData(Workbook excel)throws Exception{
		int rowIndex = (rowIndexInteger.get() == null ? DEFAULT_ROW_INDEX : rowIndexInteger.get().intValue());
		if (null == excel) {
			throw new Exception("Workbook instance is null !");
		}
		Sheet sheet = excel.getSheetAt(0);
		if (null == sheet) {
			throw new Exception("Workbook sheet is null !");
		}
		List<List<String>> currentDatas = new ArrayList<List<String>>();
		//EXCEL工作薄总记录条数
		int totalRows = sheet.getPhysicalNumberOfRows();
		log.info("数据总记录：".concat(String.valueOf(totalRows)));
		//根据传入的起始页算出的实际获取的总记录条数
		int realTotalRows = totalRows - rowIndex;
		log.info("数据开始读取记录索引：".concat(String.valueOf(rowIndex)));
		log.info("数据过滤行后记录：".concat(String.valueOf(realTotalRows)));
		//存在数据，取出总列数（有一条线程计算出列数，其余线程直接取，列数读一次。）
		if (realTotalRows >= 1 && sheet.getRow(rowIndex) != null) {
			int totalCellsCount = sheet.getRow(rowIndex).getPhysicalNumberOfCells();
			totalCells.set(totalCellsCount);
		}else{
			throw new Exception("check your row index is override !");
		}
		//开始取当前也的行数据
        for (int i = rowIndex; i <= totalRows; i++) {
        	//当前页的行
        	Row row = sheet.getRow(i);
        	if (row == null) {//最后一行
				break;
			}
        	List<String> eachRowDatas = getPagedData(row);
        	if (isNotBlank(eachRowDatas)) {
				currentDatas.add(eachRowDatas);
			}
        }
        return currentDatas;
	}
	/**
	 * 获取当前分页行数据
	 */
	public static LinkedList<String> getPagedData(Row row)throws Exception{
		LinkedList<String> datas = new LinkedList<String>();
		int totalCellsCount = totalCells.get().intValue();
		//迭代列，获取每一列数据
		for (int c = 0; c < totalCellsCount; c++) {
			Cell cell = row.getCell(c);
			String cellValue = "";
			if (null != cell) {
				// 判断数据类型
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC: // 数字
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							cellValue = sdf.format(cell.getDateCellValue());// 日期
						} catch (Exception e) {
							throw new Exception("exception on get date data !".concat(e.toString()));
						}finally{
							sdf = null;
						}
					} else {
						cellValue = getString(cell.getNumericCellValue());// 数值
					}
					break;
				case Cell.CELL_TYPE_STRING: // 字符串
					cellValue = getString(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = getString(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA: // 公式
					cellValue = getString(cell.getCellFormula());
					break;
				case Cell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case Cell.CELL_TYPE_ERROR: // 故障
					cellValue = "ERROR VALUE";
					break;
				default:
					cellValue = "UNKNOW VALUE";
					break;
				}
			}
			datas.add(cellValue);
		}
		return datas;
	}
	public static boolean validateExcel(String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}
		if (!isNotBlank(filePath) || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			return false;
		}
		return true;
	}
	public static boolean isExcel2003(String filePath) {

		return filePath.matches("^.+\\.(?i)(xls)$");

	}
	public static boolean isExcel2007(String filePath) {

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}
	public static String getString(Object obj){
		if (null == obj) {
			return "";
		}
		String val = obj.toString();
		if (null == val || val.trim().equals("")) {
			return "";
		}
		return val.trim();
	}
	public static boolean isNotBlank(List<?> list){
		return (null ==list || list.size() == 0)?false:true;
	}
	public static boolean isNotBlank(String string){
		return (null ==string || string.trim().equals(""))?false:true;
	}
	public static String getDateTimeFromMill(long mill){
		String dateStr = "";
		try {
			Date date = new Date(mill);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}
	public static String getMillDiff(long start,long end){
		BigDecimal startDec = new BigDecimal(start);
		BigDecimal endDec = new BigDecimal(end);
		BigDecimal sub = endDec.subtract(startDec);
		BigDecimal sec = sub.divide(new BigDecimal(1000l)).setScale(2,BigDecimal.ROUND_HALF_UP);;
		return String.valueOf(sec.floatValue());
	}
	//用来装载所需的结果集 静态公共变量，线程用synchronized同步锁住 treeMap可以自动根据KEY排序
	public static Map<Integer, LinkedList<LinkedList<String>>> resultMap = new TreeMap<Integer, LinkedList<LinkedList<String>>>();
	/**
	 * 分线程读取EXCEL
	 */
	public static LinkedList<LinkedList<String>> readWithThread(String excel)throws Exception{
		String pre = "---->";
		int pageSize = (pageSizeInteger.get() == null ? DEFAULT_PAGESIZE : pageSizeInteger.get().intValue());
		int rowIndex = (rowIndexInteger.get() == null ? DEFAULT_ROW_INDEX : rowIndexInteger.get().intValue());
		//根据分页大小获取线程数
		if (!validateExcel(excel)) {
			throw new Exception("excel file is unright !");
		}
		boolean isExcel2003 = isVersion2003(excel);
		InputStream is = null;
		Workbook workbook = null;
		try {
			File file = new File(excel);
			is = new FileInputStream(file);
			workbook = initWorkbook(is, isExcel2003);
		} catch (Exception ex) {
			throw new Exception("load excel file exception !".concat(ex.toString()));
		} 
		if (null == workbook) {
			throw new Exception("Workbook instance is null !");
		}
		Sheet sheet = workbook.getSheetAt(0);
		if (null == sheet) {
			throw new Exception("Workbook sheet is null !");
		}
		//------------------------------------计算线程数量---------------------------------
		//EXCEL工作薄总记录条数
		int totalRows = sheet.getPhysicalNumberOfRows();
		//初步估计页数
		int tempPage  = totalRows/pageSize;
		//总页数
		int threadNum =  ((totalRows % pageSize == 0)?(tempPage):(tempPage+1));
		//------------------------------------------------------------------------------
		if (threadNum>MAX_THREAD_NUM) {
			throw new Exception("too small of page size !");
		}
		long timeout = 30l;
		log.info(pre+"start thread，num:"+threadNum+" count down time out:"+timeout+".sec");
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		//用来装载线程，最后结束所有线程
		Map<String, Future<?>> futureMap = new ConcurrentHashMap<String, Future<?>>();
		 ExecutorService taskExecutor = Executors.newCachedThreadPool();
		log.info(pre+"begin to run each thread...");
		long startTime = System.currentTimeMillis();// 获取开始时间
		for (int i = 1; i <= threadNum; i++) {
			//生成每个线程
			ExcelThread thread = new ExcelThread();
			thread.setCountDownLatch(countDownLatch);//传递给每个线程用来统计，线程结束通过countDown来计数
			thread.setResultMap(resultMap);
			thread.setPage(i);//当前页
			thread.setPageSize(pageSize);//分页大小
			thread.setRowIndex(rowIndex);//从excel第几行开始读，下标
			thread.setWorkbook(workbook);//excel对象
			Future<?> future = taskExecutor.submit(thread);
			futureMap.put("THREAD" + i, future);
		}
		log.info(pre+"thread all submit！");
		log.info(pre+"wait for countdown...");
		try {
			//直到所有线程countDown，才往下走。
			countDownLatch.await(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("count down error.",e);
		}finally{
			taskExecutor.shutdown();
			long endTime = System.currentTimeMillis();// 获取结束时间
			log.info("all task time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
		}
		log.info(pre+"count down over,start next step...");
		//结束所有线程任务
		stop(futureMap);
		log.info(pre+"threads all done...");
		if (null == resultMap || resultMap.size() == 0) {
			log.error(pre+"none datas...");
			return null;
		}
		LinkedList<LinkedList<String>> finalDatas = new LinkedList<LinkedList<String>>();
		Iterator<Entry<Integer, LinkedList<LinkedList<String>>>> iter = resultMap.entrySet().iterator();
		while (iter.hasNext()) {
			finalDatas.addAll((iter.next()).getValue());
		}
		return finalDatas;
	}
	public static void stop(Map<String, Future<?>> futureMap){
		try {
			if (null != futureMap) {
				Iterator<String> iter = futureMap.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					Future<?> eachFuture = futureMap.get(key);
					eachFuture.cancel(true);
					log.info("thread future："+key+"stoped.");
				}
			}
		} catch (Exception e) {
			log.error("stop futures error ！",e);
		} finally {
			futureMap.clear();
		}
	}
	public static String getCellValToString(Cell cell){
		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return DateTimeUtils.getDate(cell.getDateCellValue());
			}else{
				return String.valueOf(cell.getNumericCellValue());
			}
		}else if(type == Cell.CELL_TYPE_BLANK){
			return "";
		}else if(type == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}else if (type == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		}
		return cell.getStringCellValue();
	}
	private static LinkedHashMap<String, String> getTempalte(Workbook workBook) throws Exception{
		LinkedHashMap<String, String> titileAndPropertyNameMap = new LinkedHashMap<String, String>();
		LinkedList<String> titles = new LinkedList<String>();
		LinkedList<String> propertyNames = new LinkedList<String>();
		if (null!=workBook) {
			Sheet sheet = workBook.getSheetAt(0);
			int index = 0;
			if (sheet != null) {
				//row.cellIterator() sheet.rowIterator() 与row.iterator sheet.iterator 区别，前者按原序列。
				for (Iterator<Row> iter = (Iterator<Row>) sheet.rowIterator(); iter.hasNext();) {
					Row row = (Row) iter.next();
					for (Iterator<Cell> iterator = row.cellIterator(); iterator.hasNext();) {
						Cell cell = (Cell) iterator.next();
						String cellVal = getString(getCellValToString(cell));
						if (isNotBlank(cellVal)) {
							if (index == 0) {
								titles.add(cellVal);//列头标题
							}
							if (index == 1 ) {
								propertyNames.add(cellVal);//配置的属性名称
							}
						}
					}
					index++;
					if (index > 1) {
						break;
					}
				}
			}else{
				throw new Exception("HSSFSheet is null !");
			}
		}else{
			throw new Exception("HSSFWorkbook is null !");
		}
		if (titles.size() == propertyNames.size()) {
			for (int i = 0; i < titles.size(); i++) {
				titileAndPropertyNameMap.put(titles.get(i),propertyNames.get(i));//不要用属性作为KEY
			}
			return titileAndPropertyNameMap;
		}
		throw new Exception("get title and bean propery name error!check the template !");
	}
	private static void setExcelTitle(Workbook workBook, Collection<String> titles) throws Exception{
		if (workBook == null) {
			throw new Exception("HSSFWorkbook is null !");
		}
		Sheet sheet = workBook.getSheetAt(0);
		if (sheet == null) {
			throw new Exception("HSSFSheet is null !");
		}
		Row row = sheet.createRow(0);//创建标题行
		int index = 0;
		for (String title:titles) {
			Cell cell = row.createCell(index);
			cell.setCellValue(title);
			index++;
		}
	}
	private static HashMap<String, String> getFieldType(Object instance,Collection<String> propertyNames)throws Exception {
		if (null == instance) {
			throw new Exception("get field type error:instance is null !");
		}
		if (null == propertyNames || propertyNames.size() == 0) {
			throw new Exception("get field type error:property names is null !");
		}
		HashMap<String, String> map = new HashMap<String, String>();
		//vo集成的父类字段名，这样可以在模板配置父类的数据
		@SuppressWarnings("rawtypes")
		Class parentClass = instance.getClass().getSuperclass();
		Field[] parentFields = parentClass.getDeclaredFields();
		//vo的字段名
		Field[] fields = instance.getClass().getDeclaredFields();
		fields = (Field[]) ArrayUtils.addAll(fields, parentFields);
		Iterator<String>  propertyIter = propertyNames.iterator();
		//将模板配置的属性的类型取出，后续用来判断类型写入数据时进行响应处理。
		//比如日期格式化，数字四舍五入等。
		while (propertyIter.hasNext()) {
			String propertyName = getString(propertyIter.next()) ;
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				if (propertyName.toLowerCase().equals(fieldName.toLowerCase())) {//支持XML模板配置全部小写的属性名称
					map.put(fieldName, fields[i].getType().toString());
					break;
				}
			}
		}
		return map;
	}
	
	public static void writeExcelWithThread(String excelTemplate, List<?> datas, String output,int threadNum,String blank)throws Exception{
		if (!isNotBlank(datas)) {
			log.warn("there is none data to write...");
			return;
		}
		if (threadNum>MAX_THREAD_NUM) {
			throw new Exception("too larget of thread num,must be under"+MAX_THREAD_NUM+" !");
		}
		String pre = "---->";
		int totalRows = datas.size();
		log.info("totalRows:".concat(String.valueOf(totalRows)));
		int pageSize = (totalRows/threadNum);
		log.info("pagesize:".concat(String.valueOf(pageSize)));
		//初步估计页数
		int tempPage  = totalRows/pageSize;
		//总页数
		int totalPage =  ((totalRows % pageSize == 0)?(tempPage):(tempPage+1));
		log.info("totalPage:".concat(String.valueOf(totalPage)));
		OutputStream out = null;
		InputStream in = null;
		long startTime = System.currentTimeMillis();// 获取开始时间
		try {
			//输出文件路径
			out = new FileOutputStream(output, true); 
			//读取模板路径
			in = new FileInputStream(new File(excelTemplate));
			// Workbook workBook = WorkbookFactory.create(in);
			Workbook workBook = initWorkbook(in, isExcel2003(excelTemplate));//自适应读取版本文件
			//加载模板数据
			LinkedHashMap<String, String> titleAndPropNames = getTempalte(workBook);
			if (null == titleAndPropNames || titleAndPropNames.size() == 0) {
				throw new Exception("load template error !");
			}
			//设置标题行
			setExcelTitle(workBook, titleAndPropNames.keySet());
			//配置的vo属性名
			Collection<String> propertyNames = titleAndPropNames.values();
			HashMap<String, String> propertyNameAndTypes = getFieldType(datas.get(0), propertyNames);
			long timeout = 30l;
			log.info(pre+"start thread，num:"+threadNum+" count down time out:"+timeout+".sec");
			CountDownLatch countDownLatch = new CountDownLatch(threadNum);
			//用来装载线程，最后结束所有线程
			Map<String, Future<?>> futureMap = new ConcurrentHashMap<String, Future<?>>();
			 ExecutorService taskExecutor = Executors.newCachedThreadPool();
			log.info(pre+"begin to run each thread...");
			//数据分页线程去生成行
			for (int i = 1; i <= totalPage; i++) {
				List<?> pagingData = pagingCollection(i, pageSize, datas);
				//生成每个线程
				ExcelOutputThread thread = new ExcelOutputThread();
				thread.setCountDownLatch(countDownLatch);//传递给每个线程用来统计，线程结束通过countDown来计数
				thread.setDatas(pagingData);
				thread.setPage(i);
				thread.setBlank(blank);
				thread.setPageSize(pageSize);
				thread.setRowIndex(i);
				thread.setWorkbook(workBook);
				thread.setPropertyNames(propertyNames);
				thread.setPropertyTypes(propertyNameAndTypes);
				Future<?> future = taskExecutor.submit(thread);
				futureMap.put("THREAD" + i, future);
				Thread.sleep(100);
			}
			log.info(pre+"thread all submit！");
			log.info(pre+"wait for countdown...");
			try {
				//直到所有线程countDown，才往下走。
				countDownLatch.await(timeout, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				log.error("count down error.",e);
			}finally{
				taskExecutor.shutdown();
			}
			log.info(pre+"count down over,start next step...");
			//结束所有线程任务
			stop(futureMap);
			log.info(pre+"threads all done...");
			//写出数据到Excel
			workBook.write(out);
		} catch (Exception e) {
			throw new Exception("output excel exception !".concat(e.toString()));
		}finally{
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
				}
			}
			log.info(pre+"write task over...");
			long endTime = System.currentTimeMillis();// 获取结束时间
			log.info("all task time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
		}
	}
	/**
	 * 分页生成excel
	 * @param excelTemplate 模板
	 * @param datas 数据
	 * @param output 生成文件
	 * @param pageSize 分页大小
	 * @param blank 报表模板中无须导出数据的列所设置的临时值比如blank1,blank2
	 * @throws Exception
	 */
	public static void write(String excelTemplate, List<?> datas, String output,int pageSize,String blank)throws Exception{
		if (isNotBlank(datas)) {
			int totalRows = datas.size();
			//初步估计页数
			int tempPage  = totalRows/pageSize;
			//总页数
			int totalPage =  ((totalRows % pageSize == 0)?(tempPage):(tempPage+1));
			OutputStream out = null;
			InputStream in = null;
			long startTime = System.currentTimeMillis();// 获取开始时间
			try {
				//输出文件路径
				out = new FileOutputStream(output, true); 
				//读取模板路径
				in = new FileInputStream(new File(excelTemplate));
				//生成excel对象
				Workbook workBook = initWorkbook(in, isExcel2003(excelTemplate));
				//加载模板数据
				LinkedHashMap<String, String> titleAndPropNames = getTempalte(workBook);
				if (null == titleAndPropNames || titleAndPropNames.size() == 0) {
					throw new Exception("load template error !");
				}
				//设置标题行
				setExcelTitle(workBook, titleAndPropNames.keySet());
				//配置的vo属性名
				Collection<String> propertyNames = titleAndPropNames.values();
				
				HashMap<String, String> propertyNameAndTypes = getFieldType(datas.get(0), propertyNames);
				//数据分页去生成行
				for (int i = 1; i <= totalPage; i++) {
					List<?> pagingData = pagingCollection(i, pageSize, datas);
					if (isNotBlank(pagingData)) {
						insertData(pageSize,i, pagingData, workBook, propertyNames, propertyNameAndTypes,blank);
					}
					Thread.sleep(300);
				}
				//写出数据到Excel
				workBook.write(out);
			} catch (Exception e) {
				throw new Exception("output excel exception !".concat(e.toString()));
			}finally{
				if (null != out) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
					}
				}
				long endTime = System.currentTimeMillis();// 获取结束时间
				log.info("total time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
			}
		}
	}
	/**
	 * 一次性生成Excel
	 * @param excelTemplate 模板
	 * @param datas 数据
	 * @param output 生成文件
	 * @throws Exception
	 */
	public static void write(String excelTemplate, List<?> datas, String output,String blank)throws Exception{
		if (isNotBlank(datas)) {
			int pageSize = 0;
			int page = 1;
			OutputStream out = null;
			InputStream in = null;
			long startTime = System.currentTimeMillis();// 获取开始时间
			try {
				//输出文件路径
				out = new FileOutputStream(output, true); 
				//读取模板路径
				in = new FileInputStream(new File(excelTemplate));
				//生成excel对象
				Workbook workBook = initWorkbook(in, isExcel2003(excelTemplate));
				//加载模板数据
				LinkedHashMap<String, String> titleAndPropNames = getTempalte(workBook);
				if (null == titleAndPropNames || titleAndPropNames.size() == 0) {
					throw new Exception("load template error !");
				}
				//设置标题行
				setExcelTitle(workBook, titleAndPropNames.keySet());
				//配置的vo属性名
				Collection<String> propertyNames = titleAndPropNames.values();
				HashMap<String, String> propertyNameAndTypes = getFieldType(datas.get(0), propertyNames);
				//数据生成行
				if (isNotBlank(datas)) {
					insertData(pageSize,page, datas, workBook, propertyNames, propertyNameAndTypes,blank);
				}
				//写出数据到Excel
				workBook.write(out);
			} catch (Exception e) {
				throw new Exception("output excel exception !".concat(e.toString()));
			}finally{
				if (null != out) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
					}
				}
				long endTime = System.currentTimeMillis();// 获取结束时间
				log.info("total time cost-->[start:" + getDateTimeFromMill(startTime) + "][end:" + getDateTimeFromMill(endTime) + "][cost:" + getMillDiff(startTime, endTime) + "sec]");
			}
		}
	}
	public static void insertData(int pageSize,int rowIndex,List<?> datas,Workbook workbook,Collection<String> propertyNames,Map<String, String> propertyTypes,String blank){
		if (isNotBlank(datas)) {
			//获取第一个工作薄
			Sheet sheet = workbook.getSheetAt(0) ;
			//当前集合数据的大小
			int dataSize = datas.size();
			if (pageSize == 0) {
				pageSize = dataSize;
			}
			//逐列增加，每列写数据
			int index = 0;
			rowIndex = (rowIndex == 1?1:rowIndex*pageSize -pageSize + 1);
			//VO属性名，模板配置的名字集合
			if (null!=propertyNames && propertyNames.size()>0) {
				for (int i = 0; i < dataSize; i++) {
					Row row = sheet.createRow(rowIndex);
					index = 0;
					String cellVal = "";
					Object data = datas.get(i);
					for (String propertyName : propertyNames) {
						Cell cell = row.createCell(index);
						Object propertyVal = null;
						if (!propertyName.contains(blank)) {
							propertyVal = getFieldValueByName(getString(propertyName), data);
						}
						if (null != propertyVal) {
							//字段类型
							String propertyType = propertyTypes.get(propertyName);
							//针对日期格式化处理
							if (propertyType.contains("java.util.Date")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								cellVal = sdf.format((Date)propertyVal);
							}else{
								cellVal = propertyVal.toString();
							}
							cell.setCellValue(cellVal);
						}else{
							//报表模板有些字段本身是空在那后期 填数据的，这个时候在模板设置一些无关的值，来跟列头索引对应，但是反射取的时候就会得不到真正的值
							//所以值设置为空
							cell.setCellType(Cell.CELL_TYPE_BLANK);
							cell.setCellValue("");
						}
						index++;
					}
					rowIndex++;
				}
			}
		}
	}
	public static Object getFieldValueByName(String fieldName, Object instance) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = instance.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(instance, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 分页取集合数据（不在页数范围内数据返回为空，正常的页数范围：一到总页数）
	 * @param page 起始页
	 * @param pageSize 每页的数据条数
	 * @param collection 待分页取的集合数据
	 * @return 当前页的数据
	 */
	public static <T> List<T> pagingCollection(int page,int pageSize,List<T> collection){
		if (null == collection || collection.size() ==0) {
			return null;
		}
		if (page<1) {
			return null;
		}
		int totalNum = collection.size();
		int tempPage  = totalNum/pageSize;
		int totalPage = ((totalNum % pageSize == 0)?(tempPage):(tempPage+1));
		if (page>totalPage) {
			return null;
		}
		List<T> currentCollection = new LinkedList<T>();
        int currentPage = (page > 1 ? (page -1) * pageSize : 0);
        for (int i = 0; i < pageSize && i < collection.size() - currentPage; i++) {
            currentCollection.add(collection.get(currentPage + i));
        }
        return currentCollection;
	}
	public static String getUniqueNumber(String pre){
		 String un = "" ;        
		 String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
		 String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
		 un = trandNo.toString().substring(0, 4);
		 un = un + sdf ;
		 if (null!=pre&&!"".equals(pre.trim())) {
			 return pre+"_"+un;
		 }
		 return un ;
	 }
	public static void main(String[] args)throws Exception {
		//各种用法
		//读取
		//取所有 46203行 cost:2.01sec
//		ExcelExecuter.setRowIndex(3);
//		List<List<String>> allReadDatas =  ExcelExecuter.read("C:/Users/Administrator/Desktop/POI读取写入EXCEL工具类/测试报表/上市公司财务指示.xls");
//		testDataResult(allReadDatas, "D:/target.txt");
		//分线程取 46203行 cost:1.11sec
//		ExcelExecuter.setPageSize(5000);//测试了下10个线程最佳 
//		ExcelExecuter.setRowIndex(3);
//		LinkedList<LinkedList<String>> datas = ExcelExecuter.readWithThread("C:/Users/Administrator/Desktop/POI读取写入EXCEL工具类/测试报表/上市公司财务指示.xls");
//		testDataResult(datas, "D:/target.txt");
		//分页取 5000 cost:0.23sec
//		ExcelExecuter.setPageSize(5000);
//		ExcelExecuter.setRowIndex(3);
//		LinkedList<LinkedList<String>> datas = ExcelExecuter.read(2, "C:/Users/Administrator/Desktop/POI读取写入EXCEL工具类/测试报表/上市公司财务指示.xls");
//		testDataResult(datas, "D:/target.txt");
		
		//输出
		List<TestBeanA> datas = new ArrayList<TestBeanA>();
		for (int i = 1; i < 120000; i++) {
			TestBeanA  p = new TestBeanA();
			p.setName("name"+i);
			p.setAge("3"+i);
			p.setTel("1341046110"+i);
			datas.add(p);
		}
		String blank = "blank";
		String excelTemplate = ExcelExecuter.class.getClassLoader().getResource("").getPath().concat("template_excel/testTemp.xls");
//		String output = ExcelExecuter.class.getClassLoader().getResource("").getPath().concat("download_excel/").concat(getUniqueNumber(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).concat(".xls"));
//		分批生成 cost:1.46sec 9000行
//		write(excelTemplate, datas, output, 3000);
		//一次性生成 cost:0.47sec 9000行
//		write(excelTemplate, datas, output);
		//分线程写出 cost:10 线程 1.58sec 5 线程  cost:1.03sec 2 线程 cost:0.69sec 1线程 cost:0.53sec 9000行
//		writeExcelWithThread(excelTemplate, datas, output, 1);
		//分批生成多张
		PagenationUtil<TestBeanA> p = new PagenationUtil<>(datas, 20000);
		int totalpage = p.getTotalPage();
		for (int i = 1; i < totalpage+1; i++) {
			String filename = getUniqueNumber(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).concat(String.valueOf(i))).concat(".xls");
			String output = ExcelExecuter.class.getClassLoader().getResource("").getPath().concat("download_excel/").concat(filename);
			p.setPage(i);
			List<TestBeanA> eachlist = p.getPageList();
			write(excelTemplate, eachlist, output,blank);
		}
//		总结： IO输出还是一次性最短时间，频繁读取写入跟SQL一样时间只会越长。
//		     IO只读取，分线程比一次性读取要快一倍，分页取，读的数据越少时间越快。
//			   所以，读取分线程读，写入一次性写。
	}
	
	public static void testDataResult(List<List<String>> datas,String excel)throws Exception{
		File target = new File(excel);
		FileOutputStream out = new FileOutputStream(target);
		OutputStreamWriter writer  = new OutputStreamWriter(out,"UTF-8");
		BufferedWriter bw = new BufferedWriter(writer);
		for (List<String> list : datas) {
			StringBuffer sb = new StringBuffer();
			for (String string : list) {
				sb.append(string).append(" ");
			}
			String line = sb.toString()+"\r\n";
			bw.write(line);
		}
		bw.close();
		writer.close();
		out.close();
		out.flush();
	}
	public static void testDataResult(LinkedList<LinkedList<String>> datas,String excel)throws Exception{
		File target = new File(excel);
		FileOutputStream out = new FileOutputStream(target);
		OutputStreamWriter writer  = new OutputStreamWriter(out,"UTF-8");
		BufferedWriter bw = new BufferedWriter(writer);
		for (List<String> list : datas) {
			StringBuffer sb = new StringBuffer();
			for (String string : list) {
				sb.append(string).append(" ");
			}
			String line = sb.toString()+"\r\n";
			bw.write(line);
		}
		bw.close();
		writer.close();
		out.close();
		out.flush();
	}
}
/**
 * 分页线程获取EXCEL数据，
 * <br>这里用callable接口不用runable接口，因为使用线程池runable并不能传值到线程，
 * <br>而如果不用线程池提交线程，用.run()原始运行，runable可以获取到传参。
 * @author Kyoxue
 *
 */
class ExcelThread implements Callable<Map<Integer, LinkedList<LinkedList<String>>>>,Serializable {
	private static final long serialVersionUID = 2653370912987041067L;
	public static final Logger logger = LoggerFactory.getLogger(ExcelThread.class);
	private CountDownLatch countDownLatch;
	private Integer pageSize;//这里类型用对象应用类型 如果是空，则用默认的pageSize
	private Integer rowIndex;//这里类型用对象应用类型 如果是空，则用默认的rowIndex
	private int page;
	private Map<Integer, LinkedList<LinkedList<String>>> resultMap;
	private Workbook workbook;
	@Override
	public Map<Integer, LinkedList<LinkedList<String>>> call() throws Exception {
		try {
			logger.info("excel page:"+page+"running..");
			if (null!=getPageSize()) {
				ExcelExecuter.setPageSize(getPageSize().intValue());
			}
			if (null!=getRowIndex()) {
				ExcelExecuter.setRowIndex(getRowIndex().intValue());
			}
			LinkedList<LinkedList<String>> datas = ExcelExecuter.pagingExcelData(page,workbook);
			synchronized (resultMap) {
				resultMap.put(getPage(), datas);
			}
		} catch (Exception e) {
			logger.error("excel page:"+page+" execute exception ！",e);
		} finally {
			if (null != countDownLatch) {
				countDownLatch.countDown();
			}
			logger.info("excel page:"+page+" finish task..");
		}
		return resultMap;
	}
	
	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Map<Integer, LinkedList<LinkedList<String>>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<Integer, LinkedList<LinkedList<String>>> resultMap) {
		this.resultMap = resultMap;
	}

}

class ExcelOutputThread implements Callable<Workbook>,Serializable {
	private static final long serialVersionUID = 2653370912987041067L;
	public static final Logger logger = LoggerFactory.getLogger(ExcelThread.class);
	private CountDownLatch countDownLatch;
	private int pageSize;
	private int rowIndex;
	private int page;
	private String blank;
	private Workbook workbook;
	private List<?> datas;
	private Collection<String> propertyNames;
	private Map<String, String> propertyTypes;
	@Override
	public Workbook call() throws Exception {
		try {
			logger.info("excel page:"+page+"running..");
			synchronized (workbook) {
				ExcelExecuter.insertData(pageSize, rowIndex, datas, workbook, propertyNames, propertyTypes,blank);
			}
		} catch (Exception e) {
			logger.error("excel page:"+page+" execute exception ！",e);
		} finally {
			if (null != countDownLatch) {
				countDownLatch.countDown();
			}
			logger.info("excel page:"+page+" finish task..");
		}
		return workbook;
	}
	public String getBlank() {
		return blank;
	}
	public void setBlank(String blank) {
		this.blank = blank;
	}
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public Workbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	public List<?> getDatas() {
		return datas;
	}
	public void setDatas(List<?> datas) {
		this.datas = datas;
	}
	public Collection<String> getPropertyNames() {
		return propertyNames;
	}
	public void setPropertyNames(Collection<String> propertyNames) {
		this.propertyNames = propertyNames;
	}
	public Map<String, String> getPropertyTypes() {
		return propertyTypes;
	}
	public void setPropertyTypes(Map<String, String> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	
}