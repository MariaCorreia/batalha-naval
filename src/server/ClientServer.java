package server;

import java.net.InetAddress;

/**
 * Abstracao do cliente conectado ao servidor
 * @author levymateus
 *
 */
public class ClientServer {
	
	private String name;
	
	private String port;
	
	public static int id;
	
	private InetAddress host;
	
	public ClientServer(String name, String port, InetAddress host){
		id += 1;
		this.name = name;
		this.port = port;
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		ClientServer.id = id;
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}
	
}
