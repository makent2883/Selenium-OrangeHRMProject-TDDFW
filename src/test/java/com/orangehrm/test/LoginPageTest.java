package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{
	 
	private LoginPage loginPage;
	private HomePage homePage;
	
	//we need to initialize those loginpage and homepage by creating objects of those pages with driver and create the object before every testcases method, so use @BeforeMethod
	@BeforeMethod
	public void setUpPages() {
	loginPage=new LoginPage(getDriver());  //driver we are getting from BaseClass getDriver() method ok
	homePage=new HomePage(getDriver()); //here we created obejcts of loginpage and homepage - within this driver instance is getting created mroe than once(you can see in Loginpage class constructor and Homepage class constructor where ActionDriver instance is getting created 2 times, one for LoginPage initialization purpose and another for HomePage initialization purpose, so to avoid this problem, we will got with Signleton design pattern approach 		
	}
	
/*	we created these verifyValidLoginTest() and invalidLoginTest() method without using DataProviders ie with hardcoded values
	so, we will keep comment to these 2 methods 
	
	//testmethod now
	@Test
	public void verifyValidLoginTest() {
	//	ExtentManager.startTest("Valid Login Test"); //when we discuss Listeners topic, we can remove this line ok bcs it will take care, but right now, we have call explicitly
			//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
		System.out.println("Running testMethod1 on thread: "+ Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering Username and Password");
		loginPage.login("Admin", "admin123");
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin Tab should be visible after successfull login "); 
		ExtentManager.logStep("Validation Successfull");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully!");
		staticWait(2); //staticWait() is from BaseClass
	}
	 
	@Test
	public void invalidLoginTest() {
	//	ExtentManager.startTest("In valid Login Test"); 
			//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
		System.out.println("Running testMethod2 on thread: "+ Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering Username and Password");
		loginPage.login("admin", "admin"); //wrong pwd is entered here
		String expectedErrorMessage="Invalid credentials1"; //wanted given wrong error msg to get fail ok
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid Error Message");	
		ExtentManager.logStep("Validation Successfull");
		ExtentManager.logStep("Logged out Successfully!");
	} //now, run this LoginPageTest by clicking Run option on this invalidLoginTest() method, firsttime with correct errormsg adn then secondtime with incorrect errormsg in expectedErrorMessage ok
	
	*/
	
	//we are creating these verifyValidLoginTest() and invalidLoginTest() method with using DataProviders concept refer as below
	//testmethod now
		@Test(dataProvider="validLoginData",dataProviderClass=DataProviders.class)   
		public void verifyValidLoginTest(String username, String password) {
		//	ExtentManager.startTest("Valid Login Test"); //when we discuss Listeners topic, we can remove this line ok bcs it will take care, but right now, we have call explicitly
				//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
			System.out.println("Running testMethod1 on thread: "+ Thread.currentThread().getId());
			ExtentManager.logStep("Navigating to Login Page entering Username and Password");
			loginPage.login(username, password);
			ExtentManager.logStep("Verifying Admin tab is visible or not");
			Assert.assertTrue(homePage.isAdminTabVisible(),"Admin Tab should be visible after successfull login "); 
			ExtentManager.logStep("Validation Successfull");
			homePage.logout();
			ExtentManager.logStep("Logged out Successfully!");
			staticWait(2); //staticWait() is from BaseClass
		}
		 
		@Test(dataProvider="inValidLoginData",dataProviderClass=DataProviders.class)
		public void invalidLoginTest(String username, String password) {
		//	ExtentManager.startTest("In valid Login Test"); 
				//we are commenting this above startTest() method, since we implemented this in TestListener class in onTestStart() method ok,so refer there
			System.out.println("Running testMethod2 on thread: "+ Thread.currentThread().getId());
			ExtentManager.logStep("Navigating to Login Page entering Username and Password");
			loginPage.login(username, password); //wrong pwd is entered here
			String expectedErrorMessage="Invalid credentials1"; //wanted given wrong error msg to get fail ok
			Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid Error Message");	
			ExtentManager.logStep("Validation Successfull");
			ExtentManager.logStep("Logged out Successfully!");
		} //now, run this LoginPageTest by clicking Run option on this invalidLoginTest() method, firsttime with correct errormsg adn then secondtime with incorrect errormsg in expectedErrorMessage ok
	
}	
