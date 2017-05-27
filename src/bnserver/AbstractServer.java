package bnserver;

import java.net.DatagramSocket;
import java.util.LinkedList;

import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpServerProtcocolInterface;

public class AbstractServer implements BnUdpServerProtcocolInterface, BnUdpClientProtocolInterface{
	
	protected int port = 10000;
	
	protected DatagramSocket socket = null;
	
	protected LinkedList<BnUdpClientNode> clientList;

}
