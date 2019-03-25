package com.sdn.app;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Host_location {
	
	private String Controller_IP;
	public HashMap<String , Switch> host;
	private ServletContext context;
	
	public Host_location(String Controller_IP,ServletContext context) {
		this.Controller_IP = Controller_IP;
		host = new HashMap<String , Switch>();
		this.context = context;
	}
	public void get_host_location() {
		
		JSONParser parser = new JSONParser();
		try {
			
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));
			
			WebResource webResource2 = client.resource("http://"+Controller_IP+":8080/restconf/operational/opendaylight-inventory:nodes/");
			
			ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);  
			
			if (response2.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
			}
			String output2 = response2.getEntity(String.class);
			JSONObject obj= new JSONObject(output2);
			JSONObject obj_1 = (JSONObject) obj.get("nodes");
			//JSONArray test = (JSONArray) obj.get("nodes.node");
			JSONArray nodes = (JSONArray) obj_1.get("node");
			for(int i=0;i<nodes.length();i++) {
				JSONObject js = (JSONObject)nodes.get(i);
				JSONArray tmp = (JSONArray) js.get("node-connector");
				for(int j=0;j<tmp.length();j++) {
					JSONObject tmp_1 = (JSONObject) tmp.get(j);
					if(tmp_1.has("address-tracker:addresses")) {
						JSONArray tmp_2 = (JSONArray) tmp_1.get("address-tracker:addresses");
						for(int k=0;k<tmp_2.length();k++) {
							JSONObject tmp_3 = (JSONObject) tmp_2.get(k);
							host.put(tmp_3.getString("mac"), new Switch(tmp_1.getString("flow-node-inventory:hardware-address"), js.getString("id"), tmp_1.getString("flow-node-inventory:port-number"),tmp_1.getString("flow-node-inventory:name")));
						}
					}
				}
			}
			System.out.println("Got "+host.size()+" host details"); 
		} catch (Exception e) {
			System.out.println("Failed to get Host and its immediate switch details");
		}
	}

}
