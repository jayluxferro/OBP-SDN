package com.sdn.app;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Set_Bandwidth_in_OVSDB {

	private String Controller_IP;
	private String Bandwidth;
	private String switch_interface;
	private String Mininet_IP;
	private ServletContext context;
	
	public Set_Bandwidth_in_OVSDB(String Controller_IP,String Mininet_IP,String Bandwidth,String switch_interface,ServletContext context) {
		this.Controller_IP = Controller_IP;
		this.Mininet_IP = Mininet_IP;
		this.Bandwidth = Bandwidth;
		this.switch_interface = switch_interface;
		this.context = context;
	}

	public boolean Create_OVSDB_record() {
		boolean status = false;
		try{
			connect_to_ovsdb();				// Connect to ovsdb server in mininet
			HashMap<String, String> port_mapping = Get_port_details();	//	Get the UUID and port name from port table
			String queue_uuid = insert_queue();  // Create a new record in queue table and get its UUID 
			System.out.println("Successfully inserted new record into Queue table UUID of the new record is : "+queue_uuid);
			String qos_uuid = insert_qos(queue_uuid);	// Create a new record in QOS table and map it to queue_uuid
			System.out.println("Successfully inserted new record into Qos table UUID of the new record is : "+qos_uuid);
			String port_uuid = update_port(qos_uuid, port_mapping.get(switch_interface));	// Update port table with qos_uuid
			status = true;
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			status = false;
		}
		return status;
	}

	public void connect_to_ovsdb() throws RuntimeException{
		try {
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));
			WebResource webResource2 = client.resource("http://"+Controller_IP+":8080/controller/nb/v2/connectionmanager/node/HOST1/address/"+Mininet_IP+"/port/6640/");
			ClientResponse response2 = webResource2.accept("application/json").put(ClientResponse.class);  
			if (response2.getStatus() != 200) {
				throw new RuntimeException("Error while connecting to ovsdb with error code : "+response2.getStatus());
			}
			System.out.println("Connected to ovsdb server");
		} catch (Exception e) {
			throw new RuntimeException("Error while connecting to ovsdb");
		}
	}


	public HashMap<String, String> Get_port_details() throws RuntimeException{
		HashMap<String, String> port_uuid_mapping = new HashMap<String, String>();
		try {
			String output2 = get_method("http://"+Controller_IP+":8080/ovsdb/nb/v2/node/OVS/HOST1/tables/port/rows");
			JSONObject obj = (JSONObject) JSONValue.parse(output2);
			JSONObject rows=(JSONObject) obj.get("rows");
			Iterator<String> keys = rows.keySet().iterator();
			while (keys.hasNext()) {
				String value = (String) keys.next();				
				JSONObject port_details=(JSONObject) rows.get(value);				
				String key=port_details.get("name").toString();
				port_uuid_mapping.put(key, value);
			}
			System.out.println("Got all the details of all the ports from Port table");
		} catch (RuntimeException r) {
			throw new RuntimeException("Failed to get port details with Reason : " + r.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to get port details");
		}
		return port_uuid_mapping;
	}

	public String insert_queue() throws RuntimeException{
		String URL = "http://"+Controller_IP+":8080/ovsdb/nb/v2/node/OVS/HOST1/tables/queue/rows";
		try {
			JSONObject jsonobj = new Read_json().read_json("Queue.json");
			JSONObject row = (JSONObject) jsonobj.get("row");
			JSONObject queue = (JSONObject) row.get("Queue");
			JSONArray otherconfig = (JSONArray) queue.get("other_config");
			JSONArray tmp_1 = (JSONArray) otherconfig.get(1);
			JSONArray tmp_2 = (JSONArray) tmp_1.get(1);
			tmp_2.set(1, Bandwidth);
			System.out.println("Successfully inserted new record into Queue table");
			return post_method(URL,jsonobj.toString());			
		} catch (RuntimeException e){
			throw new RuntimeException("Error while inserting into Queue table. Reason : "+e.getMessage());
		}
	}


	public String insert_qos(String queue_uuid) throws RuntimeException {
		String URL = "http://"+Controller_IP+":8080/ovsdb/nb/v2/node/OVS/HOST1/tables/qos/rows";
		try {	
			JSONObject jsonobj = new Read_json().read_json("Qos.json");
			JSONObject row = (JSONObject) jsonobj.get("row");
			JSONObject qos = (JSONObject) row.get("QoS");
			JSONArray queue = (JSONArray) qos.get("queues");
			JSONArray tmp_1 = (JSONArray) queue.get(1);
			JSONArray tmp_2 = (JSONArray) tmp_1.get(0);
			JSONArray tmp_3 = (JSONArray) tmp_2.get(1);
			tmp_3.set(1, queue_uuid);

			return post_method(URL,jsonobj.toString());
		} catch (RuntimeException e){
			throw new RuntimeException("Error while inserting into Qos table. Reason : "+e.getMessage());
		}
	}


	public String update_port(String Qos_uuid,String port_uuid) throws RuntimeException{
		try {
			String URL = "http://"+Controller_IP+":8080/ovsdb/nb/v2/node/OVS/HOST1/tables/port/rows/"+port_uuid;
			JSONObject jsonobj = new Read_json().read_json("Port.json");
			JSONObject row = (JSONObject) jsonobj.get("row");
			JSONObject port = (JSONObject) row.get("Port");
			JSONArray qos = (JSONArray) port.get("qos");
			JSONArray tmp_1 = (JSONArray) qos.get(1);
			JSONArray tmp_2 = (JSONArray) tmp_1.get(0);		
			tmp_2.set(1, Qos_uuid);
			return put_method(URL,jsonobj.toString());
		} catch (RuntimeException e){
			throw new RuntimeException("Error while updating into Port table. Reason : "+e.getMessage());
		}

	}

	public String put_method(String URL,String jsonobj) throws RuntimeException{
		String output2 = null;
		try {
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

			ClientResponse response2 = client.resource(URL)
					.accept(MediaType.APPLICATION_XML)
					.type(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class,jsonobj);

			if (response2.getStatus() != 200) {
				throw new RuntimeException("Error while invoking "+ URL +" with error code : "+response2.getStatus());
			}
			output2 = response2.getEntity(String.class);


		} catch (Exception e) {
			throw new RuntimeException("Error while invoking "+ URL);
		}
		return output2;		
	}



	public String post_method(String URL,String jsonobj) throws RuntimeException{
		String output2 = null;
		try {
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

			ClientResponse response2 = client.resource(URL)
					.accept(MediaType.APPLICATION_XML)
					.type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class,jsonobj);

			if (response2.getStatus() != 201) {
				throw new RuntimeException("Error while invoking "+ URL +" with error code : "+response2.getStatus());
			}
			output2 = response2.getEntity(String.class);

		} catch (Exception e) {
			throw new RuntimeException("Error while invoking "+ URL);
		}
		return output2;
	}


	public String get_method(String URL) throws RuntimeException{
		String output2 = null;
		try {
			HashMap<String, String> port_uuid_mapping = new HashMap<String, String>();
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

			WebResource webResource2 = client.resource(URL);

			// For get
			ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);  

			if (response2.getStatus() != 200) {
				throw new RuntimeException("Error Code : "+response2.getStatus());
			}

			output2 = response2.getEntity(String.class);

		} catch (Exception e) {
			throw new RuntimeException("Error while connecting to ovsdb");
		}
		return output2;
	}




}