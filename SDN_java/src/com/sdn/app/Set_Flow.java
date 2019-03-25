package com.sdn.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

public class Set_Flow {
	
	static int flow_id = 117;
	static int table_id = 0;
	private String Controller_IP;
	private String MAC;
	private int queue_id;
	private int output_port;
	private String switch_name;
	private ServletContext context;
	
	public Set_Flow(String Controller_IP,String MAC,int queue_id,int output_port,String switch_name,ServletContext context) {
		this.Controller_IP = Controller_IP;
		this.MAC = MAC;
		this.queue_id = queue_id;
		this.output_port = output_port;
		this.switch_name = switch_name;
		this.context = context;
	}
	
	public boolean Set() {
		// TODO Auto-generated method stub
		boolean status = false;
		try {
			File file = new File("C:/Users/ckedamalap.SPIRENTCOM/Desktop/flow.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Flow.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Flow flow = (Flow) jaxbUnmarshaller.unmarshal(file);
			//System.out.println(flow);
			
			flow.id = (byte) flow_id;
			flow.flowName = "Flow"+flow_id;
			ArrayList<Flow.Instructions.Instruction.ApplyActions.Action> action = (ArrayList<Flow.Instructions.Instruction.ApplyActions.Action>)flow.instructions.instruction.applyActions.getAction();
			action.get(0).outputAction.outputNodeConnector = (byte)output_port;
			action.get(1).setQueueAction.queueId = (byte)queue_id;
			flow.match.ethernetMatch.ethernetDestination.address = MAC;
			String URL="http://"+Controller_IP+":8181/restconf/config/opendaylight-inventory:nodes/node/"+switch_name+"/table/"+table_id+"/flow/"+flow_id;
			put_method(URL, flow);
			status = true;
		  } catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("Error While setting flow");  
			status = false;
		  }
		return status;
	}

	public static String put_method(String URL,Flow xmlobj) {
		String output2 = null;
		try {
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));
			
			ClientResponse response2 = client.resource(URL)
					.accept(MediaType.APPLICATION_XML)
					.type(MediaType.APPLICATION_XML)
					.put(ClientResponse.class,xmlobj);

			if (response2.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
			}
			output2 = response2.getEntity(String.class);

			System.out.println("Flow Set Successfully");

		} catch (Exception e) {
			System.out.println("Error While setting flow");
		}
		return output2;		
	}

	
}
