package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private ActionDriver actionDriver;

	// Define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']"); // here takig admin tab for validating home page
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	//for db testing purpose locating these below elementes ie PIM, 
	private By pimTab=By.xpath("//span[text()='PIM']");
	private By empName=By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div");
	private By searchButton=By.xpath("//button[@type='submit']");
	private By empFirstAndMiddleName=By.xpath("//div[@class='oxd-table-card']/child::div[1]/child::div[3]");   //"//div[@class='oxd-table orangehrm-employee-list']/descendant::div[text()='Arsh S']");
	private By empLastName=By.xpath("//div[@class='oxd-table-card']/child::div[1]/child::div[4]");  //div[@class='oxd-table orangehrm-employee-list']/descendant::div[text()='S']");

	// Method-1: initialize the ActionDriver object by using WebDriver instance
	/*
	public HomePage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}*/
	public HomePage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}

	//Method-2: Method to verify if Admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	//Method-3: Method to verify orange hrm logo is visible
	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(orangeHRMLogo);
	}
	
	//Method-4: Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	}
	
	//Method-5:Method to navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	//Method-6:Method for Employee Search
	public void employeeSearch(String value) {
		actionDriver.enterText(empName, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(empFirstAndMiddleName);
	}
	
	//Method-7: Method to verify emp first and middle name
	public boolean verifyEmpFirstAndMiddleName(String empFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(empFirstAndMiddleName, empFirstAndMiddleNameFromDB);
	}
	
	//Method-8: Method to verify emp last name
	public boolean verifyEmpLastName(String empLastNameFromDB) {
		return actionDriver.compareText(empLastName, empLastNameFromDB);
	}
		
}
