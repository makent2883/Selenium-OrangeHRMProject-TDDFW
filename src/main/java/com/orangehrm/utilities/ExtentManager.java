package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent; // ExtentReports is main class to generate and manage reports
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>(); // for parallel testing for thread safe, we will
																		// create ThreadLocal variable of ExtentTest
																		// class - it defines the test which we are
																		// going to write on our extent reports
	private static Map<Long, WebDriver> driverMap = new HashMap<>();// store threadID(here Long is taken to store
																	// threadID) and webdriver instance as key-value
																	// pair by using HashMap class object

	// Method-2: Initialize Extent Report
	public synchronized static ExtentReports getReporter() { //for thread safety we need to declare as synchronized
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html"; // ExtentReport.html is the report name																														
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);// it will generate the contents in the extent reports, now create ExtentSparkReporte object, earlier we used to use ExtentHtmlReport																			
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports(); // initalzing or creating object to ExtentReports
			extent.attachReporter(spark);
			
			// Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name")); // gives os name where we run the
																						// extent report
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	// Method-3: Start the Test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName); // we need to createtest by calling createTest()
																	// method adn then we need to set the value to start
																	// the test
		test.set(extentTest);
		return extentTest; // so by usign ExtentTest class reference of this startTest() method, we are
							// going to start the Test
	}
	
	//Method-4: End the Test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	//Method-5: Get Current Thread's test
	//like log.info, log.error, we will do by using this test name
	public synchronized static ExtentTest getTest() {
		return test.get(); //to get current threads test
	}
	
	//Method-6: Method to get the name of the current test
	public static String getTestName() { //thsi method we need while generatign the screenshots ok
		//first get currentTest
		ExtentTest currentTest=getTest();
		if(currentTest!=null) {
			return currentTest.getModel().getName();//we want to get the exact name of the test
		}
		else {
			return "No Test is currently active for this Thread";
		}
	}
	
	//Method-7: Log steps
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}
	
	//Method-8: Log step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver,String logMessage,String screenShotMessage) {
		getTest().pass(logMessage);
		//call screenshot method
		attachScreenshot(driver,screenShotMessage);
	}
	
	//Method-15: Log step validation for API Testing, for pass scenario
		public static void logStepValidationForAPI(String logMessage) {
			getTest().pass(logMessage);
	}
		
	//Method-9: Log a Failure
	public static void logFailure(WebDriver driver,String logMessage,String screenShotMessage) {
		String colorMessage=String.format("<span style='color:red;'>%s</span>",logMessage); //here %s is placeholder for string
		getTest().fail(colorMessage);
		//call screenshot method
		attachScreenshot(driver,screenShotMessage);
	}
	
	//Method-14: Log a Failure for API Testing
		public static void logFailureAPI(String logMessage) {
			String colorMessage=String.format("<span style='color:red;'>%s</span>",logMessage); //here %s is placeholder for string
			getTest().fail(colorMessage);		
		}
	
	//Method-10: Log a Skip
	public static void logSkip(String logMessage) {
		String colorMessage=String.format("<span style='color:orange;'>%s</span>",logMessage);
		getTest().skip(colorMessage);
	}
	
	//Method-11: Take a screenshot with date and time in the file
	public synchronized static String takeScreenshot(WebDriver driver,String screenshotName) {
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		//Format date and time for filename
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		//Saving the screenshot to a file
		String destinationPath=System.getProperty("user.dir") + "/src/test/resources/screenshots/"+screenshotName+"_"+timeStamp+".png";
		//copy to new destination file
		File finalPath=new File(destinationPath);
		try {
			FileUtils.copyFile(src, finalPath); //surround it with try-catch
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//convert screenshot to Base64 for embedding in the Report
		String base64Format=convertToBase64(src);
		return base64Format;  //we need to create one utility method,so that this screenshot will be converted into base64Format. 
							//so, whenever we call this takeScreenshot() method, that will be in String format ok, so we wil create another method to convert this into base64format and that method we will call it here ok at line 130 	
	}

	//Method-12: Convert screenshot to Base64 format
	public static String convertToBase64(File screenshotFile) {
		String base64Format="";
		//Read the file content into a byte array (ie we need to create one array, that will read the stringfileformat into string of array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);//this surreound with try-catch
			base64Format=Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//convert the byte array to Base64 string
		return base64Format;
	}
	
	//Method-13: Attach this scrrenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver,String message) { //we need to call thsi method at line 98 and 105
		try {
			String screenshotBase64=takeScreenshot(driver,getTestName());
			getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build()); //surround with try-catch
		} catch (Exception e) {
			getTest().fail("Failed to attach Screenshot"+message);
			e.printStackTrace();
		}
	}
		
	// Method-1: Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
