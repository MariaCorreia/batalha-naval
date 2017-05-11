package testes;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bnlogin.BnUdpLogin;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.net.SocketException;
import javax.swing.Action;

public class GuiUdpLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField txtField_Login;
	
	private JTextField textField_IP;
	
	private JTextField textField_Port;
	
	private JButton btnLogin;
	
	private final Action actionLogin = new ActionLogin();

	public GuiUdpLogin() {
		init();
		this.setVisible(true);
	}
	
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 236);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(123, 11, 46, 14);
		contentPane.add(lblLogin);
		
		txtField_Login = new JTextField();
		txtField_Login.setBounds(67, 36, 153, 20);
		contentPane.add(txtField_Login);
		txtField_Login.setColumns(10);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(10, 79, 46, 14);
		contentPane.add(lblIp);
		
		textField_IP = new JTextField();
		textField_IP.setBounds(67, 76, 153, 20);
		contentPane.add(textField_IP);
		textField_IP.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 125, 46, 14);
		contentPane.add(lblPort);
		
		textField_Port = new JTextField();
		textField_Port.setBounds(67, 122, 153, 20);
		contentPane.add(textField_Port);
		textField_Port.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setAction(actionLogin);
		btnLogin.setBounds(131, 163, 89, 23);
		contentPane.add(btnLogin);
		
		textField_IP.setText("localhost");
		textField_Port.setText("7000");
	}
	
	private class ActionLogin extends AbstractAction {	
				
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public ActionLogin() {
			putValue(NAME, "Login");
		}
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(){
				public void run(){
					try {
						BnUdpLogin.getInstance().loginAttempt(txtField_Login.getText(), "localhost", 10000);
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
			}.start();
			
		}
	}
}
