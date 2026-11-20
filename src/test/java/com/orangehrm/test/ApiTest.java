package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest { //this class no need to extend BaseClass, bcs we dont need to initialize webdriver, it will run in backend so

	//step1: Method-1:
	//@Test(retryAnalyzer = RetryAnalyzer.class) //use this retryAnalyzer attribute at @Test level, only after creating RetryAnalyzer class topic ok. and commenting this line, when we apply retry logic at listener level ie at TestListener class.
	@Test
	public void verifyGetUserAPI() { //when we apply retrylogic,then this testcase will be applicable for retry logic 
		//softassertion
		SoftAssert softAssert=new SoftAssert(); //instead of creatign object of softassertion here, we already creating in BaseClass ie getSoftAssert() we can use here, but, since it is API testing and we not extended BaseClass, so we need to create softassertion object like here. ok.
		
		//step1:Define API Endpoint
		String endPoint="https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: "+endPoint);
		
		//step2:Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response=APIUtility.sendGetRequest(endPoint);
		
		//step3:validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid=APIUtility.validateStatusCode(response, 201);//delibarately changing status code from 200 to 201 to check Retry logic ok.
		softAssert.assertTrue(isStatusCodeValid,"Statuc code is not as Expected");
		if(isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		}
		else {
			ExtentManager.logFailureAPI("Statuc Code Validation Failed");
		}
		
		//step4:validate user name (some fields from payload we are validating now)
		ExtentManager.logStep("Validating response body for username");
		String userName=APIUtility.getJsonValue(response, "username"); //"username" we are validating from the response
		boolean isUserNameValid="Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid,"username is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed!");
		}
		else {
			ExtentManager.logFailureAPI("Username Validation Failed");
		}
		
		//step5:validate Email
		ExtentManager.logStep("Validating response body for email");
		String userEmail=APIUtility.getJsonValue(response, "email"); //"username" we are validating from the response
		boolean isEmailValid="Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid,"Email is not valid");
		if(isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed!");
			}
		else {
			ExtentManager.logFailureAPI("Email Validation Failed");
			}		
		//like this you can validate other fields also from response payload like id etc..
			
		softAssert.assertAll();
	}
}
