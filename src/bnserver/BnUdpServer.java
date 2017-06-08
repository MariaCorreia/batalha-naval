package bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import bnprotocol.BnUdpServerProtocolInterface;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class BnUdpServer extends BnUdpAbstractServer {

	private static BnUdpServer INSTANCE = null;
	
	private BnUdpGameServer gameServer = null;
			
	public BnUdpServer getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpServer();
		return INSTANCE;
	}
	
	public BnUdpServer(){
		super.clientList = new ArrayList<BnUdpClientNode>();
		gameServer = new BnUdpGameServer(this);
		gameServer.init();
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
	            new Object [][] {}, new String [] {"Clientes Conectados", "IP", "Port"});
	}
     
	 private void updateTableModel(){
	     Object rowData[] = new Object[3];
	     int index = 0;
	     
	    if (clientTableModel.getRowCount() > 0) {
	    	    for (int i = clientTableModel.getRowCount() - 1; i > -1; i--) {
	    	        clientTableModel.removeRow(i);
	    	    }
	    }
	    
	     while(index < clientList.size()){
	    	 rowData[0] = clientList.get(index).name;
	         rowData[1] = clientList.get(index).address;
	         rowData[2] = clientList.get(index).port;
	         clientTableModel.addRow(rowData);
	        index++;
		} 
	}
         
	public void init() {
		
		new Thread(){
			
			public void run(){
				
				try {
					
					socket = new DatagramSocket(port);
					System.out.println("Server escutando na porta " + socket.getLocalPort());
					
					while(true){
						
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						
						if(socket.getBroadcast()) {
									
							try {
															
								String requestString = new String(requestData.getData()).trim();								
								String type = requestString.substring(0, 2);
								String clientName = requestString.substring(3, requestString.length());
								clientName.trim();
								String message = null;
								DatagramPacket replyData = null;
								
								switch (type) {
								
								case CONNECTION_REQUEST:
									if(clientName.length() <= NICKNAME_SIZE){
										if(getClientByName(clientName) == null){
											clientList.add(
														new BnUdpClientNode(requestString.substring(3,requestString.length()),
														requestData.getPort(), 
														requestData.getAddress())
													);
												updateTableModel();
                                                                                            
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
									System.out.println("Server enviou " + message);
									socket.send(replyData);
									System.out.println("Server lista de conectados ");
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
									message = BnUdpServerProtocolInterface.LOGOUT_ACK+"#";
									buffer = message.getBytes();
									replyData = new DatagramPacket(buffer, 
											buffer.length, requestData.getAddress(), 
											requestData.getPort());
									System.out.println("Server enviou " + message);
									socket.send(replyData);
									printClientList();
									connectedListBroadcast();
									break;
									
								case SEND_MESSAGE:
									String[] aux = requestString.split("#");
																		
									if(aux[1].equals(BROADCAST_MSG)){
										
										System.out.print("Server enviou uma mensagem em broadcast ");
										
										/**
										 * Enviar mensagem para todos
										 */
										if(clientList.isEmpty()){
											message = BnUdpServerProtocolInterface.UNKNOWN_USER+"#";
											System.out.println(message + " (unknown user) ");
										}else{
										
											message = SEND_MESSAGE+"#"+aux[1]+"#"+aux[2]+"#"+aux[3];
											buffer = message.getBytes();
											
											System.out.println(" mensagem broadcast: " + message);
											
											for(int i = 0; i < clientList.size(); i++){
												System.out.print("[" + i + "]" + clientList.get(i).name);
												replyData = new DatagramPacket(
															buffer, 
															buffer.length, 
															clientList.get(i).address, 
															clientList.get(i).port
														);
												System.out.println( " enviando para " + 
														clientList.get(i).address + ":" + 
														clientList.get(i).port + 
														" data: " + message);
												socket.send(replyData);
											}
											
											break;
											
										}// end if
										
									}else{
																				
										/**
										 * Enviar mensagem para destino
										 */
										BnUdpClientNode clientNode = getClientByName(aux[1]);
										if (clientNode != null) {
											message = "02"+"#"+aux[1]+"#"+aux[2]+"#"+aux[3];
											buffer = message.getBytes();
											
											// pacote para o destinatário
											DatagramPacket destino = new DatagramPacket(
													buffer, 
													buffer.length, 
													clientNode.address, 
													clientNode.port
													);
											
											System.out.println("Servidor enviou para:"
													+ " " + clientNode.name + clientNode.address + " " + clientNode.port + "(data) " + message);
											
											socket.send(destino);
											
											/**
											 * Envia para o transmissor
											 */
											DatagramPacket toSender = new DatagramPacket(
													buffer, 
													buffer.length, 
													requestData.getAddress(), 
													requestData.getPort()
													);
											socket.send(toSender);
											
										}// endif
										
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
									
								break;
												
								/**
								 * Apartir deste case usamos as mesmas variaveis em comum...
								 */
								case REQUEST_MATCH:
									BnUdpClientNode item = getClientByName(clientName);
									//System.out.println("nome "+item.name);
									if(item != null){
										//gameServer.getInstance();
										gameServer.waitingToPlay.push(item);
										message = MATCH_REQUEST_ACK+"#";
									}else{
										message = UNKNOWN_USER+"#";
									}
									System.out.println("mensagem " + message);
									byte[] buf = new byte[FRAME_SIZE];
									buf = message.getBytes();
									DatagramPacket dt = new DatagramPacket(buf, buf.length, item.address, item.port);
									socket.send(dt);
									System.out.println("Server enviou " + message);
                                                                       
									gameServer.printClientList();
									break;
								
								case GET_OUT_QUEUE:
									item = getClientByName(clientName);
                                                                        if(item != null){
										for (int i = 0; i < gameServer.waitingToPlay.size(); i++) {
											if(item.name.equals(gameServer.waitingToPlay.get(i).getName()))
												gameServer.waitingToPlay.remove(i);
										}
										message = LEAVE_QUEUE_ACK+"#";
									}else{
										message = UNKNOWN_USER+"#";
									}
									buf = message.getBytes();
									dt = new DatagramPacket(buf, buf.length, item.address, item.port);
									socket.send(dt);
									System.out.println("Server enviou " + message);
									gameServer.printClientList();
									break;
									
								case LEAVE_GAME:
									item = getClientByName(clientName);
									if(item!=null){
										message = BnUdpServerProtocolInterface.LEAVE_GAME_ACK+"#";
									}else{
										message = UNKNOWN_USER+"#";
									}
									buf = message.getBytes();
									dt = new DatagramPacket(buf, buf.length, item.address, item.port);
									socket.send(dt);
									System.out.println("Server enviou " + message);
								default:
									break;
								}
								
							} catch (Exception e) {
								System.err.println("Server erro:" + e.getMessage());
							}
							
						}
						
					}
					
				} catch (Exception e) {
					System.err.println("Server " + e.getMessage() );
					e.printStackTrace();
				}
				
			}// end public void run
			
		}.start();
		
		this.refreshClientList();
				
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
							if(lastTime >= REFRESH_INTERVAL){
								clientList.remove(index);
								updateTableModel();
								printClientList();
							}
							
						}
						
						sleep(REFRESH_INTERVAL);
						
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
	private BnUdpClientNode getClientByName(String name){
		BnUdpClientNode clientNode = null;
		for(int i = 0 ; i < clientList.size(); i++){
			if(clientList.get(i).name.equals(name)){
				//System.out.println(" pessoa conectada :" + clientList.get(i).name);
				return clientList.get(i);
			}
		}
		
		return clientNode;
	}
	
	private void printClientList(){
		System.out.println("Conectados: "+clientList.size());
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
        
        System.out.println("Servidor Lista de clientes ativos, mensagem: " + message);
        
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