package bnclient;

import java.net.DatagramSocket;
import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpServerProtcocolInterface;

/**
 * 
 * @author levymateus
 *
 */
public class AbstractClient implements BnUdpServerProtcocolInterface, BnUdpClientProtocolInterface{
	
	/*
	 * 
	 */
	protected static DatagramSocket socket = null;
	
	/**
	 * Retornado pelo método loginAttempt em caso de tempo limite exedido.
	 * Tempo limite acontece quando se esgotam as tentativas máximas de conexão com o 
	 * servidor.
	 */
	public static final String TIMED_OUT = "-1";
	
	
	protected boolean loginStatus = true;

}
