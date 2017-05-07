package bnlogin;

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
public class BnUdpLogin implements BnUdpProtocolInterface {
	
	private static BnUdpLogin INSTANCE = null;
		
	/**
	 * Retornado pelo método loginAttempt em caso de tempo limite exedido.
	 * Tempo limite acontece quando se esgotam as tentativas máximas de conexão com o 
	 * servidor.
	 */
	public static final String TIMED_OUT = "-1";
	
	/**
	 * Numero m�ximo de tentativas de conexão.
	 * Por padrão é 5.
	 */
	private int connectionAttempt = 5;

	/**
	 * Tempo m�ximo para resposta do servidor em milisegundos.
	 * Por padrao 1000 ms.
	 */
	private int timeOut = 1000;
	
	public static BnUdpLogin getInstance(){
		if(INSTANCE == null) {
	         INSTANCE = new BnUdpLogin();
	      }
	      return INSTANCE;
	}
	
	public BnUdpLogin() {}

	/**
	 * Este método faz uma tentantiva de bnlogin UDP seguindo o protocolo implementado.
	 * @param nickname - Nome de usuario
	 * @param addr - Endereco IP do servidor
	 * @param port - Porta de escuta do servidor
	 * @return Retorna uma String que representa a resposta do servidor ou TIMED_OUT. 
	 * @see BnUdpProtcocolInterface
	 * @throws SocketException
	 */
	public String loginAttempt(String nickname, String addr, int port) throws SocketException{
		
		DatagramSocket socket = new DatagramSocket();
		String type = BnUdpClientProtocolInterface.CONNECTION_REQUEST;
		String message = type+"#"+nickname;
		
		return attempt(message, addr, port, socket);
		
	}
	
	/**
	 * Este método faz uma tentantiva de logout UDP seguindo o protocolo implementado.
	 * @param nickname - Nome de usuario
	 * @param addr - Endereco IP do servidor
	 * @param port - Porta de escuta do servidor
	 * @return Retorna uma String que representa a resposta do servidor ou TIMED_OUT. 
	 * @see BnUdpProtcocolInterface
	 * @throws SocketException
	 */
	public String logoutAttempt(String nickname, String addr, int port) throws SocketException{
		
		DatagramSocket socket = new DatagramSocket();
		String type = BnUdpClientProtocolInterface.LOGOUT;
		String message = type+"#";
		
		return attempt(message, addr, port, socket);
		
	}
	
	private String attempt(String message, String addr, int port, DatagramSocket socket){
		
		int attempt = 1;
		
		while(attempt <= connectionAttempt){
			
			try {
				
				byte[] data = message.getBytes();
				InetAddress address = InetAddress.getByName(addr);
				DatagramPacket request = new DatagramPacket(data, data.length, address, port);
				
				byte[] buffer = new byte[FRAME_SIZE];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
											
				System.out.println("Client " + socket.getLocalSocketAddress() + 
						" send (data): " + message);
				
				socket.send(request);
				socket.setSoTimeout(timeOut);
				socket.receive(reply);
				
				String replyString = new String(reply.getData());
								
				System.out.println("Client " + socket.getLocalSocketAddress() + 
						" reply (data): " + replyString.substring(0, 3));
				
				socket.close();
				
				return replyString.substring(0, 2);
				
			} catch (Exception e) {
				System.err.println(e.getMessage() + " - attempt: " + attempt);
				attempt++;
			}

		}
		
		socket.close();
				
		return TIMED_OUT;
	}
	
	public int getConnectionAttempt() {
		return connectionAttempt;
	}

	public void setConnectionAttempt(int connectionAttempt) {
		this.connectionAttempt = connectionAttempt;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
		
}
