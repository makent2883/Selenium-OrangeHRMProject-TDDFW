package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	//step2: 
	public static List<String[]> getSheetData(String filePath, String sheetName){
		//data variable is defined as a list of arrays of string
		List<String[]> data=new ArrayList<>();
		
		//fileinputstream class, to use this, we will use try-with-resources statement, refer below. with try-with-resouces -> no need to use finally block, automatically resources will get closed after their tasks completion ok
		try(FileInputStream fis=new FileInputStream(filePath); Workbook workbook=new XSSFWorkbook(fis)){
			Sheet sheet=workbook.getSheet(sheetName);//to access the sheet, use Sheet interface
			if(sheet==null) {
				throw new IllegalArgumentException("Sheet "+sheetName+" doesn't exists");
			}
			//now, iterate through rows
			for(Row row:sheet) { //use Row interface
				if(row.getRowNum()==0) {  
					continue; //to ignore the first row and then continue with next row
				}
				//Read all cells in the row (row wise and cell wise data in cell)
				List<String> rowData=new ArrayList<>();
				for(Cell cell:row) { //iterate through row one by one all the cells
					rowData.add(getCellValue(cell));
				}
				//convert rowData to String[] and add that into main array ie into data.
				data.add(rowData.toArray(new String[0])); //to create array with zero object-use new String[0]				
			}//for closing
		}//try-with-resource closing
		catch(IOException e) {
			e.printStackTrace();
		}
		return data;  //thsi data will return us list of array of strings ok. 
		
	}//getSheetData() closing
	
	//step1: 
	private static String getCellValue(Cell cell) {
		if(cell==null) {
			return "";  //if cell is null we will return blank value ok
		}
		switch(cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((int)cell.getNumericCellValue()); //other than date if it is decimal ornumeric then use this
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}	
	} //closing getCellValue() method
}
