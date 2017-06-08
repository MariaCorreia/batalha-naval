package bnclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpServerProtocolInterface;

/**
 * 
 * @author levymateus
 *
 */
public class AbstractClient implements BnUdpServerProtocolInterface, BnUdpClientProtocolInterface{
	
	/*
	 * Socket do cliente
	 */
	protected static DatagramSocket socket = null;
	
	/**
	 * Retornado pelo m�todo loginAttempt em caso de tempo limite exedido.
	 * Tempo limite acontece quando se esgotam as tentativas máximas de conexão com o 
	 * servidor.
	 */
	public static final String TIMED_OUT = "-1";
	
	/**
	 * Status de login do cliente
	 */
	protected boolean loginStatus = true;
	
	/**
	 * 
	 */
	protected ArrayList<String> listOfconnections = new ArrayList<>(MAX_NUMBER_CONNECTED);
	
	/**
	 * Envia uma mensagem genenerica ao servidor.
	 * @param dest
	 * @param data
	 * @throws IOException 
	 */
	public static final void sendData(String data, String ip, int port) throws IOException{
		byte[] buf = new byte[FRAME_SIZE];
		buf = data.getBytes();
		InetAddress addr = InetAddress.getByName(ip);
		//int port = Integer.parseInt(chat.getServerPort());
		DatagramPacket toSend = new DatagramPacket(buf, buf.length, addr, port);
		
		System.out.println("Client enviou " + data);
		
		socket.send(toSend);
	}
	
}
