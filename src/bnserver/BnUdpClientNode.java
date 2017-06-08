package bnserver;

import java.net.InetAddress;

public class BnUdpClientNode {
	
	/**
	 * E true se o client estiver jogando e false se ao contrario.
	 */
	protected boolean arePlaying = false;
	
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

	public boolean isArePlaying() {
		return arePlaying;
	}

	public void setArePlaying(boolean arePlaying) {
		this.arePlaying = arePlaying;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public long getLastPing() {
		return lastPing;
	}

	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}

}
