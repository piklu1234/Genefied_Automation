package com.genefied.Utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	
	private int retryCount = 0;
    private static final int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {
		
//		Throwable cause = result.getThrowable();
//		if (cause instanceof StaleElementReferenceException || cause instanceof NoSuchElementException) {
//            System.out.println("Test failed due to " + cause.getClass().getSimpleName() + ". Not retrying.");
//            return false;
//        }
		
		if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
		return false;
	}

}
