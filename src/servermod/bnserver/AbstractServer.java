package servermod.bnserver;

import java.net.DatagramSocket;
import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpServerProtocolInterface;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class AbstractServer implements BnUdpServerProtocolInterface, BnUdpClientProtocolInterface{
	
	protected int port = 10000;
	
	protected DatagramSocket socket = null;
	
	protected ArrayList<BnUdpClientNode> clientList;
        
        protected DefaultTableModel clientTableModel;

}
