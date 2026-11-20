package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait; // for explicit wait purpose
	public static final Logger logger=BaseClass.logger; //to use logger object of BaseClass in ActionDriver class, we need to write like this ie BaseClass.logger ok

	// to initialize these 2 variables ie driver, wait, we have to use constructor
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait=Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));  //Duration.ofSeconds(30)); // this time 30seconds we can pass it from config // properties later ok.																		
		//System.out.println("WebDriver instance is created.."); instead of sop, use logger as below
		logger.info("WebDriver instance is created..");
	}

	// now methods

	// Method-3 => Method to click an element
	public void click(By by) {
		String elementDescription=getElementDescription(by);
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by); // if it is clickable then we will go for click operation ok
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element:"+elementDescription); //after creating ExtentManager class,then call this method here, when we click any element, then it will log this step in extent report.
			logger.info("clicked an element-->"+elementDescription);
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("Unable to click element: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element: ", elementDescription+"_Unable to click");
			logger.error("Unable to click element");
		}
	}

	// Method-4 => Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by); // onceele is visible then only we will go with clear and insertion
			applyBorder(by,"green"); 
			//driver.findElement(by).clear();
			//driver.findElement(by).sendKeys(value);  //instead of creating 2 times using by, we can do like this below line
			WebElement element=driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on "+getElementDescription(by)+"-->"+value);
		} catch (Exception e) {
			applyBorder(by,"red");
			//System.out.println("Unable to enter the value: " + e.getMessage()); inplace of sop use logger
			logger.error("Unable to enter the value: " + e.getMessage());
		}
	}

	// Method-5 => Method to get text from an input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);	
			applyBorder(by,"green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by,"red");
			//System.out.println("Unable to get the text: " + e.getMessage()); inplace of sop use logger
			logger.error("Unable to get the text: " + e.getMessage());
			return "";
		}
	}

	// Method-6 => Method to compare two texts
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by,"green");
			//	System.out.println("Texts are Matching: " + actualText + " equals " + expectedText);
				logger.info("Texts are Matching: " + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text Verified Successfully! "+actualText+" equals "+expectedText);
				return true; //if texts are matching
			} else {
				applyBorder(by,"red");
			//	System.out.println("Texts are are Matching: " + actualText + " not equals " + expectedText);
				logger.error("Texts are are Matching: " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!", "Text Comparison Failed! "+actualText+" not equals "+expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by,"red");
			//System.out.println("Unable to compare Texts: " + e.getMessage());
			logger.error("Unable to compare Texts: " + e.getMessage());
		}
		return false;
	}

	/*
	// Method-7 => Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			boolean isDisplayed = driver.findElement(by).isDisplayed();
			if (isDisplayed) {
				System.out.println("Element is visible");
				return isDisplayed;
			} else {
				return isDisplayed;
			}
		} catch (Exception e) {
			System.out.println("Element is not displayed: "+e.getMessage());
			return false;
		}
	} This method i am rewriting like below, as it has so many lines so
	*/
	
	//Method-7 => Simplified method adn removed redundant conditions
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			logger.info("Element is displayed "+getElementDescription(by));
			ExtentManager.logStep("Element is displayed: "+getElementDescription(by)); //after creatign ExtentManager class then only add this line ok
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
			return driver.findElement(by).isDisplayed();	
		} catch (Exception e) {
			applyBorder(by,"red");
			//System.out.println("Element is not displayed: "+e.getMessage());
			logger.error("Element is not displayed: "+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: ","Element is not dispalyed: "+getElementDescription(by));
			return false;
		}
	}
	
	//Method-8 => Method to Scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by,"green");
			JavascriptExecutor js=(JavascriptExecutor) driver;
			WebElement element=driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by,"red");
			//System.out.println("Unable to locate element: "+e.getMessage());
			logger.error("Unable to locate element: "+e.getMessage());
		}
	}
	
	//Method-9 => Method to wait for page load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor)WebDriver)
					.executeScript("return document.readyState").equals("complete")); //if readyState equals to complete, there are different states fo webpage like interactive or loading, so if incase of complete-then we will say page loaded successfully otherwiser page didint loaded within the given timeout 
			//System.out.println("Page loaded successfully.");
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			//System.out.println("Page did not load within "+timeOutInSec+" seconds. Exception: "+e.getMessage());
			logger.error("Page did not load within "+timeOutInSec+" seconds. Exception: "+e.getMessage());
		}
	}
	
	// Method-1 => wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));// adn susrround this line with try-catch, to handle
																	// the error properly
		} catch (Exception e) {
			//System.out.println("Element is not clickable: " + e.getMessage());
			logger.error("Element is not clickable: " + e.getMessage());
		}
	}

	// Method-2 => wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			//System.out.println("Element is not visible: " + e.getMessage());
			logger.error("Element is not visible: " + e.getMessage());
		}
	}
	
	//Method-10 => Method to get the description of an element using By locator and then we will use this Unable to describe the method inside our ActionDriver class ok
	public String getElementDescription(By locator) {
		//Check for null driver or null locator to avoid NullPointerException
		if(driver==null) {
			return "Driver is not initialized.";
		}	
		if(locator==null) {
			return "Locator is null.";
		}	
		try {
			//Find the element using the locator
			WebElement element=driver.findElement(locator);
			
			//Get Element Description ie element attributes
			String name=element.getDomProperty("name");   //getAttribute(null) ->this is deprecated and we have getDomAttribute()
			String id=element.getDomProperty("id");
			String text=element.getText();
			String className=element.getDomProperty("class");
			String placeHolder=element.getDomProperty("placeholder");
			
			//Return the description based on element attributes
			/*if all these attributes are not empty and not null then we will return
			 * so, to check for empty and not null for all these attributes, we will create another method isNotEmpty as below 
			 */
			if(isNotEmpty(name)) { //means, if name is not empty or not null, then return as below
				return "Element with name:" + name;
			}
			else if(isNotEmpty(id)) {
				return "Element with id:"+ id;
			}
			else if(isNotEmpty(text)) {  //if the text is long, we can truncate it right, so to truncate long text purpose, we will wirte another method truncate() as below
				return "Element with text:"+ truncate(text,50); 
			}
			else if(isNotEmpty(className)) {
				return "Element with class:"+ className;
			}
			else if(isNotEmpty(placeHolder)) {
				return "Element with placeholder:"+ placeHolder;
			} 
			else {
				return "Element located using: " + locator.toString();
			}
			//and now keep this from 177 in try-catch without using last else ok
		} catch (Exception e) {
			// Log exception for debugging
			e.printStackTrace(); // Replace with a logger in a real-world scenario
			return "Unable to describe element due to error: " + e.getMessage();
		}
		//return "Unable to describe the element";
	}
	
	//Utility method to check a String is not null or empty and use this in getElementDescription() method
	private boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();  //means if vlaue is not null and not empty and then it will return based on this, and we will use this method inside this getElementDescription() method 
	}
	
	//Utility method to truncate long string and use this in getElementDescription() method
	private String truncate(String value, int maxLength) { //upto our given maxlength we will truncate it ok
		if(value==null || value.length()<=maxLength){
			return value;
		}
		return value.substring(0,maxLength)+"...";
	}
	
	//Utility method to Border an element
	public void applyBorder(By by, String color) {
		try {
			//Locate the element
			WebElement element=driver.findElement(by);
			//Apply the border - use javascript to apply the border
			String script="arguments[0].style.border='3px solid "+color+"'";
			//use javascriptexecutor to execute this script
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript(script,element);
			logger.info("Applied the border with color "+color+" to element "+getElementDescription(by));  //then surround this in try-catch ok
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: "+getElementDescription(by),e);
		}
	} //now we will call this applyBorder() method in all the Action related  methods inside this ActionDriver class ok.
	
 // ===================== Select Methods =====================
	
    // Method to select a dropdown by visible text
    public void selectByVisibleText(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByVisibleText(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown value: " + value, e);
        }
    }
    
    // Method to select a dropdown by value
    public void selectByValue(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByValue(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by actual value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown by value: " + value, e);
        }
    }
    
    // Method to select a dropdown by index
    public void selectByIndex(By by, int index) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByIndex(index);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by index: " + index);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown by index: " + index, e);
        }
    }
    
 // Method to get all options from a dropdown
    public List<String> getDropdownOptions(By by) {
        List<String> optionsList = new ArrayList<>();
        try {
            WebElement dropdownElement = driver.findElement(by);
            Select select = new Select(dropdownElement);
            for (WebElement option : select.getOptions()) {
                optionsList.add(option.getText());
            }
            applyBorder(by, "green");
            logger.info("Retrieved dropdown options for " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to get dropdown options: " + e.getMessage());
        }
        return optionsList;
    }

    
    // ===================== JavaScript Utility Methods =====================
    
    // Method to click using JavaScript
    public void clickUsingJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            applyBorder(by, "green");
            logger.info("Clicked element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to click using JavaScript", e);
        }
    }
    
    // Method to scroll to the bottom of the page
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        logger.info("Scrolled to the bottom of the page.");
    }
    
    // Method to highlight an element using JavaScript
    public void highlightElementJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
            logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to highlight element using JavaScript", e);
        }
    }
    
    // ===================== Window and Frame Handling =====================
    
    // Method to switch between browser windows
    public void switchToWindow(String windowTitle) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(windowTitle)) {
                    logger.info("Switched to window: " + windowTitle);
                    return;
                }
            }
            logger.warn("Window with title " + windowTitle + " not found.");
        } catch (Exception e) {
            logger.error("Unable to switch window", e);
        }
    }
    
    // Method to switch to an iframe
    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
            logger.info("Switched to iframe: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to switch to iframe", e);
        }
    }
    
    // Method to switch back to the default content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.info("Switched back to default content.");
    }
    
    // ===================== Alert Handling =====================
    
    // Method to accept an alert popup
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            logger.info("Alert accepted.");
        } catch (Exception e) {
            logger.error("No alert found to accept", e);
        }
    }
    
    // Method to dismiss an alert popup
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            logger.info("Alert dismissed.");
        } catch (Exception e) {
            logger.error("No alert found to dismiss", e);
        }
    }
    
    // Method to get alert text
    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("No alert text found", e);
            return "";
        }
    }
    
    // ===================== Browser Actions =====================
    
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            ExtentManager.logStep("Page refreshed successfully.");
            logger.info("Page refreshed successfully.");
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
            logger.error("Unable to refresh page: " + e.getMessage());
        }
    }

    public String getCurrentURL() {
        try {
            String url = driver.getCurrentUrl();
            ExtentManager.logStep("Current URL fetched: " + url);
            logger.info("Current URL fetched: " + url);
            return url;
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
            logger.error("Unable to fetch current URL: " + e.getMessage());
            return null;
        }
    }

    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            ExtentManager.logStep("Browser window maximized.");
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "maximize_window_failed");
            logger.error("Unable to maximize window: " + e.getMessage());
        }
    }
    
 // ===================== Advanced WebElement Actions =====================

    public void moveToElement(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(by)).perform();
            ExtentManager.logStep("Moved to element: " + elementDescription);
            logger.info("Moved to element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element", elementDescription + "_move_failed");
            logger.error("Unable to move to element: " + e.getMessage());
        }
    }

    public void dragAndDrop(By source, By target) {
        String sourceDescription = getElementDescription(source);
        String targetDescription = getElementDescription(target);
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
            ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
            logger.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop", sourceDescription + "_drag_failed");
            logger.error("Unable to drag and drop: " + e.getMessage());
        }
    }

    public void doubleClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Double-clicked on element: " + elementDescription);
            logger.info("Double-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element", elementDescription + "_doubleclick_failed");
            logger.error("Unable to double-click element: " + e.getMessage());
        }
    }

    public void rightClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Right-clicked on element: " + elementDescription);
            logger.info("Right-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element", elementDescription + "_rightclick_failed");
            logger.error("Unable to right-click element: " + e.getMessage());
        }
    }

    public void sendKeysWithActions(By by, String value) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(driver.findElement(by), value).perform();
            ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
            logger.info("Sent keys to element --> " + elementDescription + " | Value: " + value);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys", elementDescription + "_sendkeys_failed");
            logger.error("Unable to send keys to element: " + e.getMessage());
        }
    }

    public void clearText(By by) {
        String elementDescription = getElementDescription(by);
        try {
            driver.findElement(by).clear();
            ExtentManager.logStep("Cleared text in element: " + elementDescription);
            logger.info("Cleared text in element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text", elementDescription + "_clear_failed");
            logger.error("Unable to clear text in element: " + e.getMessage());
        }
    }   
    
 // Method to upload a file
    public void uploadFile(By by, String filePath) {
        try {
            driver.findElement(by).sendKeys(filePath);
            applyBorder(by, "green");
            logger.info("Uploaded file: " + filePath);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to upload file: " + e.getMessage());
        }
    }  
    
}
	

