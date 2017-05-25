package bnserver;

import java.net.InetAddress;

public class BnUdpClientNode {
	
	protected String name;
	
	protected int port;
	
	protected InetAddress address; 
	
	public BnUdpClientNode(String name, int port, InetAddress address){
		this.name = name;
		this.port = port;
		this.address = address;
	}

}
