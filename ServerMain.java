import java.io.IOException;
import java.net.SocketException;

import bnserver.BnUdpServer;

public class ServerMain {
	
	public static void main(String[] args) {
		
		String port = args[0];
		System.out.println("Server port:" + port);
		BnUdpServer server = new BnUdpServer();
		server.setPort(Integer.parseInt(port));
		try {
			server.run();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
}
