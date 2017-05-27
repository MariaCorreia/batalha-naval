package bnserver;

import java.net.InetAddress;

public class BnUdpClientNode {
	
	protected String name;
	
	protected int port;
	
	protected InetAddress address; 
	
	private long lastPing;
	
	public BnUdpClientNode(String name, int port, InetAddress address){
		this.name = name;
		this.port = port;
		this.address = address;
		this.lastPing = System.currentTimeMillis();
	}
	
	/**
	 * Atualiza o ultimo ping e retorna a diferença entre o ultimo ping e o ping atual.
	 * @param currentPing
	 * @return Diferença entre o ultimo ping e o ping atual.
	 */
	protected long lastPing(){
		
		long currentTime = System.currentTimeMillis();
		long difference = currentTime - this.lastPing;
		this.lastPing = currentTime;
		
		return difference;
	}

}
