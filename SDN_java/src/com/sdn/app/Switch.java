package com.sdn.app;

public class Switch {
	public String mac;
	public String id;
	public String port;
	public String switch_interface;
	public Switch(String mac,String id,String port,String switch_interface) {
		this.mac = mac;
		this.id = id;
		this.port = port;
		this.switch_interface = switch_interface;
	}
}
