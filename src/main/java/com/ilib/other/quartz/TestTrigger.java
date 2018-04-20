package com.ilib.other.quartz;

import org.springframework.stereotype.Service;

@Service
public class TestTrigger {

	public void testTimer(){
		System.out.println("定时开始了...");
	}

	public void testTimer0(){
		System.out.println("定时另外个开始了...");
	}
}
