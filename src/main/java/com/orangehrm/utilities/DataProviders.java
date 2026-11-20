package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

	//step1: to read the Excel file, create one variable FILE_PATH
	private static final String FILE_PATH=System.getProperty("user.dir")+"/src/test/resources/testdata/TestData.xlsx";

	//step2:
	private static Object[][] getSheetData(String sheetName) {
		//call getSheetData() method
		List<String[]> sheetData=ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
		//creating object of this Object[]][] array
		Object[][] data=new Object[sheetData.size()][sheetData.get(0).length]; //and we initilaize the object with noof rows-so use sheetData.size() and noof columns in a row-use sheetData.get(0).length - here get(0) is the first row 
		//for loop to get entire data from excel
		for(int i=0; i<sheetData.size();i++) {
			data[i]=sheetData.get(i);
		}
		return data;	
	} //getSheetData() closing
	
	//step3: now we need to create dataprovider methods by using @DataProvider annotation and this is for validLoginData
	@DataProvider(name="validLoginData")
	public static Object[][] validLoginData(){
		return getSheetData("validLoginData"); 
	}
	
	//step4: dataprovider method for invalidLoginData
	@DataProvider(name="inValidLoginData")
	public static Object[][] inValidLoginData(){
		return getSheetData("inValidLoginData");  
	}
			
	//step5: consider this method, while discussing Database Testing, where we store db data in excel and then provide that excel data to testcases using dataprovider ok	
	@DataProvider(name="dbEmpVerification")
	public static Object[][] dbEmpVerification(){
		return getSheetData("DBEmpVerification"); 
	}
}			
			
