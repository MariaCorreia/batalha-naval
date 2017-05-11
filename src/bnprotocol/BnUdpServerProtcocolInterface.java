package bnprotocol;

/**
 * Esta interface define o protocolo de comunicacao por parte do servidor.
 * @author levymateus
 *
 */
public interface BnUdpServerProtcocolInterface extends BnUdpProtocolInterface {
	
	/**
	 * Porta padrao do servidor
	 */
	public static final int STD_SERVER_PORT = 10000;
	
	/**
	 * C�digo que o servidor retorna em caso de conexao aceita.
	 */
	public static final String CONNECTION_ACCEPTED = "40";
	
	/**
	 * C�digo que o servidor retorna em caso de logout aceito
	 */
	public static final String ACK_LOGOUT = "41";
	
	/**
	 * C�digo que o servido retorna quando envia a lista de conectados.
	 */
	public static final String LIST_CONNECTED = "42";
	
	/**
	 * C�digo que o servido retorna quando o nome de login � invalido.
	 */
	public static final String INVALID_LOGIN = "80";
	
	/**
	 * C�digo que o servido retorna quando a mensagem enviado ao servidor � invalida
	 * ...
	 */
	public static final String INVALID_MESSAGE = "81";
        
        /*
        *Código que o servidor retorna quando o nome do usuário enviado já existe.
        */
        public static final String REPEATED_NAME = "82";
		
		
}
