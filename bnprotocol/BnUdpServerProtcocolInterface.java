package bnprotocol;

/**
 * Esta interface define o protocolo de comunicacao por parte do servidor.
 * @author levymateus
 *
 */
public interface BnUdpServerProtcocolInterface extends BnUdpProtocolInterface {
	
	/**
	 * Código que o servidor retorna em caso de conexao aceita.
	 */
	public static final String CONNECTION_ACCEPTED = "40";
	
	/**
	 * Código que o servidor retorna em caso de tempo exedido.
	 */
	public static final String ACK_LOGOUT = "41";
	
	/**
	 * Código que o servido retorna quando estaviando a lista de conectados.
	 */
	public static final String LIST_CONNECTED = "42";
		
}
