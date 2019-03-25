package com.sdn.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class CreateFileExample 
{
    public void test()
    {	
    	try {
    		 
	      File file = new File("newfile.txt");
	      
	      if (file.createNewFile()){
	        System.out.println("File is created!");
	      }else{
	        System.out.println("File already exists.");
	      }
	      
    	} catch (IOException e) {
	      e.printStackTrace();
	}
    }
    
    
    public String read() {
    	
    	Properties prop = new Properties();
    	InputStream input = null;
    	String r = null;
    	try {

    		input = new FileInputStream("SDN_App_Config.properties");
    			// load a properties file
    		prop.load(input);

    		// get the property value and print it out
    		String SDN_IP = prop.getProperty("SDN_IP");
    		String Mininet_IP = prop.getProperty("Mininet_IP");
    		r = SDN_IP +", "+Mininet_IP;
    		System.out.println("Config"+r);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	return r;
    }
    
    
}