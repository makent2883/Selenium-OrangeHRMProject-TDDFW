package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

	//this method returns a Logger instance for the provided class(where Logger is an interface,so it returns Logger interface, so that we can log particular class) 
	public static Logger getLogger(Class<?> clazz) { //so, here we will provide class for which we want log-for this we will use generics as Class<?> means unknown type-it takes any type of instance of class and give variable name as clazz
		return LogManager.getLogger();  //getLogger is a method of LogManager class, and it returns Logger
		}
	 //now, we need to initialize the Logger. So in BaseClass – the primary location we will initialize Logger. So goto BaseClass 	
}
