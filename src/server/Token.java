package server;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Separa os tokens de um frame.
 * @author Usuario
 *
 */
public class Token {
	
	private String data = null;
	
	private char type;
	
	private String content = null;
	
	private String port = null;
	
	private InetAddress address = null;
		
	public Token(DatagramPacket datagram){
		this.data = new String(datagram.getData());
		parse();
	}
	
	private void parse(){
		char[] aux = new char[data.length()];
		aux = data.toCharArray();
		type = aux[0];
		
		switch (type) {
		
		/**
		 * Conexao
		 */
		case '1': 
			content = new String(aux);
			content = content.substring(2);
			System.out.println("type: " + type + " data: " + content);
			break;
			
		/**
		 * Desconexao
		 */
		case '5':
			break;

		default:
			break;
		}
		
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
}
