package servermod.bnserver;

import java.net.InetAddress;

public class BnUdpClientNode {
	
	/**
	 * E true se o client estiver jogando e false se ao contrario.
	 */
	protected boolean inMatch;
	
	/**
	 * Verdadeiro se for a vez do jogador e false ao contrário
	 */
	protected boolean isYourTurn;
	
	protected String name;
	
	protected int port;
	
	protected InetAddress address; 
	
	/**
	 * tempo do ultimo ping enviado ao servidor
	 */
	private long lastPing;
	
	/**
	 * Matriz referente ao jogo
	 */
	private BnMatrix gameMatrix;
	
	public BnUdpClientNode(String name, int port, InetAddress address){
		this.inMatch = false;
		this.isYourTurn	= false;
		this.name = name;
		this.port = port;
		this.address = address;
		this.gameMatrix = null;
		this.lastPing = System.currentTimeMillis();
	}
	
	/**
	 * Atualiza o ultimo ping e retorna a diferença entre o ultimo ping e o ping atual.
	 * @param currentPing
	 * @return Diferença entre o ultimo ping e o ping atual.
	 */
	protected long lastPing(){
		
		long currentTime = System.currentTimeMillis();
		long difference = currentTime - this.lastPing;
		this.lastPing = currentTime;
		
		return difference;
	}

	public boolean isArePlaying() {
		return inMatch;
	}

	public void setArePlaying(boolean arePlaying) {
		this.inMatch = arePlaying;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public long getLastPing() {
		return lastPing;
	}

	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}

	public BnMatrix getGameMatrix() {
		return gameMatrix;
	}

	public void setGameMatrix(BnMatrix gameMatrix) {
		this.gameMatrix = gameMatrix;
	}

}
