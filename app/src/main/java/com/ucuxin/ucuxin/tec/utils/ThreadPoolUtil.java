package com.ucuxin.ucuxin.tec.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

	private static ExecutorService pool;
	
	static {
		pool = Executors.newCachedThreadPool();
	}
	
	public static void execute(Runnable command) {
		
		pool.execute(command);
		
	}
	
	
}
