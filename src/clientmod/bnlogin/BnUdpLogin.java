package clientmod.bnlogin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpProtocolInterface;

/**
 * Esta classe se encarrega de fazer o bnlogin com um servidor utilizando o protocolo UDP.
 * @author levymateus
 *
 */
public class BnUdpLogin extends clientmod.bnclient.AbstractClient implements BnUdpProtocolInterface {
	
	private static BnUdpLogin INSTANCE = null;
	
	/**
	 * Numero máximo de tentativas de conexão.
	 * Por padrão é 1.
	 */
	private int connectionAttempt = 1;
	
	public static BnUdpLogin getInstance(){
		if(INSTANCE == null) {
	         INSTANCE = new BnUdpLogin();
	      }
	      return INSTANCE;
	}
	
	public BnUdpLogin() {
	}

	/**
	 * Este método faz uma tentantiva de login. Este método cira um socket do cliente 
	 *  envia uma requisição de login e espera uma resposta do servidor.
	 * @param nickname - Nome de usuario
	 * @param addr - Endereco IP do servidor
	 * @param port - Porta de escuta do servidor
	 * @return Retorna uma String que representa a resposta do servidor ou TIMED_OUT. 
	 * @see BnUdpProtcocolInterface
	 * @throws SocketException
	 */
	public String loginAttempt(String nickname, String addr, int port) throws SocketException{
		
		socket = new DatagramSocket();
		
		String type = BnUdpClientProtocolInterface.CONNECTION_REQUEST;
		String message = type+"#"+nickname;
		
		int attempt = 1;
		
		while(attempt <= connectionAttempt){
			
			try {
				
				byte[] data = message.getBytes();
				InetAddress address = InetAddress.getByName(addr);
				DatagramPacket request = new DatagramPacket(data, data.length, address, port);
				
				byte[] buffer = new byte[FRAME_SIZE];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
											
				System.out.println("Cliente enviou " + message);
				
				socket.send(request);
				socket.receive(reply);
				
				String replyString = new String(reply.getData());
								
				System.out.println("Cliente recebeu " + replyString.substring(0, 3));
								
				return replyString.substring(0, 2);
				
			} catch (Exception e) {
				System.err.println(e.getMessage() + " - attempt: " + attempt);
				attempt++;
			}

		}
						
		return TIMED_OUT;
		
	}
	
	/**
	 * Este método envia uma requisição de logout.
	 * @param nickname - Nome de usuario
	 * @param addr - Endereco IP do servidor
	 * @param port - Porta de escuta do servidor
	 * @return Retorna uma String que representa a resposta do servidor ou TIMED_OUT. 
	 * @see BnUdpProtcocolInterface
	 * @throws SocketException
	 */
	public void logoutAttempt(String nickname, String addr, int port) throws SocketException{
		
		String type = BnUdpClientProtocolInterface.LOGOUT;
		String message = type+"#"+nickname;
		
		try {
			
			byte[] data = message.getBytes();
			InetAddress address = InetAddress.getByName(addr);
			DatagramPacket request = new DatagramPacket(data, data.length, address, port);
			
			System.out.println("Client enviou " + message);
			
			socket.send(request);
			
		} catch (Exception e) {
			System.err.println("Client (logout) " + socket.getLocalSocketAddress() + e.getMessage() );
		}
				
	}
	
	public int getConnectionAttempt() {
		return connectionAttempt;
	}

	public void setConnectionAttempt(int connectionAttempt) {
		this.connectionAttempt = connectionAttempt;
	}
		
}
