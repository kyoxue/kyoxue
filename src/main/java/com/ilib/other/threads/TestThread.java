package com.ilib.other.threads;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class TestThread implements Runnable{

	public static final Logger logger = LoggerFactory.getLogger(TestThread.class);
	private CountDownLatch countDownLatch;
	private String val;
	private List<String> returnList;
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public List<String> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<String> returnList) {
		this.returnList = returnList;
	}
	@Override
	public void run() {
		try {
			logger.info("线程"+val+"正在运行..");
			returnList.add("T"+val);
		} catch (Exception e) {
			logger.error("线程"+val+"执行异常！",e);
		} finally {
			if (null != countDownLatch) {
				countDownLatch.countDown();
			}
			logger.info("线程"+val+"运行完毕..");
		}
	}

}
