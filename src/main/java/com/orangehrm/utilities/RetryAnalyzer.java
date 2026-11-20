package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	//step1: class must implement IRetryAnalyzer interface from testng 
	//step2:add unimplemented methods ie retry() of this IRetryAnalyzer interface as below:		
	
	private int retryCount=0; //Number of retries
	public static final int maxRetryCount=2; //Maximum noof retries
	@Override
	public boolean retry(ITestResult result) { //this method Returns true if the test method has to be retried, false otherwise.
		//step3: declare variable retrycount and maxRetryCount refer above and then continue code of this method below
		if(retryCount<maxRetryCount) {
			retryCount++;
			return true; //Retry the test
		}
		return false; //if false, then it wont retry the exeuction ok		
	}
}

//step3: to use/implement this retry logic, we will apply it in  ApiTest testcase ok.
			//so goto ApiTest testcase now from here ok.
//step4: then run “APITesting.xml” file ok.