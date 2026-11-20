package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	private ActionDriver actionDriver;

	// Define locators using By class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[@type='submit']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

	// initialize the ActionDriver object by using WebDriver instance
	
	/*public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver); //instead of creating object of Actiondriver everytime in pagers, we will just call getActionDriver() emthod from BaseClass - with this we can avoid creation of multiple instances of driver ie what singleton approach says ok
	} */
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}

	// Method to perform login
	public void login(String username, String password) {
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}

	// Method to check if error msg is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	// Method to get the text from Error message
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}

	// Verify if Error Msg is correct or not
	public boolean verifyErrorMessage(String expectedErrorMsg) {
		return actionDriver.compareText(errorMessage, expectedErrorMsg);
	}

}
