package com.gautham.multithreading;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		Thread.sleep(1000);
		Integer random = (int) (Math.random()*100);
		return random;
	}

}
