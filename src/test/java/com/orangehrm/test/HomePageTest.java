package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;

	// we need to initialize those loginpage and homepage by creating objects of
	// those pages with driver and create the object before every testcases method,
	// so use @BeforeMethod
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver()); // driver we are getting from BaseClass getDriver() method ok
		homePage = new HomePage(getDriver());
	}

	/*	we created these verifyOrangeHRMLogo() method without using DataProviders ie with hardcoded values
	so, we will keep comment to this method 
	
	// testmethod now
	@Test
	public void verifyOrangeHRMLogo() {
	//	ExtentManager.startTest("Home Page Verify Logo Test"); 
			//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
		ExtentManager.logStep("Navigating to Login Page entering Username and Password");
		loginPage.login("Admin","admin123");
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visible");
		ExtentManager.logStep("Validation Successfull");
		ExtentManager.logStep("Logged out Successfully!");
	} //now run this test clicking Run option ok
	
	*/
	
	//we are creating these verifyValidLoginTest() and invalidLoginTest() method with using DataProviders concept refer as below
	//testmethod now
	@Test(dataProvider="validLoginData",dataProviderClass=DataProviders.class)
	public void verifyOrangeHRMLogo(String username,String password) {
		//	ExtentManager.startTest("Home Page Verify Logo Test"); 
				//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
			ExtentManager.logStep("Navigating to Login Page entering Username and Password");
			loginPage.login(username,password);
			ExtentManager.logStep("Verifying Logo is visible or not");
			Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visible");
			ExtentManager.logStep("Validation Successfull");
			ExtentManager.logStep("Logged out Successfully!");
		} //now run this test clicking Run option ok
}
