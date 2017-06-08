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
	 * Tamanho máximo para nome de usuário
	 */
	public static final int NICKNAME_SIZE = 16;
	
	/**
	 * Tempo padrão em que o cliente envia um pacote de feedback
	 * em milisegundos
	 */
	public static final int PING_INTERVAL = 5000;
	
	/**
	 * Código enviado para requisitar um bnlogin.
	 */
	public static final String CONNECTION_REQUEST = "00";
	
	/**
	 * Código enviado para requisitar logout.
	 */
	public static final String LOGOUT = "01";

	/**
	 * Palavra reservada para mensagem em broadcast
	 */
	public static final String BROADCAST_MSG = "all";
	
	/**
	 * Código enviado para envio de mensagem.
	 */
	public static final String SEND_MESSAGE = "02";
	
	/**
	 * Enviado como sinal de status de conexão.
	 */
	public static final String PING = "03";
	
	/**
	 * Requesicao para partida.
	 */
	public static final String REQUEST_MATCH = "20";
	
	/**
	 * Requisicao para sair da partia.
	 */
	public static final String LEAVE_GAME = "21";
	
	/**
	 * Envio da matriz de jogo
	 */
	public static final String SEND_MATRIX = "22";
	
	/**
	 * Requisicao de saida da fila de espera para jogar.
	 */
	public static final String GET_OUT_QUEUE = "23";
	
}
