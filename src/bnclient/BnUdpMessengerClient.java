package bnclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import bnguiclient.BnUdpTelaClienteChat;
import bnlogin.BnUdpLogin;
import bnprotocol.BnUdpServerProtcocolInterface;

public class BnUdpMessengerClient extends AbstractClient {
	
	private static BnUdpMessengerClient INSTANCE = null;
			
	private BnUdpTelaClienteChat chat = null;
	
	public BnUdpMessengerClient getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpMessengerClient();
		return INSTANCE;
	}
	
	public BnUdpMessengerClient(BnUdpTelaClienteChat chat){
		this.chat = chat;
	}
	
	public BnUdpMessengerClient(){ 
		
	}
	
	/**
	 * Inicia escuta do cliente. Espera os dados transmitidos e os exibe usando a referencia para
	 * a instancia de chat.
	 */
	public void startListning() {
		
		new Thread(){
			
			public void run(){
				
				System.out.println("Cliente " + socket.getLocalSocketAddress() + " listening . . .");
												
				while(loginStatus){
					
					try {
																			
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						
						if(socket.getBroadcast()) {
														
							try {
								
								System.out.println("Client " + getName() + " id:" + getId());
								
								String requestString = new String(requestData.getData()).trim();
														
								System.out.println("Client " + socket.getLocalSocketAddress() + " receive (data) " + requestString);
								
								String[] split = requestString.split("#");
								
								switch (split[0]) {
								case SEND_MESSAGE:							
									String chatString = split[2]+":"+split[3]+"\n";
									chat.gettChat().setText(chat.gettChat().getText()+chatString);
									break;
								
								case BnUdpServerProtcocolInterface.ACK_LOGOUT:
									loginStatus = false;
									chat.gettChat().setText(chat.gettChat().getText()+"Voce foi desconectado (a) . . .\n");
									System.out.println("Voce foi desconectado (a) . . . ");
									break;
									
								case LIST_CONNECTED:					
									int num = listOfconnections.size() + split.length;
									System.out.println("Client (list of clients) " + "[" + num + "]");
									
									for(int i = 1; i < split.length; i++){
										listOfconnections.add(split[i]);
										System.out.println("\n"+split[i]);
									}
									
									break;
									
								case UNKNOWN_USER: //TODO: fazer ação
									break;
									
								case BnUdpLogin.TIMED_OUT:
									chat.gettChat().setText(chat.gettChat().getText()+"Nao foi possivel desconectar  . . .\n");
									System.out.println("Nao foi possivel desconectar . . .");
									break;
									

								default:
									break;
								}
								
								
							} catch (Exception e) {
								System.err.println("Client "  + socket.getLocalAddress() + " error:" + e.getMessage());
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
													
					} catch (Exception e) {
						System.err.println("Client " + e.getMessage());
					}
				}
				
				// TODO: levantar tela de login
				chat.dispose();
				this.interrupt();
				
			}// end public void run
			
		}.start();
				
	}// end init();
	
	/**
	 * Envia pacotes ao servidor, sinalizando que esta conectado. 
	 * Responsável por fechar o socket caso o cliente nao esteja logado.
	 */
	public void ping(){
		
		new Thread(){
			
			public void run(){
				
				byte[] buf = new byte[FRAME_SIZE];
				DatagramPacket packet = null;
				InetAddress address = null;
				String  message = null;
				message = PING+"#"+chat.getNickname();
								
				while(loginStatus){
					
					try {
						
						buf = message.getBytes();
						address = InetAddress.getByName(chat.getServerIP());
						packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(chat.getServerPort()));
						
						socket.send(packet);
						
						sleep(PING_INTERVAL);
						
					} catch (Exception e) {
						System.err.println("Client " + socket.getLocalSocketAddress() + " (ping) " + e.getMessage()); 
					}
					
				}
				
				socket.close();
				this.interrupt();
				
			}
			
		}.start();
		
	}
	
	/**
	 * Envia uma mensagem genérica ao servidor.
	 * @param dest
	 * @param data
	 * @throws IOException 
	 */
	public void sendData(String data) throws IOException{
		byte[] buf = new byte[FRAME_SIZE];
		buf = data.getBytes();
		InetAddress addr = InetAddress.getByName(chat.getServerIP());
		int port = Integer.parseInt(chat.getServerPort());
		DatagramPacket toSend = new DatagramPacket(buf, buf.length, addr, port);
		
		System.out.println("Client " + socket.getLocalAddress() + " send (data) :" + data);
		
		socket.send(toSend);
	}
	
	public void requestLogout(){
		try {
        	if(loginStatus){
        		BnUdpLogin.getInstance().logoutAttempt(chat.getNickname(), chat.getServerIP(), Integer.parseInt(chat.getServerPort()));
        	}else{ 
        		System.err.println("Desconectado(a)");
        	}	
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
