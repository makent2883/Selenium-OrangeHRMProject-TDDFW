package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

//step1: to make db connection, we need 3 things like db url, db username and db pwd, so we will create 3 variables 
	private static final String DB_URL="jdbc:mysql://localhost:3307/orangehrm"; //here 3307 is port num, orangehrm is databasename
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="";
	private static final Logger logger=BaseClass.logger;
	
//step2: method to make db connection
	public static Connection getDBConnection() {
		try {
			//System.out.println("Starting DB Connection..."); //instead of sop, we can use logs, so that it can come in log files also
			logger.info("Starting DB Connection...");
			Connection con=DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD); //here Connection is interface and DriverManager is a class (refer in my java notes)
			//System.out.println("DB Connection Successfull"); //then surround these 3 lines with try-catch ok
			logger.info("DB Connection Successfull"); 
			return con;
		} catch (SQLException e) {
			//System.out.println("Error while establishing the DB Connection");
			logger.error("Error while establishing the DB Connection");
			e.printStackTrace();
			return null;
		}	
	}
	
//step3: method to get data from db and store in a Map
	public static Map<String,String> getEmpDetails(String empID) { //return type as Map bcs, we can store the values in key-value pair, for eg:firstname=Arsh, secondname=S like this
		String query="SELECT emp_firstname,emp_middlename,emp_lastname FROM hs_hr_employee WHERE employee_id="+empID;
		//now we need to store these data into map reference, so create object of hashmap
		Map<String,String> empDetails=new HashMap<>();
		//we need 3 objects now, to execute the query, for this, we will use try-with-reseources stmt
			//connection object, statement obj-to execute the query, resultset object-to store results after execution
		try(Connection con=getDBConnection();
				Statement stmt=con.createStatement(); 
				ResultSet rs=stmt.executeQuery(query)
				){
			//System.out.println("Executing Query: "+query);
			logger.info("Executing Query: "+query);
			if(rs.next()) {
				String firstName=rs.getString("emp_firstname");
				String middleName=rs.getString("emp_middlename");
				String lastName=rs.getString("emp_lastname");
				//store in map
				empDetails.put("firstName", firstName);
				empDetails.put("middletName", middleName!=null?middleName:""); //some emp's wont have middlename, so for that we wil use Ternary operator here - if he has middlename, it will print middlename otherwise it will print blank ok
				empDetails.put("lastName", lastName);
				
			//	System.out.println("Query Executed Successfully");
				logger.info("Query Executed Successfully");
			//	System.out.println("Employee Data Fetched: "+empDetails);
				logger.info("Employee Data Fetched: "+empDetails);
			} //if closing
			else {
				//System.out.println("Employee not found");
				logger.error("Employee not found");
			}			
		}//try-with-resource closing
		catch(Exception e) {
			//System.out.println("Error while executing query");
			logger.info("Error while executing query");
			e.printStackTrace();
		}
		return empDetails;		
	}//getEmpDetails closing
	
}
