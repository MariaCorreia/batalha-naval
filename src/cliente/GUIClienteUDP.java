package cliente;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cliente.login.gui.GUILoginUDP;

@SuppressWarnings("serial")
public class GUIClienteUDP extends JFrame {

	private JPanel contentPane;
	
	private ClienteUDP cliente = null;
		
	private JTextField textField;
	
	private final Action action = new ActionSend();

	public GUIClienteUDP() {
		init();
		cliente = new ClienteUDP();
		new GUILoginUDP(cliente, this).setVisible(true);
	}
	
	public void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTexto = new JLabel("Texto:");
		lblTexto.setBounds(10, 11, 46, 14);
		contentPane.add(lblTexto);
		
		textField = new JTextField();
		textField.setBounds(66, 8, 223, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setAction(action);
		btnSend.setBounds(312, 7, 89, 23);
		contentPane.add(btnSend);
	}
	
	private class ActionSend extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ActionSend() {
			putValue(NAME, "Submit");
			putValue(SHORT_DESCRIPTION, "submit text to server");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUIClienteUDP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
