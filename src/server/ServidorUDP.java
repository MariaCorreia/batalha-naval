package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import javax.sound.midi.Soundbank;

import fcontrol.ControlFrame;

public class ServidorUDP {
	
	private DatagramSocket socket = null;
	
	/**
	 * Fila de clientes conectados ao servidor.
	 */
	private ArrayList<Object> clientList = null;
	
	private GUIServidorUDP guiServidor = null;
	
	private int frameLenght = 1024;
		
	public ServidorUDP(GUIServidorUDP guiServidor){
		System.out.println("Hello !");
		clientList = new ArrayList<Object>(20);
	}
	
	public void printClienteList(){
		System.out.println("Client list:");
		for (int i = 0; i < clientList.size(); i++) {
			ClientServer obj = (ClientServer) clientList.get(i);
			System.out.println("[" + i +"]" + " " + obj.getName());
		}
	}
	
	/**
	 * Checa a lista de cliente conectados. 
	 * @param arg0 - Objeto a ser verificado
	 * @return Caso já exista o cliente retorna true 
	 * o contrário retorna false.
	 */
	private boolean haveObjectInList(ClientServer arg0){
		for (int i = 0; i < clientList.size(); i++) {
			ClientServer obj = (ClientServer) clientList.get(i);
			if(obj.getId() ==  arg0.getId())
				return true;
		}
		return false;
	}
	
	public void start(){
		
		new Thread(){
			
			public void run(){
				
				System.out.println("Thread: " + getId());
				
				try {
					
					socket = new DatagramSocket(7000);
					byte[] buffer = new byte[frameLenght];
					byte[] ctrl = new byte[frameLenght];
					
					while(true){
						
						//System.out.println("waiting ...");
						
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						
						Token dataTokens = new Token(request);
						
						switch (dataTokens.getType()) {
						case '1':
							
							byte[] response = new byte[frameLenght];
							// add in list
							ClientServer client = new ClientServer(dataTokens.getContent(), dataTokens.getPort(), dataTokens.getAddress());
							if(!haveObjectInList(client)){
								clientList.add(client);
								response[0] = 0;
							}else{
								response[0] = -1;
							}
							
							printClienteList();
														
							DatagramPacket reply = new DatagramPacket(response, 
									request.getLength(), request.getAddress(), request.getPort());
							socket.send(reply);
							
							break;

						default:
							break;
						}
						
					}
					
				} catch (Exception e) {
					System.out.println("Erro: " + e.getMessage() );
				}
				
			}
			
		}.start();
		
	}
		
}
