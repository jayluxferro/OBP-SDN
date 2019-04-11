package com.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdn.app.CreateFileExample;
import com.sdn.app.Host_location;
import com.sdn.app.Set_Bandwidth_in_OVSDB;
import com.sdn.app.Set_Flow;
import com.sdn.app.Switch;

/**
 * Servlet implementation class Myservlet
 */
public class Myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Host_location host_location;   
	private static String Controller_IP = "127.0.0.1";
	private static String Mininet_IP = "192.168.56.101";
	private ServletContext context;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Myservlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		ServletContext context = getServletContext();
		context.log("SDN_App_Context");
		host_location = new Host_location(Controller_IP,context);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String Host_Mac = request.getParameter("Host_Mac");
		String Bandwidth = request.getParameter("Bandwidth");
		String Result = null;
		
		if(Bandwidth.matches("\\d+(\\.\\d+)?")) {        // To check if entered bandwidth is in right format
			Integer Bandwidth_integer = Integer.parseInt(Bandwidth);			
			if(0 < Bandwidth_integer && Bandwidth_integer < 10000) {	// To check if the bandwith is >0 && <10G
				if(Host_Mac.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) { //To check if given MAC is in right format 
					host_location.get_host_location();  // To get host and its immediate switch details
					if(!host_location.host.isEmpty()) { // To check if host exists
						Switch s = host_location.host.get(Host_Mac); // Getting immediate switch details for a host
						if(!(s == null)) {                     // To check if given host is connected to a switch
							Set_Bandwidth_in_OVSDB ovsdb = new Set_Bandwidth_in_OVSDB(Controller_IP,Mininet_IP, Bandwidth+"000000", s.switch_interface, context);
							boolean status = ovsdb.Create_OVSDB_record();				// Creating records in OVSDB
							if(status) { // To check if OVSDB record creation is successful
								Set_Flow flow = new Set_Flow(Controller_IP, Host_Mac, 0, new Integer(s.port), s.id,context); // Create a flow with action as port and queue id
								if(!flow.Set()) {
									Result = "Error while setting flow";
								} else {
									Result = "Bandwidth is set successfully";
								}
							}
							else {
								Result = "Error while inserting into OVSDB";
							}

						} else {
							Result = "No Such Host Exists";			
						}
					} else {
						Result = "Please check the connectivity between mininet and ODL";
					}
				} else {
					Result = "Enter Correct MAC address in correct format";
				}
			}else {
				Result = "Bandwidth should be greater than 0 and less than 10,000";
			}
		}else {
			Result = "Entered Bandwidth not in correct format";
		}


		request.getSession().setAttribute("Result", Result);

		response.sendRedirect("result.jsp");	
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
