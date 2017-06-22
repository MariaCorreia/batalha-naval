package servermod.bnserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Stack;

import com.sun.istack.internal.NotNull;

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
    	this.matchList = new LinkedList<BnMatch>();
        this.server = server;
    }
    
    public void init(){
    	
    	new Thread(){
    		
    		public void run(){
    			
    			BnUdpClientNode c1 = null;
    			BnUdpClientNode c2 = null;
    			
    			while(true){
    				
    				if(waitingToPlay.size() >=2){
    					
    					for (int i = 0; i < waitingToPlay.size(); i = i + 2) {

    						c1 = waitingToPlay.pop();
    						c2 = waitingToPlay.pop();
    						
							matchList.add(new BnMatch(c1, c2) );
							showMatchList();
							//TODO: desenhar tela de log da partida
							
													
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
							e.printStackTrace();
						}
    					
    				}
    				
    				try {
    					byte[] buf = new byte[FRAME_SIZE];
        				DatagramPacket dpkt = new DatagramPacket(buf, buf.length);
        				socket.receive(dpkt);
        				String split[] = new String(dpkt.getData()).split("#");
        				switch (split[0]) {
    					case LEAVE_QUEUE:
    						System.out.println("Matrix accepted !");
    						break;

    					default:
    						break;
    					}
					} catch (Exception e) {
					}
    				
    			}
    			
    		}
    		
    	}.start();
    	
    }
    
    protected void printClientList(){
    	System.out.println("lista de clientes na fila de espera:");
    	for (int i = 0; i < waitingToPlay.size(); i++) {
			System.out.println(waitingToPlay.get(i).name);
		}
    	
    }
    
    protected void showMatchList(){
    	System.out.println("Lista de partidas ativas");
    	for (int index = 0; index < matchList.size(); index++) {
			System.out.println("Partida [" + index + "]\nplayer 1 " + matchList.get(index).player_1.name);
			System.out.println("player 2 " + matchList.get(index).player_2.name);
		}
    }
    
    /**
     * Procura uma partida pelo nome de um dos jogadores que compoem a mesma.
     * @param player - nome de um jogador que compõe a partida
     * @return um objeto BnMatch referente a partida onde esta o jogador "player"
     */
    protected BnMatch searchMatch(@NotNull String player){
    	if(player == null) 
    		return null;
    	for (int index = 0; index < matchList.size(); index++) {
			if(matchList.get(index).player_1.name.equals(player) || matchList.get(index).player_2.equals(player))
				return matchList.get(index);
			
		}
		return null;
    }
    
    /**
     * Remove a partida da lista de partidas listMatch.
     * @param player - nome de um dos jogadores que compõem a partida.
     */
    protected void removeMatch(String player){
    	for (int index = 0; index < this.matchList.size(); index++) {
			if(matchList.get(index).player_1.equals(player) || matchList.get(index).player_2.equals(player)){
				matchList.remove(index);
			}
		}
    }

}
