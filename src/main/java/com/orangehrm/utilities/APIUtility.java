package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {

	//step1: Method to send the GET request
	public static Response sendGetRequest(String endPoint) { //endpoint is url, to send getrequest we have to pass url
		return RestAssured.get(endPoint);
	}
	
	//step2: Method to send POST request
	public static Response sendPostRequest(String endPoint,String payLoad) {  //POST requires url and payload ie body
		return RestAssured.given().header("Content-Type","application/json") //here contenttype should be in json format
						   .body(payLoad)  //adding body
						   .post();  //send post request here
	}
	
	//step3: Method to validate the Response status 
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode()==statusCode;
	}
	
	//step4: Method to extract value from JSON Response
	public static String getJsonValue(Response response, String value) {  //here value is-which response value you want from jsonresponse
		return response.jsonPath().getString(value);
	}
	
}

