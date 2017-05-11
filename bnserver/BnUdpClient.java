package bnserver;

import java.net.InetAddress;

public class BnUdpClient {
	
	protected String name;
	
	protected int port;
	
	protected InetAddress address; 
	
	public BnUdpClient(String name, int port, InetAddress address){
		this.name = name;
		this.port = port;
		this.address = address;
	}

}
