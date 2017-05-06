package bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import bnprotocol.BnUdpServerProtcocolInterface;

public class BnUdpServer implements bnprotocol.BnUdpClientProtocolInterface {

	private static BnUdpServer INSTANCE = null;
	
	DatagramSocket socket = null;
	
	public BnUdpServer getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpServer();
		return INSTANCE;
	}
	
	public BnUdpServer(){}
	
	public void run() throws IOException {
		
		socket = new DatagramSocket(10000);
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
							String message = null;
							DatagramPacket replyData = null;
							
							switch (type) {
							
							case CONNECTION_REQUEST:
								message = BnUdpServerProtcocolInterface.CONNECTION_ACCEPTED+"#";
								buffer = message.getBytes();
								replyData = new DatagramPacket(buffer, 
										buffer.length, requestData.getAddress(), 
										requestData.getPort());
								System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
								socket.send(replyData);
								break;
								
							case LOGOUT: 
								message = BnUdpServerProtcocolInterface.ACK_LOGOUT+"#";
								buffer = message.getBytes();
								replyData = new DatagramPacket(buffer, 
										buffer.length, requestData.getAddress(), 
										requestData.getPort());
								System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
								socket.send(replyData);
								break;

							default:
								break;
							}
							
						} catch (Exception e) {
							socket.close();
							System.err.println("Error: " + e.getMessage() );
						}
						
					}
					
				}.start();
				
			}
			
		}
				
	}

}
