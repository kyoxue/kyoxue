package com.ilib.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ilib.common.SpringContext;
import com.ilib.other.threads.TestThread;
import com.ilib.testcommon.BaseTest;

public class ThreadTest extends BaseTest{
	Logger log = LoggerFactory.getLogger(ThreadTest.class);
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	/**
	 * 测试线程池配置
	 */
	@Test
	public void testThreadPool() throws Exception {
		logger.info(taskExecutor.getCorePoolSize()+"----------------");
		logger.info(taskExecutor.getMaxPoolSize()+"----------------");
		logger.info(taskExecutor.getKeepAliveSeconds()+"----------------");
		String pre = "---->";
		int threadNum = 10;
		long timeout = 30l;
		logger.info(pre+"开始运行，线程数:"+threadNum+" 线程计数器等待最大延迟:"+timeout+".sec");
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		//用来装载所需的结果集
		List<String> returnList = new ArrayList<String>();
		//用来装载线程，最后结束所有线程
		Map<String, Future<?>> futureMap = new ConcurrentHashMap<String, Future<?>>();
		logger.info(pre+"开始生成每一个线程。");
		for (int i = 0; i < threadNum; i++) {
			//生成每个线程
			TestThread testThread = (TestThread) SpringContext.getBean("testThread");
			testThread.setCountDownLatch(countDownLatch);//传递给每个线程用来统计，线程结束通过countDown来计数
			testThread.setReturnList(returnList);//传递到每个线程装载返回的结果信息
			testThread.setVal(i+"");//传递给每个线程的参数
			Future<?> future = taskExecutor.submit(testThread);
			futureMap.put("THREAD" + i, future);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.error("sleep error.",e);
			}
		}
		logger.info(pre+"线程生成完毕！");
		logger.info(pre+"开始等待线程计数...");
		try {
			//直到所有线程countDown，才往下走。
			countDownLatch.await(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("count down error.",e);
		}
		logger.info(pre+"线程计数结束...");
		//当前线程池活动线程数目
		int count = taskExecutor.getActiveCount();
		logger.info("当前线程池活动线程数目: "+count);
		//结束所有线程任务
		stop(futureMap);
		logger.info(pre+"所有线程任务被结束...");
		if (null == returnList || returnList.size() == 0) {
			logger.error(pre+"未获取到任何信息。。");
			return;
		}
		logger.info(pre+"开始输出结果信息：");
		for (String string : returnList) {
			logger.info(pre+string);
		}
	}
	private void stop(Map<String, Future<?>> futureMap){
		try {
			if (null != futureMap) {
//				Iterator<Future<?>> iter = futureMap.values().iterator();
//				while (iter.hasNext()) {
//					Future<?> eachFuture = iter.next();
//					eachFuture.cancel(true);
//				}
				Iterator<String> iter = futureMap.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					Future<?> eachFuture = futureMap.get(key);
					eachFuture.cancel(true);
					logger.info("线程任务："+key+"已停止。");
				}
			}
		} catch (Exception e) {
			logger.error("结束所有线程任务异常！",e);
		} finally {
			futureMap.clear();
		}
	}
}
