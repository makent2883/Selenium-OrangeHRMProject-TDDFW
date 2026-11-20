package com.orangehrm.test;

import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{
	//step1:
	private LoginPage loginPage; 
	private HomePage homePage;
	//step2:
	//we need to initialize those loginpage and homepage by creating objects of those pages with driver and create the object before every testcases method, so use @BeforeMethod
	@BeforeMethod
	public void setUpPages() {
		loginPage=new LoginPage(getDriver());  //driver we are getting from BaseClass getDriver() method ok
		homePage=new HomePage(getDriver()); //here we created obejcts of loginpage and homepage - within this driver instance is getting created mroe than once(you can see in Loginpage class constructor and Homepage class constructor where ActionDriver instance is getting created 2 times, one for LoginPage initialization purpose and another for HomePage initialization purpose, so to avoid this problem, we will got with Signleton design pattern approach 		
	}
	
	//step3:
	@Test(dataProvider="dbEmpVerification",dataProviderClass=DataProviders.class)
	public void verifyEmpNameVerificationFromDB(String empID,String empName) {
		
		//SoftAssertion
		//SoftAssert softAssert=new SoftAssert(); //this is not threadsafe, so we created this object using ThreadLocal, so refer in Baseclass for this
		SoftAssert softAssert=getSoftAssert(); //this is created by ThreadLocal,so threadsafe ok.
		
		ExtentManager.logStep("Logging with Admin Credentials");
		loginPage.login("Admin", "MakAdmin@123"); //hardcoding here later we can avoid it ok
		
		ExtentManager.logStep("Click on PIM tab");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee");
	//	homePage.employeeSearch("Arsh"); //we will hardcode it now,later we will see dynamically
		homePage.employeeSearch(empName); //here providing empname through dataprovider concept ok
		
		ExtentManager.logStep("Get the Employee Name from DB");
		//String employee_id="2"; //here also we are hardcoding the value, later we wil do with dataprovider		
		String employee_id=empID; //here providing empid through dataprovider concept ok
		
		//Fetch the data into Map
		Map<String,String> empDetails=DBConnection.getEmpDetails(employee_id);
		String empFirstName=empDetails.get("firstName"); //from DBConnection class we have these keys right ie firstName, which are stored in a Map
		String empMiddleName=empDetails.get("middletName");
		String empLastName=empDetails.get("lastName");
		String empFirstAndMiddleName=(empFirstName+" "+empMiddleName).trim();//firstname and middlename is coming at once in UI, so we will concatenate them and in case,if there is no middlename then we will trim() the space ok
		
		//validation for first name and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
	//	Assert.assertTrue(homePage.verifyEmpFirstAndMiddleName(empFirstAndMiddleName),"First and Middle name are not Matching"); //this is hard assertion ok
		softAssert.assertTrue(homePage.verifyEmpFirstAndMiddleName(empFirstAndMiddleName),"First and Middle name are not Matching"); //this is soft assertion ok
		
		//valiatiion for last name
		ExtentManager.logStep("Verify the employee last name");
	//	Assert.assertTrue(homePage.verifyEmpLastName(empLastName)); 
		softAssert.assertTrue(homePage.verifyEmpLastName(empLastName)); 
		
		ExtentManager.logStep("DB Validation Completed");	
		
		softAssert.assertAll();
	}
	
}
/*for Soft Assertion
 * 	we will try to fail this - vlidation of firstname and middle name and then will see with softassertion will it continue its execution with "validation for last name" ok
 *  for this, i will just add "Test" instead of blank space, between empFirstName and empMiddleName at line 50 ok. like below
 *  String empFirstAndMiddleName=(empFirstName+"Test"+empMiddleName).trim(); 
 * and now run this "DBTesting.xml" file 
 * then with HardAssertion, if this vlidation of firstname and middle name  fails, then it wont continue its execution fo remaining stmts. so this "validation for last name" wont get execute.
 *so, we have to use Soft Assertion in our above script. 
 *so created SoftAssertion object iin this above script. 
 * But the problem with SoftAssertion is -
 * 		it is not thread safe, every time we need to create object of this softassert here.
 * 	so, what we will do is, 
 * 		we can create ThreadLocal object of SoftAssert in our BaseClass and we can utilize that.
 * so, goto BaseClass now from here -
 * 		in BaseClass, create ThreadLocal object of SoftAssert, refer in BaseClass.
 * after creating SoftAssert object usigin ThreadLocal and getSoftAssert() method, come back to this class and then call that method ie getSoftAssert() at line 35
 * 	and use that softAssert object at lines whereever we have HardAssert, put this softAssert object ok or keep comment to hardassert line ok.
 * and at last line of that method, write  	softAssert.assertAll(); ok
 * 
 * after keeping softasertion in this class, now run DBTesting.xml file to see its result ok.
 * 
 * 
 */
