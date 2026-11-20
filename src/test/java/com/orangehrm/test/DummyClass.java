package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass{

	@Test
	public void dummyTest() {
		//just checking title
	//	String title=driver.getTitle(); //using ThreadLocal driver instead of WebDriver directly for parallel teting purpose.
	//	ExtentManager.startTest("DummyTest1 Test"); //after creating ExtentManager class,then write this line here
			//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
		String title=getDriver().getTitle(); 
		ExtentManager.logStep("Verifying the Title");
		assert title.equals("OrangeHRM"):"Test Failed - Title is Not Matching";
		
		System.out.println("Test Passed - Title is Matching");
	//	ExtentManager.logSkip("This case is skipped"); //we can comment this line, as we are implemeting this skip test in TestListener class ok.
		throw new SkipException("Skipping the test as part of Testing."); //wantedly writing this skip exception, after writing ExtentManager ok
				
	}

}
