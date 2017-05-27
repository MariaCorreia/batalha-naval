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
	 * Código que o servidor retorna em caso de logout aceito
	 */
	public static final String ACK_LOGOUT = "41";
	
	/**
	 * Código que o servido retorna quando envia a lista de conectados.
	 */
	public static final String LIST_CONNECTED = "42";
	
	/**
	 * Código que o servido retorna quando o nome de login é invalido.
	 */
	public static final String INVALID_LOGIN = "80";
	
	/**
	 * Código que o servido retorna quando a mensagem enviado ao servidor é invalida
	 * ...
	 */
	public static final String INVALID_MESSAGE = "81";
	
	/**
	 *  Código enviado do servidor indicando que um usuário não existe ou nao esta conectado
	 */
	public static final String UNKNOWN_USER = "82";
	
	/**
	 * Numero máximo permitido de conectados. 
	 * Baseado no tamanho de um FRAME_SIZE.
	 * ( (FRAME_SIZE - 3 ) / BnUdpClientProtocolInterface.NICKNAME_SIZE ) - 15
	 */
	public static final int MAX_NUMBER_CONNECTED = 16;
	
	/**
	 * Tempo em ms que o servidor atualiza a lista de clientes
	 */
	public static final int REFRESH_TIME = 20000;
		
}
