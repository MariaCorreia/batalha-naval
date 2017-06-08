package bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Stack;

import bnprotocol.BnUdpServerProtocolInterface;

public class BnUdpGameServer extends BnUdpAbstractServer {
	
	//private BnUdpGameServer INSTANCE = null;
	
    private BnUdpServer server = null;
	
    /**
     * Lista/Fila de clientes que esperam uma partida
     */
    protected Stack<BnUdpClientNode> waitingToPlay = null;
    
    protected LinkedList<BnMatch> matchList = null;
    
    /*public BnUdpGameServer getInstance(){
    	if(INSTANCE == null){
    		INSTANCE = new BnUdpGameServer();
    	}
    	if (INSTANCE!=null){
    		System.out.println("true");
    	}
    	return INSTANCE;
    }
    
    private BnUdpGameServer (){
    	waitingToPlay = new Stack<BnUdpClientNode>();
    }*/
    
    public BnUdpGameServer (BnUdpServer server){
    	this.waitingToPlay = new Stack<BnUdpClientNode>();
        this.server = server;
    }
    
    public void init(){
    	
    	new Thread(){
    		
    		public void run(){
    			
    			BnUdpClientNode c1 = null;
    			BnUdpClientNode c2 = null;
    			
    			while(true){
    				
    				//System.out.println(waitingToPlay.size());
    				if(waitingToPlay.size() >=2){
    					
    					for (int i = 0; i < waitingToPlay.size(); i = i + 2) {

    						c1 = waitingToPlay.pop();
    						c2 = waitingToPlay.pop();
    						
							//matchList.add(new BnMatch(c1, c2) );
						
						}
    					
    					String message;
    					byte[] buffer;
    					DatagramPacket dt1, dt2;
    					message = BnUdpServerProtocolInterface.MATCHMAKING+"#"+c1.getName();
						buffer = message.getBytes();
						dt1 = new DatagramPacket(
								buffer, 
								buffer.length, 
								c2.getAddress(), 
								c2.getPort()
								);
						
						message = BnUdpServerProtocolInterface.MATCHMAKING+"#"+c2.getName();
						buffer = message.getBytes();
						dt2 = new DatagramPacket(
								buffer, 
								buffer.length, 
								c1.getAddress(), 
								c1.getPort()
								);
						
						
						System.out.println("Server enviou " + message);
						
						try {
							server.socket.send(dt1);
							server.socket.send(dt2);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					
    				}
    				
    			}
    			
    		}
    		
    	}.start();
    	
    }
    
    protected void printClientList(){
    	
    	for (int i = 0; i < waitingToPlay.size(); i++) {
             System.out.println("lista de clientes na fila de espera:");
			System.out.println(waitingToPlay.get(i).name);
		}
    	
    }

}
