package bnserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;

import bnprotocol.BnUdpServerProtcocolInterface;

public class BnUdpServer implements bnprotocol.BnUdpClientProtocolInterface {

	private static BnUdpServer INSTANCE = null;
	
	private int port = 10000;
	
	private DatagramSocket socket = null;
	
	private LinkedList<BnUdpClientNode> clientList;
		
	public BnUdpServer getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpServer();
		return INSTANCE;
	}
	
	public BnUdpServer(){
		this.clientList = new LinkedList<BnUdpClientNode>();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void init() {
		
		new Thread(){
			
			public void run(){
				
				try {
					
					socket = new DatagramSocket(port);
					System.out.println("Server " + socket.getLocalSocketAddress() + " running . . .");
					
					while(true){
						
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						
						if(socket.getBroadcast()) {
									
							try {
								
								System.out.println("Server " + getName() + " id:" + getId());
								
								String requestString = new String(requestData.getData()).trim();
														
								System.out.println("Server " + socket.getLocalAddress() + " receive (data) " + requestString);
								
								String type = requestString.substring(0, 2);
								String clientName = requestString.substring(3, requestString.length());
								String message = null;
								DatagramPacket replyData = null;
								
								switch (type) {
								
								case CONNECTION_REQUEST:
									if(clientName.length() <= NICKNAME_SIZE){
										if(!isConnected(clientName)){
											clientList.add(
														new BnUdpClientNode(requestString.substring(3,requestString.length()),
														requestData.getPort(), 
														requestData.getAddress())
													);
											message = BnUdpServerProtcocolInterface.CONNECTION_ACCEPTED+"#";
										}else{ 
											message =  BnUdpServerProtcocolInterface.UNKNOWN_USER+"#";
										}
										
									}else{
										message = BnUdpServerProtcocolInterface.INVALID_LOGIN+"#";
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
									while(index < clientList.size()){
										if(clientList.get(index).name.equals(clientName)){
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
									
								case SEND_MESSAGE:
									/**
									 *  012 3	.	.	.      n n+1 . .  m m+1 .
									 *  02# <destino | [all] > # <origem> # <msg>
									 */
									int n = 0;
									String aux = requestString.substring(3, requestString.length());
									String receiver = aux.substring(0, n = aux.indexOf("#"));
									aux = aux.substring(n + 1, n = aux.length());
									String sender = aux.substring(0, n = aux.indexOf("#"));
									String msg = aux.substring(n + 1, aux.length());
									
									System.out.println(receiver);
																			
									if(receiver.equals(BROADCAST_MSG)){
										
										System.out.print("broadcast Server " + socket.getLocalAddress() + " send (data) ");
										
										/**
										 * Enviar mensagem para todos
										 */
										if(clientList.isEmpty()){
											message = BnUdpServerProtcocolInterface.UNKNOWN_USER+"#";
											System.out.println(message + " (unknown user) ");
										}else{
										
											message = SEND_MESSAGE+"#"+receiver+"#"+sender+"#"+msg;
											buffer = message.getBytes();
											
											System.out.println("/ (broadcast message) " + message);
											
											for(int i = 0; i < clientList.size(); i++){
												System.out.print("[" + i + "]" + clientList.get(i).name);
												replyData = new DatagramPacket(
															buffer, 
															buffer.length, 
															clientList.get(i).address, 
															clientList.get(i).port
														);
												System.out.println( " sending to " + 
														clientList.get(i).address + ":" + 
														clientList.get(i).port + 
														" data: " + message);
												socket.send(replyData);
											}
											
											break;
											
										}// end if
										
									}else{
										
										System.out.println(" receiver ");
										
										/**
										 * Enviar mensagem para destino
										 */
										
										if (isConnected(receiver)) {
											message = SEND_MESSAGE+"#"+receiver+"#"+sender+"#"+msg;
										}else{
											message = BnUdpServerProtcocolInterface.UNKNOWN_USER+"#";
										}
										
									}// end if
								
								
									buffer = message.getBytes();
									replyData = new DatagramPacket(buffer, 
											buffer.length, requestData.getAddress(), 
											requestData.getPort());
									System.out.println("Server " + socket.getLocalSocketAddress() + " send (data) " + message);
									socket.send(replyData);
								
									break;

								default:
									break;
								}
								
							} catch (Exception e) {
								//socket.close();
								System.out.println("Server " + getName() + " id:" + getId() + " " + e.getMessage());
							}
							
						}
						
					}
					
				} catch (Exception e) {
					System.err.println("Server " + e.getMessage());
				}
				
			}// end public void run
			
		}.start();
				
	}
	
	/**
	 * Estado do cliente.
	 * @param name
	 * @return Retorna true se o cliente estiver ativo, caso contrário, false.
	 */
	private boolean isConnected(String name){
		java.util.Iterator<BnUdpClientNode> i = clientList.iterator();
		while(i.hasNext())
			if(i.next().name.equals(name)) return true;
		
		return false;
	}
	
	private void printClientList(){
		System.out.println("connected: "+clientList.size());
		for (BnUdpClientNode bnUdpClient : clientList) {
			System.out.println(" Name:" + bnUdpClient.name + " ip:" + bnUdpClient.address);
		}
	}

	public String formClientList(){
        String message = "42#";
        for (BnUdpClientNode bnUdpClient : clientList) {
            message = message+"#"+bnUdpClient.name;
        }
        
        return message;
    }

}