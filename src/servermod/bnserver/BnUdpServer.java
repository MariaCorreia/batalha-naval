package servermod.bnserver;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import bnprotocol.BnUdpProtocolInterface;
import bnprotocol.BnUdpServerProtocolInterface;
import servermod.bninterfacegrafica.BnUdpGuiServidor;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import com.sun.istack.internal.NotNull;

public class BnUdpServer extends BnUdpAbstractServer {

	private static BnUdpServer INSTANCE = null;
	
	private BnUdpGameServer gameServer = null;
	
	private String FIXER;
			
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
					if(socket != null) BnUdpGuiServidor.bConfirmar.setEnabled(false);
					System.out.println("Server escutando na porta " + socket.getLocalPort());
					
					while(!socket.isClosed()){
						
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						String address = requestData.getAddress().getHostAddress();
						
						if(socket.getBroadcast()) {
									
							try {
								
								String requestString = new String(requestData.getData()).trim();	
								String split[] = requestString.split("#");
								String type = split[0];
								String clientName = split[1];
								String message = null;
								DatagramPacket replyData = null;
								
								switch (type) {
								
								case CONNECTION_REQUEST:
									FIXER = "CONNECTION_REQUEST";
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
											message =  BnUdpServerProtocolInterface.RUNTIME_ERROR+"#";
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
									FIXER = "LOGOUT";
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
									FIXER = "SEND_MESSAGE";
									String[] aux = requestString.split("#");
																		
									if(aux[1].equals(BROADCAST_MSG)){
										
										System.out.print("Server enviou uma mensagem em broadcast ");
										
										/**
										 * Enviar mensagem para todos
										 */
										if(clientList.isEmpty()){
											message = BnUdpServerProtocolInterface.RUNTIME_ERROR+"#";
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
									FIXER = "PING";
									/*
									 * Atualiza o ultimo ping do cliente
									 */
									for(int i = 0; i < clientList.size(); i++){
										if(clientList.get(i).name.equals(clientName)){
											clientList.get(i).lastPing();
										}
									}
									
								break;
								
								
								/****************************************************************************
								 * 
								 *			 Tratamento para protocolos referentes ao jogo
								 * 
								 ****************************************************************************/
												
								/**
								 * Apartir deste case usamos as mesmas variaveis em comum...
								 */
								case REQUEST_MATCH:
									FIXER = "REQUEST_MATCH";
									BnUdpClientNode item = getClientByName(clientName);
									//System.out.println("nome "+item.name);
									if(item != null){
										//gameServer.getInstance();
										gameServer.waitingToPlay.push(item);
										message = MATCH_REQUEST_ACK+"#";
									}else{
										message = RUNTIME_ERROR+"#";
									}
									System.out.println("mensagem " + message);
									byte[] buf = new byte[FRAME_SIZE];
									buf = message.getBytes();
									DatagramPacket dt = new DatagramPacket(buf, buf.length, item.address, item.port);
									socket.send(dt);
									System.out.println("Server enviou " + message);
                                                                       
									gameServer.printClientList();
									break;
								
								case LEAVE_QUEUE:
									FIXER = "LEAVE_QUEUE";
									item = getClientByName(clientName);
                                                                        if(item != null){
										for (int i = 0; i < gameServer.waitingToPlay.size(); i++) {
											if(item.name.equals(gameServer.waitingToPlay.get(i).getName()))
												gameServer.waitingToPlay.remove(i);
										}
										message = LEAVE_QUEUE_ACK+"#";
									}else{
										message = RUNTIME_ERROR+"#";
									}
									buf = message.getBytes();
									dt = new DatagramPacket(buf, buf.length, item.address, item.port);
									socket.send(dt);
									System.out.println("Server enviou " + message);
									gameServer.printClientList();
									break;
									
								case LEAVE_GAME:
									FIXER = "LEAVE_GAME";
									BnMatch match = gameServer.searchMatch(clientName);
									if(match != null){
										
										if(match.player_1.name.equals(clientName)){
											
											BnUdpProtocolInterface.sendData(new String(MATCH_RESULT+"#"+match.player_2.name),
													match.player_2.address.getHostAddress(), match.player_2.port, socket);
											
											BnUdpProtocolInterface.sendData(new String(LEAVE_GAME_ACK+"#"),
													match.player_1.address.getHostAddress(), match.player_1.port, socket);
										
										}else{
											
											BnUdpProtocolInterface.sendData(new String(MATCH_RESULT+"#"+match.player_1.name),
													match.player_1.address.getHostAddress(), match.player_1.port, socket);
											
											BnUdpProtocolInterface.sendData(new String(LEAVE_GAME_ACK+"#"),
													match.player_2.address.getHostAddress(), match.player_2.port, socket);
										}
										
									}else{
										
										BnUdpProtocolInterface.sendData(new String(RUNTIME_ERROR+"#"+"Partida nao encontrada"),
												requestData.getAddress().getHostAddress(), requestData.getPort(), socket);
										
									}
									break;
									
								case SEND_MATRIX:
									FIXER = "SEND_MATRIX";
									System.out.println("cliente" + clientName + "enviou uma matriz");
									String string = null;
									
									// monta vetor de coordenadas . . .
									for (int i = 2; i < split.length; i++) string = split[i];
									
									BnUdpClientNode c = getClientByName(split[1]); if(c == null) break;
									match = gameServer.searchMatch(c.name);
									BnMatrix matrix = new BnMatrix();
									
									if(matrix.initialize(string)){ 
										// garante que foi inicializada com sucesso
										// sem exeções
										c.setGameMatrix(matrix);
										message = MATRIX_ACCEPTED + "#" + c.name;
									}else{
										message = ERROR+"#"+"matriz invalida";
									}
									
									buf = new byte[FRAME_SIZE];
									buf = message.getBytes();
									dt = new DatagramPacket(buf, buf.length, match.player_1.address, match.player_1.port);
									socket.send(dt);
									dt = new DatagramPacket(buf, buf.length, match.player_2.address, match.player_2.port);
									socket.send(dt);
									
									/**
									 *  Verificar quando a matriz dos 2 jogadores foram aceitas e enviar iniciar partida	
									 */
									match = gameServer.searchMatch(split[1]);
									
									/**
									 * Se os dois jogadores tem uma matriz aceita entao envia START_GAME
									 */
									if( match != null && match.player_1.getGameMatrix() != null && match.player_2.getGameMatrix() != null){
										
										int rand = (int) Math.random()*2;
										System.out.println("random="+rand);
										if(rand > 1){
											message = START_GAME + "#"+ match.player_1.name;
											match.player_1.isYourTurn = true;
										}else{
											message = START_GAME + "#"+ match.player_2.name;
											match.player_2.isYourTurn = true;
										}
											
										buf = message.getBytes();
										dt = new DatagramPacket(buf, buf.length, match.player_1.address, match.player_1.getPort());
										socket.send(dt);
										dt = new DatagramPacket(buf, buf.length, match.player_2.address, match.player_2.getPort());
										socket.send(dt);
										match.isPalaying = true;
										System.out.println("Servidor enviou " + message + "para  jogador " + match.player_1.getName() + ", " + match.player_2.name);
									}
									
									break;
								
								case SHOT: 
									FIXER = "SHOT";
									split = requestString.split("#");
									match = gameServer.searchMatch(split[1]);
									
									if(match != null && match.isPalaying){
										
										//TODO: enviar pacote para os dois jogadores
										if(match.player_1.name.equals(split[1])){
											
											int teste = match.player_2.getGameMatrix().hitOnCoord(split[2]);
											System.out.println("tiro dado por =" + split[1] + " contra = " + match.player_2.name);
											
											if(teste == BnMatrix.HIT){
												
												// seta coordenada do hit para destruido
												// match.player_2.getGameMatrix().setCoordValue(split[2], BnMatrix.DESTROYED);
												message = OPPONET_SHOT+"#"+split[2]+"#"+"true";
												
											}else if(teste == BnMatrix.MISS){
												
												message = OPPONET_SHOT+"#"+split[2]+"#"+"false"; 
												
											}else if(teste == BnMatrix.DESTROYED){
												
												// se a coordenada já estiver destruida
												message = GAME_ERROR +"# coordenada invalida/destruida";
												
											}
											
											buf = message.getBytes();
											dt = new DatagramPacket(buf, buf.length, match.player_2.address, match.player_2.port);
											socket.send(dt);
											
										}
										
									}
									
									break;
								
								default:
									break;
								}
								
							} catch (Exception e) {
								System.err.println("Server erro em " + FIXER + " " + e.getMessage());
								BnUdpProtocolInterface.sendData(new String(ERROR+"#"+e.getMessage()), address, requestData.getPort(), socket);
							}
							
						}
						
					}
					
				} catch (BindException be){
					BnUdpGuiServidor.bConfirmar.setVisible(true);
					System.err.println("porta inválida. Já está em uso.");
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
	private BnUdpClientNode getClientByName(@NotNull String name){
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