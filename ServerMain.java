import java.io.IOException;
import java.net.SocketException;

import bnserver.BnUdpServer;

public class ServerMain {
	
	public static void main(String[] args) {
		
		BnUdpServer server = new BnUdpServer();
		try {
			server.run();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
}
