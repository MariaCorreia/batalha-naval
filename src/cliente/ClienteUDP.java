package cliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import fcontrol.ControlFrame;

public class ClienteUDP {
	
	private DatagramSocket socket = null;
	
	private int frameSize = 256; // definida por padrão
	
	public static ControlFrame LOGIN_STATUS = null;
	
	public ClienteUDP(){
		
	}
	
	public ControlFrame login(String nickname, String addrString, int port){
		
		int attempt = 0;
		
		while(attempt <= 5){
			try {
				
				socket = new DatagramSocket();
				String message = "1#"+nickname;
				byte[] data = message.getBytes();
				InetAddress address = InetAddress.getByName(addrString);
				DatagramPacket request = new DatagramPacket(data, data.length, address, port);
				
				byte[] buffer = new byte[frameSize];
				byte[] byteReply = null;
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
													
				socket.send(request);
				socket.setSoTimeout(2000);
				socket.receive(reply);
				byteReply = reply.getData();
				System.out.println("request received:"+reply.getData());

				if(byteReply[0] == 0){
					System.out.println("Conexao aceita !");
					LOGIN_STATUS = ControlFrame.CONNECTION_RESPONSE;
					break;
				}
				
				if(byteReply[0] == -1){
					System.out.println("Este nome já está em uso !");
					LOGIN_STATUS = ControlFrame.INVALID_NAME;
					break;
				}
				
			} catch (Exception e) {
				System.err.println("Erro: " + e.getMessage());
				attempt++;
			}
			
			LOGIN_STATUS = ControlFrame.INVALID_NAME;
		}
		
		return LOGIN_STATUS;
		
	}
	
}
