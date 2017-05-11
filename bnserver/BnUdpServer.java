package bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import bnprotocol.BnUdpServerProtcocolInterface;

public class BnUdpServer implements bnprotocol.BnUdpClientProtocolInterface {

	private static BnUdpServer INSTANCE = null;
	
	private int port = 10000;
	
	private DatagramSocket socket = null;
	
	private LinkedList<BnUdpClient> clientList;
	
	public BnUdpServer getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpServer();
		return INSTANCE;
	}
	
	public BnUdpServer(){
		this.clientList = new LinkedList<BnUdpClient>();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run() throws IOException {
		
		socket = new DatagramSocket(port);
		System.out.println("Server " + socket.getLocalSocketAddress() + " running . . .");
		
		while(true){
			
			byte[] buffer = new byte[FRAME_SIZE];
			DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
			socket.receive(requestData);
			
			if(socket.getBroadcast()) {
				
				new Thread(){
					
					public void run(){
						
						try {
							
							System.out.println("Server " + getName() + " id:" + getId());
							
							byte[] buffer = new byte[FRAME_SIZE];
							String requestString = new String(requestData.getData());
													
							System.out.println("Server " + socket.getLocalAddress() + " receive (data) " + requestString);
							
							String type = requestString.substring(0, 2);
							String clientName = requestString.substring(3, requestString.length());
							String message = null;
							DatagramPacket replyData = null;
							
							switch (type) {
							
							case CONNECTION_REQUEST:
								if(!isConnected(clientName, requestData.getAddress())){
									clientList.add(
												new BnUdpClient(requestString.substring(3,requestString.length()),
												requestData.getPort(), 
												requestData.getAddress())
											);
									message = BnUdpServerProtcocolInterface.CONNECTION_ACCEPTED+"#";
								}else{ 
									message =  BnUdpServerProtcocolInterface.USER_EXISTS+"#";
								}
								
								buffer = message.getBytes();
								replyData = new DatagramPacket(buffer, 
										buffer.length, requestData.getAddress(), 
										requestData.getPort());
								System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
								socket.send(replyData);
								System.out.println("Server connected list: ");
								printClientList();
								break;
								
							case LOGOUT:								
								int index = 0;								
								while(index <= clientList.size()){
									if(clientList.get(index).address.equals(requestData.getAddress())){
										clientList.remove(index);
									}
									
									index++;
								}
								
								message = BnUdpServerProtcocolInterface.ACK_LOGOUT+"#";
								buffer = message.getBytes();
								replyData = new DatagramPacket(buffer, 
										buffer.length, requestData.getAddress(), 
										requestData.getPort());
								System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
								socket.send(replyData);
								printClientList();
								break;

							default:
								break;
							}
							
						} catch (Exception e) {
							socket.close();
							System.out.println("Server " + getName() + " id:" + getId() + " " + e.getMessage());
						}
						
					}
					
				}.start();
				
			}
			
		}
				
	}
	
	/**
	 * Estado do cliente.
	 * @param name
	 * @param addres
	 * @return Retorna true se o cliente estiver ativo, caso contrário, false.
	 */
	private boolean isConnected(String name, InetAddress addres){
		java.util.Iterator<BnUdpClient> i = clientList.iterator();
		java.util.Iterator<BnUdpClient> j = clientList.iterator();
		while(i.hasNext())
			if(i.next().name.equals(name) && j.next().address.equals(addres)) return true;
		
		return false;
	}
	
	private void printClientList(){
		System.out.println("connected: "+clientList.size());
		for (BnUdpClient bnUdpClient : clientList) {
			System.out.println(" Name:" + bnUdpClient.name + " ip:" + bnUdpClient.address);
		}
	}

}