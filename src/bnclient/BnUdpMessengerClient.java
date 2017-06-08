package bnclient;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JOptionPane;

import bnguiclient.BnUdpTelaClienteChat;
import bnguiclient.BnUdpTelaClienteJogo;
import bnlogin.BnUdpLogin;
import bnprotocol.BnUdpServerProtocolInterface;

public class BnUdpMessengerClient extends AbstractClient {
	
	private static BnUdpMessengerClient INSTANCE = null;
			
	private BnUdpTelaClienteChat chat = null;
	
	private BnUdpTelaClienteJogo jogo = null;
	
	public BnUdpMessengerClient getInstance(){
		if(INSTANCE == null)
			INSTANCE = new BnUdpMessengerClient();
		return INSTANCE;
	}
	
	public BnUdpMessengerClient(BnUdpTelaClienteChat chat){
		this.chat = chat;
	}
	
	public BnUdpMessengerClient(){ 
		// void
	}
	
	/**
	 * Inicia escuta do cliente. Espera os dados transmitidos e os exibe usando a referencia para
	 * a instancia de chat.
	 */
	public void startListning() {
		
		new Thread(){
			
			public void run(){
				
				System.out.println("Cliente escutando na porta " + socket.getPort());
												
				while(loginStatus){
					
					try {
																			
						byte[] buffer = new byte[FRAME_SIZE];
						DatagramPacket requestData = new DatagramPacket(buffer, buffer.length);
						socket.receive(requestData);
						
						if(socket.getBroadcast()) {
														
							try {
																
								String requestString = new String(requestData.getData()).trim();
								System.out.println("Client recebeu " + requestString);
								String[] split = requestString.split("#");
								
								switch (split[0]) {
								case SEND_MESSAGE:							
									String chatString = split[2]+" to "+split[1]+": "+split[3]+"\n";
									chat.gettChat().setText(chat.gettChat().getText()+chatString);
									break;
								
								case BnUdpServerProtocolInterface.LOGOUT_ACK:
									loginStatus = false;
									chat.gettChat().setText(chat.gettChat().getText()+"\n"+"Voce foi desconectado (a) . . .");
									System.out.println("Voce foi desconectado (a) . . . ");
									if(JOptionPane.showConfirmDialog(null, "Deseja conectar-se novamente ?", "Desconectado",
											JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
										chat.retryLogin();
									}
									break;
									
								case LIST_CONNECTED:					
									System.out.println("Lista de clientes ativos:");
									
									String clientString = "";
									for(int i = 1; i < split.length; i++){
										listOfconnections.add(split[i]);
										System.out.println("\n"+split[i]);
										clientString = clientString + split[i]+"\n";
									}
									chat.gettUsuarios().setText(clientString);
									break;
									
								case UNKNOWN_USER: 
									chat.gettChat().setText(chat.gettChat().getText()+"Usuario inexistente ou desconectado (a). . .\n");
									System.out.println("Usuario inexistente ou desconectado (a) . . .");
									break;				
									
								case BnUdpLogin.TIMED_OUT:
									chat.gettChat().setText(chat.gettChat().getText()+"Nao foi possivel desconectar  . . .\n");
									System.out.println("Nao foi possivel desconectar . . .");
									break;
									
								case MATCH_REQUEST_ACK:
									chat.gettChat().setText(chat.gettChat().getText()+"Voce este na fila de espera para jogar . . . \n");
									System.out.println("Voce esta na fila de espera para jogar . . .");
									break;
								
								case LEAVE_GAME_ACK:
									chat.gettChat().setText(chat.gettChat().getText()+"Voce desistiu da partida.\n");
									System.out.println("Voce desistiu da partida.");
									chat.getbDesistir().setEnabled(false);
									chat.getbJogar().setEnabled(true);
									jogo.setVisible(false);
									break;
								
								case MATCHMAKING:
									chat.gettChat().setText(chat.gettChat().getText()+"Voce entrou em uma partida.\n");
									System.out.println("Voce entrou em uma partida."+INSTANCE);
									chat.getbJogar().setEnabled(false);
									chat.getbDesistir().setEnabled(false);
									jogo = new BnUdpTelaClienteJogo(chat.getServerIP(), Integer.parseInt(chat.getServerPort()), chat.getNickname() );
									jogo.setVisible(true);  
									
									break;
								
								case LEAVE_QUEUE_ACK:
									chat.gettChat().setText(chat.gettChat().getText()+"Voce saiu da fila de espera da partida.\n");
									System.out.println("Voce saiu da fila de espera da partida.");
									
									break;

								default:
									break;
								}
								
								
							} catch (Exception e) {
								System.err.println("Cliente erro:" + e.getMessage());
							} catch (Throwable e) {
								System.err.println("Cliente erro:" + e.getMessage());
								e.printStackTrace();
							}
							
						}
													
					} catch (Exception e) {
						System.err.println("Cliente " + e.getMessage());
					}
				}
				
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
						System.err.println("Cliente (ping) " + e.getMessage()); 
					}
					
				}
				
			}
			
		}.start();
		
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
