package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {
	@Test
	public void dummyTest() {
		//just checking title
	//	String title=driver.getTitle(); //using ThreadLocal driver instead of WebDriver directly for parallel teting purpose.
	//	ExtentManager.startTest("DummyTest2 Test"); 
			//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
		String title=getDriver().getTitle();
		ExtentManager.logStep("Verifying the Title");
		assert title.equals("OrangeHRM"):"Test Failed - Title is Not Matching";
		
		System.out.println("Test Passed - Title is Matching");	
		ExtentManager.logStep("Validation Successfull");
		 
	}
}
