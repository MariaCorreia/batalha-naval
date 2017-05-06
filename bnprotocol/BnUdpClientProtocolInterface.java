package bnprotocol;

/**
 * BatalhaNavalClienteProtocoloInterface
 * 
 * Esta interface define o protocolo de comunicação por parte do cliente
 * @author levymateus
 *
 */
public interface BnUdpClientProtocolInterface extends BnUdpProtocolInterface {
	
	/**
	 * Código enviado para requisitar um bnlogin.
	 */
	public static final String CONNECTION_REQUEST = "00";
	
	/**
	 * Código enviado para requisitar logout.
	 */
	public static final String LOGOUT = "01";

	/**
	 * Código enviado para envio de mensagem.
	 */
	public static final String SEND_MESSAGE = "02";
	
}
