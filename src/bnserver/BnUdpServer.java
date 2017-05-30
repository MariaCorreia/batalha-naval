package bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import bnprotocol.BnUdpServerProtocolInterface;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class BnUdpServer extends AbstractServer {

	private static BnUdpServer INSTANCE = null;
		
	public BnUdpServer getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpServer();
		return INSTANCE;
	}
	
	public BnUdpServer(){
		super.clientList = new ArrayList<BnUdpClientNode>();
	}

	public void setPort(int port) {
		this.port = port;
	}
        public void setTableModel(DefaultTableModel tableModel) {
		this.clientTableModel = tableModel;
	}
        public DefaultTableModel getTableModel(){
            return clientTableModel;
        }
        
         public void initTableModel(){
            clientTableModel = new javax.swing.table.DefaultTableModel(
                    new Object [][] {
            },
            new String [] {
                "Clientes Conectados", "IP", "Port"
            }
        );
        }
         
         private void updateTableModel(String clientName){
             Object rowData[] = new Object[3];
             int index = 0;
             while(index < clientList.size()){
		if(clientList.get(index).name.equals(clientName)){
		break;
		}
                index++;
		} 
                rowData[0] = clientList.get(index).name;
                rowData[1] = clientList.get(index).address;
                rowData[2] = clientList.get(index).port;
                clientTableModel.addRow(rowData);
             
        /*/for(int i = 0; i < clientList.size(); i++)
        //{
            rowData[0] = clientList.get(i).name;
            rowData[1] = clientList.get(i).address;
            rowData[2] = clientList.get(i).port;
            clientTableModel.addRow(rowData);
        /}*/
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
								
								//System.out.println("Server " + getName() + " id:" + getId());
								
								// TODO: USAR SPLIT PARA FAZER O PARSER
								String requestString = new String(requestData.getData()).trim();								
								String type = requestString.substring(0, 2);
								String clientName = requestString.substring(3, requestString.length());
								String message = null;
								DatagramPacket replyData = null;
								
								//System.out.println("Server " + socket.getLocalAddress() + " receive (data) " + requestString);
								
								switch (type) {
								
								case CONNECTION_REQUEST:
									if(clientName.length() <= NICKNAME_SIZE){
										if(isConnected(clientName, requestData.getAddress()) == null){
											clientList.add(
														new BnUdpClientNode(requestString.substring(3,requestString.length()),
														requestData.getPort(), 
														requestData.getAddress())
													);
                                                                                        updateTableModel(clientName);
                                                                                            
											message = BnUdpServerProtocolInterface.CONNECTION_ACCEPTED+"#";
										}else{ 
											message =  BnUdpServerProtocolInterface.UNKNOWN_USER+"#";
										}
										
									}else{
										message = BnUdpServerProtocolInterface.INVALID_LOGIN+"#";
									}
									
									buffer = message.getBytes();
									replyData = new DatagramPacket(buffer, 
											buffer.length, requestData.getAddress(), 
											requestData.getPort());
									System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
									socket.send(replyData);
									System.out.println("Server connected list: ");
									printClientList();
									
									connectedListBroadcast();
									
									break;
									
								case LOGOUT:	
									int index = 0;
									while(index < clientList.size()){
										if(clientList.get(index).name.equals(clientName)){
											clientList.remove(index);
										}
										
										index++;
									}
									clientTableModel.removeRow(index);
									message = BnUdpServerProtocolInterface.ACK_LOGOUT+"#";
									buffer = message.getBytes();
									replyData = new DatagramPacket(buffer, 
											buffer.length, requestData.getAddress(), 
											requestData.getPort());
									System.out.println("Server " + socket.getLocalAddress() + " send (data) " + message);
									socket.send(replyData);
									printClientList();
									connectedListBroadcast();
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
																		
									if(receiver.equals(BROADCAST_MSG)){
										
										System.out.print("broadcast Server " + socket.getLocalAddress() + " send (data) ");
										
										/**
										 * Enviar mensagem para todos
										 */
										if(clientList.isEmpty()){
											message = BnUdpServerProtocolInterface.UNKNOWN_USER+"#";
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
										BnUdpClientNode clientNode = isConnected(receiver, requestData.getAddress());
										if (clientNode != null) {
											message = SEND_MESSAGE+"#"+receiver+"#"+sender+"#"+msg;
											buffer = message.getBytes();
											
											// pacote para o destinatário
											DatagramPacket toReceiver = new DatagramPacket(
													buffer, 
													buffer.length, 
													clientNode.address, 
													clientNode.port
													);
											
											System.out.println("Server " + socket.getLocalSocketAddress() + " send to:"
													+ clientNode.address + "(data) " + message);
											
											socket.send(toReceiver);
											
											DatagramPacket toSender = null;
											if(!receiver.equals(sender)){
												// pacote para o transmissor
												toSender = new DatagramPacket(
														buffer, 
														buffer.length, 
														requestData.getAddress(), 
														requestData.getPort()
														);
												socket.send(toSender);
											}
										}
										
										if(clientNode == null){
											message = BnUdpServerProtocolInterface.UNKNOWN_USER+"#";
											buffer = message.getBytes();
											
											// pacote para o remetente
											DatagramPacket toSender = new DatagramPacket(
														buffer, 
														buffer.length, requestData.getAddress(), 
														requestData.getPort()
													);
											
											System.out.println("Server " + socket.getLocalSocketAddress() + " send to:"
													+ requestData.getAddress() + "(data) " + message);
											
											socket.send(toSender);
											
										}
										
									}// end if
								
									break;
								
								case PING:
									/*
									 * Atualiza o ultimo ping do cliente
									 */
									for(int i = 0; i < clientList.size(); i++){
										if(clientList.get(i).name.equals(clientName)){
											clientList.get(i).lastPing();
										}
									}

								default:
									break;
								}
								
							} catch (Exception e) {
								System.out.println("Server " + getName() + " id:" + getId() + " " + e.getMessage());
							}
							
						}
						
					}
					
				} catch (Exception e) {
					System.err.println("Server " + e.getMessage());
				}
				
			}// end public void run
			
		}.start();
		
		//this.refreshClientList();
				
	}
	
	/**
	 * Atualiza lista de clientes em um intervalo de tempo.
	 */
	private void refreshClientList(){
		
		new Thread(){
			
			public void run(){
				
				while(true){
					
					try {
						
						for(int index = 0; index < clientList.size(); index++){
							
							long lastTime = clientList.get(index).lastPing();
							//System.out.println("last time : " + lastTime);
							if(lastTime >= REFRESH_TIME){
								clientList.remove(index);
								printClientList();
							}
							
						}
						
						sleep(REFRESH_TIME);
						
					} catch (Exception e) {
						System.err.println("Server (client list) " + e.getMessage() );
					}
					
				}
				
			}
			
		}.start();
		
	}
	
	/**
	 * Estado do cliente.
	 * @param name
	 * @return Retorna um BnUdpClientNode se o cliente estiver ativo, caso contrário, null.
	 */
	private BnUdpClientNode isConnected(String name, InetAddress address){
		BnUdpClientNode clientNode = null;
		for(int i = 0 ; i < clientList.size(); i++){
			if(clientList.get(i).name.equals(name) && clientList.get(i).address.equals(address)){
				return clientList.get(i);
			}
		}
		
		return clientNode;
	}
	
	private void printClientList(){
		System.out.println("connected: "+clientList.size());
		for (BnUdpClientNode bnUdpClient : clientList) {
			System.out.println(" Name:" + bnUdpClient.name + " ip:" + bnUdpClient.address);
		}
	}
	
	/**
	 * Este método é responsável por enviar a lista de clientes conectados no momento. É geralmente chamada
	 * nas requisições de login e logout.
	 * @throws IOException
	 */
        
	private void connectedListBroadcast() throws IOException{
		
		byte[] buffer = new byte[FRAME_SIZE];
		DatagramPacket replyData;
		
		String message = BnUdpServerProtocolInterface.LIST_CONNECTED;
        for (BnUdpClientNode bnUdpClient : clientList) message += "#"+bnUdpClient.name;
        
        System.out.println("Server " + socket.getLocalSocketAddress() + " send (client list) [data] " + message);
        
		buffer = message.getBytes();
		
		for(int i = 0; i < clientList.size(); i++){
			
			System.out.println("[" + i + "]" + clientList.get(i).name);
			
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
		
	}

}