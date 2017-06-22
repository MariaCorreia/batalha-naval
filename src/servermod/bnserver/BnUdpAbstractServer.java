package servermod.bnserver;

import java.net.DatagramSocket;
import bnprotocol.BnUdpClientProtocolInterface;
import bnprotocol.BnUdpServerProtocolInterface;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class BnUdpAbstractServer implements BnUdpServerProtocolInterface, BnUdpClientProtocolInterface{
	
	/**
	 * Porta usada pelo servidor. Se nao modificada: 10000 por padrao
	 */
	protected int port = 10000;
	
	/**
	 * socket do servidor
	 */
	protected DatagramSocket socket = null;
	
	/**
	 *  Lista de clientes conectados ao chat.
	 */
	protected ArrayList<BnUdpClientNode> clientList;
       
	/**
	 * 
	 */
    protected DefaultTableModel clientTableModel;

	public DatagramSocket getSocket() {
		return socket;
	}

}
