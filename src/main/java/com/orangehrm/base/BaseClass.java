package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	// we needto create some variables here like below
	// why protected means, this can be used within the same pacakge and also in
	// other package if that class will extends this class ok ie we can access in
	// its child classes if they extend this class ok
	protected static Properties prop; // static bcs-the instance ie prop wil be same throughout n noof classes
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver; // creating this for singleton design pattern approach purpose ok
	
	private static ThreadLocal<WebDriver> driver=new ThreadLocal<>(); //for parallel testing using ThreadLocal purpose
 	private static ThreadLocal<ActionDriver> actionDriver=new ThreadLocal<>();

 	public static final Logger logger=LoggerManager.getLogger(BaseClass.class); //for logging purpose initializing Logger class instance
 	
 	//ThreadLocal for SoftAssert - this softassertin topic is explaned during database testing ie in "DBVerifcationTest.java" class
 	//protected ThreadLocal<SoftAssert> softAssert=new ThreadLocal<>(); //we can create like this or like below, he used below one, so commenting to this line, refer below line ok
 	protected ThreadLocal<SoftAssert> softAssert=ThreadLocal.withInitial(SoftAssert::new); //it creates SoftAssert object only when it is accessed for the first time. so we can use this "withInitial()" method and later on we can create one getter() method(as below shown) to use that object ok.
 																	//so, inside withInitial() method we need to give one lambda expression ie withInitial(SoftAssert::new);
 	//getter method to access this softassert object outside of other classes
 	public SoftAssert getSoftAssert() {
 		return softAssert.get();
 	} //now, we can call this getSoftAssert() method inside "DBVerifcationTest.java" class ok. so now goto this DBVerifcationTest class.
	
	@BeforeSuite // bcs we are going to load this before running the suite(in testng.xml file ok)
	public void loadConfig() throws IOException { // adn this prop is initializing only once irrespective of n noof
													// testcases, so made prop as static and in beforesuite ok and prop
													// will carry the same value
		// load the configuration file
		prop = new Properties();
		// now we need to read the file for that fis
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		// now we have to load the file
		prop.load(fis);
		logger.info("config.properties file loaded"); //for loggging purpose
		
		//start the Extent Report  //after completing ExtentManager class, come to this BaseClass here and call it
		//ExtentManager.getReporter(); //commenting this line, because we are calling this getReporter() method in TestListener class inside onStart() method ok. so before writing code of TestListner class, we used this method here in this BaseClass while we create ExtentManager class ok.
	}

	@BeforeMethod
	public synchronized void setup() throws IOException { //synchrnized why bcs, only one thread can use this method at a time, even though we run the script in parallel mode
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2); // 2 seconds wait after configurebrowser ok
		logger.info("WebDriver initialized and Browser Maximized");
		logger.trace("This is Trace Message"); //to see whether we will get all the msgs or not
		logger.error("This is Error Message");
		logger.debug("This is Debug Message");
		logger.fatal("This is Fatal Message");
		logger.warn("This is Warn Message");
		/*  keeping this comment and rewriting below by using ThreadLocal reference of actionDriver ok for parallel teting purpose
		// initialize the actionDriver only once
		if (actionDriver == null) {
			actionDriver = new ActionDriver(driver);
			//System.out.println("ActionDriver instance is created. ");instead of sop, now we can use logger inplace of sop stmts ok
			logger.info("ActionDriver instance is created. "+Thread.currentThread().getId()); //this will print the ThreadID ok
		} */
		
		//Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread: "+Thread.currentThread().getId());
		
	}// close of setup()

	// Initialise the webdriver based on the browser defined in config.properties
	// file 
	public synchronized void launchBrowser() {//synchrnized why bcs,so that,only one thread can use this method at a time, even though we run the script in parallel mode, 
		String browser = prop.getProperty("browser"); // with this we got the browser from config file
		if (browser.equalsIgnoreCase("chrome")) {
			// now we will use selenium new version ie 4 version feature
			// with this new feature ie webdrivermanager, no need to explicitly give the
			// driver path,au
			// it will automatically download the driver and instantiate the driver and
			// starts the session
			// driver=new ChromeDriver();
			
			//  Automatically download and set up the matching ChromeDriver
			WebDriverManager.chromedriver().setup();
			
			//creating chromeoptions for chrome browser
			//because when we work with CI/CD with jenkins environment, we mostly run the script in headless mode. so that's why i added these options to browsers
			ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); //Run chrome in headless mode
            options.addArguments("--disable-gpu"); //Disable gpu rendering for headless mode
          //  options.addArguments("--window-size=1920,1080"); //set window size
            options.addArguments("--disable-notifications");//disable browser notifications
            options.addArguments("--no-sandbox"); //Required for some CI environments like
            options.addArguments("--disable-dev-shm-usage");//resolve issues in resource-limited
            //note: you can remove headlesss options or any other options from above keep comment to those which you dont want ok
		
            //	driver = new ChromeDriver(); //byusing ThreadLocal driver we need to create object of browserdrivers ok for parallel testing purpose
			driver.set(new ChromeDriver());
			
			ExtentManager.registerDriver(getDriver()); //write this line after creating ExtentManager class ok and getDriver() is from BaseClass only ok
			logger.info("ChromeDriver Instance is created.");
		} 
		else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			
			//creating FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); //run firefox in headless mode
			options.addArguments("--disable-gpu"); //Disable gpu rendering for headless mode
	        options.addArguments("--width=1920"); //set browser width
	        options.addArguments("--height=1080"); //set browser height
	        options.addArguments("--disable-notifications");//disable browser notifications
            options.addArguments("--no-sandbox"); //Required for some CI/CD environments 
            options.addArguments("--disable-dev-shm-usage");//prevent crashes in low-resource    
						
		//	driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created.");
		}
		else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			
			//creating EdgeOptions
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); //run edge in headless mode
			options.addArguments("--disable-gpu"); //Disable gpu accelaration
	        options.addArguments("--window-size=1920,1080"); //set window size
	        options.addArguments("--disable-notifications");//disable pop-up notifications
            options.addArguments("--no-sandbox"); //Required for some CI/CD environments 
            options.addArguments("--disable-dev-shm-usage");//prevent crashes in low-     
								
		//	driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created.");
		} 
		else if (browser.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
		//	driver = new InternetExplorerDriver();
			driver.set(new InternetExplorerDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("IEDriver Instance is created.");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

//configure browser settings such as implicitwait, maximize browser and navigate to url
	public void configureBrowser() {
		// Implicit Wait - for dynamic elementse if we have, for them we can use
		// explicit waits ok
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait")); // we are getting implicitwait from
																				// config file and it is a string value,
																				// so we need to convert into integer
																				// value ok
	//	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); rewritting this as below by usign ThreadLocal driver instead of WeBDriver driver directly using getDriver() method
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		// maximize the driver
	//	driver.manage().window().maximize();
		getDriver().manage().window().maximize();
		
		// Navigatae to url, keep in try-catch - select line 78 and do right click and
		// select- surround with try/catch option ok
		try {
	//		driver.get(prop.getProperty("url"));
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL:" + e.getMessage());
		}

		// now to test this clśass working ornot, i will create a dummy test class
		// inside srs/test/java ie "DummyClass"
		// goto "DummyClass" class now.

		// before going to Dummyclass, write tearDown() method as below and then mention
		// this method as @BeforeMethod and make this tearDown() mehtod as @AfterMethod
	}

	@AfterMethod
	public synchronized void tearDown() { //here also use synchronized kw, while closing the driver
	//	if (driver != null) { //use getDriver() method instead of driver for ThreadLocal 
		if(getDriver()!=null) {
			try {
			//	driver.quit(); //use getDriver() method instead of driver for ThreadLocal
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
			// quit will close all the browsers and close the webdriver session and close
			// will close only active window,it wont close the webdriver instance.
		}
		//System.out.println("WebDriver instance is closed."); inplace of sop write logger as below
		logger.info("WebDriver instance is closed."); 
			
	//	driver = null; // once we execute testcases, then we will put driver=null and actionDriver also // null
	//	actionDriver = null;
		//instead of making driver and actionDriver=null, we can use driver.remove() method,so commenting above 2 lines and reqriting as below. so that once we quit driver, then simply we will remove the driver objects ok as below
		driver.remove();
		actionDriver.remove(); 
	//	ExtentManager.endTest(); //after each test we call endTest to flush or to end the report. and write this line after creaing ExtentManager class ok
			//we are commenting this endTest() method because we are using this method in TestListener class inside onFinish() method. so, if you have written TestListener class, then we have to comment this line, as we are using this method in TestListener class instead of in BaseClass ok.
	}

	// static wait for pause
	public void staticWait(int seconds) { // browser is closing immediately right, for that to handle, we are writing
											// this staticWait
		// Thread.sleep() -> will work with milliseconds
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); // this will wait for nanoseconds ok
	}

	/*
	 * // now to access this driver and prop outside of all classes, we need to use
	 * // getter and setter methods - ie concept of encapsulation 
	 * // Getter method for Driver 
	 * public WebDriver getDriver() { 
	 * return driver;
	 *  }
	 */
	 // Getter method for prop
	public static Properties getProp() {
		return prop; 
	}
	 
	// rewritting these getter methods as below: for the purpose of singleton design
	// pattern ok
	// Getter method for WebDriver
	public static WebDriver getDriver() {
	//	if (driver == null) { // rewritting this if as below, to use ThreadLocal driver instead of WebDriver driver for parallel testing purpose
		if(driver.get()==null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get(); // if it is initialized then it will return driver ok
	}

	// Getter method for ActionDriver
	public static ActionDriver getActionDriver() {
	//	if (actionDriver == null) { // rewritting this if as below, to use ThreadLocal driver instead of WebDriver driver for parallel testing purpose
		if(actionDriver.get()==null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get(); // if it is initialized then it will return driver ok
	}
	/*
	// Driver setter method
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	} */
	//in setDriver() also, we need to change WebDriver to ThreadLocal ok as below, so commenting above setDriver() method to use this method ok
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	
	
	

	
}// close of class
//now, configure testng.xml file, so goto testng.xml file ok
