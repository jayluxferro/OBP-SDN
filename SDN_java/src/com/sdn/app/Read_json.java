package com.sdn.app;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class Read_json {

	public static void main(String[] args) throws RuntimeException{
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		Object object;
		try {
			object = parser.parse(new FileReader(
			        "Queue.json"));
			JSONObject jsonObject = (JSONObject) object;
			JSONObject row = (JSONObject) jsonObject.get("row");
			JSONObject queue = (JSONObject) row.get("Queue");
			JSONArray otherconfig = (JSONArray) queue.get("other_config");
			System.out.println("Done");
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		} catch (IOException e) {
			throw new RuntimeException("IO Exception while reading JSON");
		} catch (ParseException e) {
			throw new RuntimeException("Error while parsing JSON");
		}
        
	}
	
	public JSONObject read_json(String filename) throws RuntimeException{
		JSONParser parser = new JSONParser();
		Object object = null;
		JSONObject jsonObject = null;
		try {
			object = parser.parse(new FileReader(
			        filename));
			jsonObject = (JSONObject) object;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(filename+" JSON File not found");
		} catch (IOException e) {
			throw new RuntimeException("IO Exception while reading JSON");
		} catch (ParseException e) {
			throw new RuntimeException("Error while parsing JSON");
		}
		return jsonObject;
	}

}
