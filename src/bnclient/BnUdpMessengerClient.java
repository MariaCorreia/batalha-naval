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
												
				while(true){
					
					try {
																			
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						
						if(socket.getBroadcast()) {
							
							System.out.println(" received ");
							
							try {
								
								System.out.println("Client " + getName() + " id:" + getId());
								
								String requestString = new String(requestData.getData()).trim();
														
								System.out.println("Client " + socket.getLocalSocketAddress() + " receive (data) " + requestString);
								
								String type = requestString.substring(0, 2);
								String message = null;
								DatagramPacket replyData = null;
								
								switch (type) {
								case SEND_MESSAGE:
									/**
									 *  012 3	.	.	.      n n+1 . .  m m+1 .
									 *  02# <destino | [all] > # <origem> # <msg>
									 */
									int n = 0;
									String aux = requestString.substring(3, requestString.length());
									String destiny = aux.substring(0, n = aux.indexOf("#"));
									aux = aux.substring(n + 1, n = aux.length());
									String sender = aux.substring(0, n = aux.indexOf("#"));
									String msg = aux.substring(n+1, aux.length());
									 
									System.out.println("Client " + socket.getLocalSocketAddress() + 
											" receive (data): " + msg + " from " + sender);
									
									String chatString = sender+":"+msg+"\n";
									chat.gettChat().setText(chat.gettChat().getText()+chatString);
									
									break;
								
								case BnUdpServerProtcocolInterface.ACK_LOGOUT:
									loginStatus = false;
									chat.gettChat().setText(chat.gettChat().getText()+"Voce foi desconectado (a) . . .\n");
									System.out.println("Voce foi desconectado (a) . . . ");
									chat.dispose();
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
							}
							
						}
													
					} catch (Exception e) {
						System.err.println("Client " + e.getMessage());
					}
				}
				
			}// end public void run
			
		}.start();
				
	}// end init();
	
	/**
	 * 
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
		//DatagramPacket toSend = new DatagramPacket(buf, buf.length, socket.getLocalAddress(), socket.getLocalPort());
		
		System.err.println("Client " + socket.getLocalAddress() + " send (data) :" + data);
		
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
