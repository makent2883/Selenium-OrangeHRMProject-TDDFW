package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener,IAnnotationTransformer{
//step1: this class must implements ITestListener interface
//step2: now we need to override unimplemented methods of this ITestListener interface, by right click here-source - override/implement methods option select
//step3: lets start with onStart() method code ok, so refer in onStart() method		
//step4: refer in onFinish() method
//step5: refer in onTestStart() method 
//step6: refer in onTestSuccess() method
//step7: refer in onTestFailure() method
//step8: refer in onTestSkipped() method
/*step9: use IAnnotationTransformer interface to implement RetryLogic. use this interface here, only when discussing Retrylogic topic ok. and it will modify @Test annotation at run time. it work same as when we applied at @Test level in ApiTest class as @Test(retryAnalyzer = RetryAnalyzer.class), now we are trying at this Listener level ok.
 *		and we need to override this interface methods ie transform(ITestAnnotation,Class,Constructor,Method) method refer at end of this class.  
 */
	//Step-5: This method will triggers when a test starts
	@Override
	public void onTestStart(ITestResult result) { //ITestResult is a class which describes the result of a test
		String testName=result.getMethod().getMethodName(); //first we will get the testname by using getMethodName()
		//and when we start the test, we will call ExtentManager startTest() method
		ExtentManager.startTest(testName);  //it will start logging in Extent Reports
		ExtentManager.logStep("Test Started: "+testName); 
			//and since we written startTest() method here, we will comment this startTest() method written in all Testclasses ie in DummyClass, DummyClass2, LoginPageTest, HomePageTest classes ok. so refer in those classes i kept comment to startTest() method ok.
	}

	//Step-6: This method will triggers when a test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getMethod().getMethodName(); 	
		
		//keeping condition for API Testing here(use this if-else condition when you start API testing class ok, otherwise, before API classes, no need to keep this condition and just keep only line 37 ok)
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {	//if testname not contains API then execute below line otherwise goto else part		
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!", "Test End: "+ testName +" - ✓ Test Passed"); //This tick mark is made in notepad adn then copied this entire text alogn with tickmark ok
		}
		else {
			ExtentManager.logStepValidationForAPI("Test End: "+ testName +" - ✓ Test Passed");
		}
	}

	//Step-7: This method will triggers when a test fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName(); 
		String failureMessage=result.getThrowable().getMessage(); //this will give failure message
		ExtentManager.logStep(failureMessage); //adn we will log this failure step by logStep ok	
		//keeping condition for API Testing here(use this if-else condition when you start API testing class ok, otherwise, before API classes, no need to keep this condition and just keep after logStep() keep logFailure() directly ok)
		if(!result.getTestClass().getName().toLowerCase().contains("api")) { //our class name is APITest,so given contains as "api"
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End: "+ testName +" - ❌ Test Failed");
		}
		else {
		ExtentManager.logFailureAPI("Test End: "+ testName +" - ❌ Test Failed");
		}
	}

	//Step-8: This method will triggers when a test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName=result.getMethod().getMethodName(); 
		ExtentManager.logSkip("Test Skipped "+testName);
	}

	//Step-3: This method will triggers when a suite starts
	@Override
	public void onStart(ITestContext context) {
		//Initialize the Extent Reports
		ExtentManager.getReporter(); //when suite starts, this onStart() method will triggers this getReporter() method ok
			//and since we are calling this getReporter() from Listeners now, so we will comment this getReporter() method in BaseClass under @BeforeSuite annotation, as we already givem in BaseClass previously na, so we will comment that line and now we will call this getReporter() method from here ok.
	}

	//Step-4: This method will triggers when suite ends
	@Override
	public void onFinish(ITestContext context) {
		//Flush the Extent Reports
		ExtentManager.endTest(); //and this endTest() method is already called in BaseClass, so we will comment this endTest() methd line in BaseClass inside tearDown() method and we will use it here ok
			//so when we start testng.xml file, then automatically our reports gets initialize using getReporter() mehtod in onStart() method and once it is done, then it will automatically end the suite in onFinish() method ok
	}

	//step9: transform() method for retry logic ok
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
	
}
