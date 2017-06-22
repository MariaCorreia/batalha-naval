package bnprotocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * BatalhaNavalProtocoloInterface
 * 
 * @author levymateus
 *
 */
public interface BnUdpProtocolInterface {
	
	/**
	 * tamanho padrão do frame o qual o servidor espera
	 */
	public static final int FRAME_SIZE = 512;
	
	/**
	 * Envia uma mensagem genenerica.
	 * @param dest
	 * @param data
	 * @throws IOException 
	 */
	public static void sendData(String data, String ip, int port, DatagramSocket socket) throws IOException{
		byte[] buf = new byte[FRAME_SIZE];
		buf = data.getBytes();
		InetAddress addr = InetAddress.getByName(ip);
		//int port = Integer.parseInt(chat.getServerPort());
		DatagramPacket toSend = new DatagramPacket(buf, buf.length, addr, port);		
		socket.send(toSend);
	}
	
}
