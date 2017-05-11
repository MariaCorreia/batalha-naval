package cliente.login.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cliente.ClienteUDP;
import cliente.GUIClienteUDP;
import fcontrol.ControlFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Action;

@SuppressWarnings("serial")
public class GUILoginUDP extends JFrame {

	private JPanel contentPane;
	
	private ClienteUDP cliente = null;
	
	private GUIClienteUDP guiCliente = null;
	
	private JTextField txtField_Login;
	
	private JTextField textField_IP;
	
	private JTextField textField_Port;
	
	private final Action actionLogin = new ActionLogin();

	public GUILoginUDP(ClienteUDP clienteUDP, GUIClienteUDP guiClienteUDP) {
		cliente = clienteUDP;
		guiCliente = guiClienteUDP;
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
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setAction(actionLogin);
		btnLogin.setBounds(131, 163, 89, 23);
		contentPane.add(btnLogin);
		
		textField_IP.setText("localhost");
		textField_Port.setText("7000");
	}
	
	/**
	 * Checa todos os campos 
	 * @return Retorna um Vector de c�digos de erro no formul�rio
	 */
	private Vector<LoginControl> checkField(){
		Vector<LoginControl> ctrl = new Vector<LoginControl>(3);
		if(txtField_Login.getText().isEmpty()){
			ctrl.addElement(LoginControl.INVALID_LOGIN);
		}
		if (textField_IP.getText().isEmpty()){
			ctrl.addElement(LoginControl.INVALID_IP);
		}
		if (textField_Port.getText().isEmpty()){
			ctrl.addElement(LoginControl.INVALID_PORT);
		}
		return ctrl;
	}
	
	public boolean checkFields(){
		Vector<LoginControl> loginCheck = checkField();
		if(!loginCheck.isEmpty()){
			showMessage(loginCheck, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private void showMessage(Vector<LoginControl> ctrl, int optionType){
		String message = "Erros: \n";
		for (int i = 0; i < ctrl.size() && ctrl != null; i++) {
			message += ctrl.get(i).name() + "\n";
		}
		JOptionPane.showMessageDialog(null, message);
	}
	
	private void showMessage(LoginControl ctrl){
		JOptionPane.showMessageDialog(null, "Erro: " + ctrl);
	}
	
	/**
	 * Faz controle dos frames de resposta vindos do servidor.
	 * @param ctrl - Flag de controle
	 * @return Retorna true se o login for feito com sucesso ou falso caso contr�rio.
	 */
	private void action(ControlFrame ctrl){
		switch (ctrl) {
		case INVALID_NAME: showMessage(LoginControl.INVALID_LOGIN);
			break;
			
		case CONNECTION_RESPONSE: upCliente();
		
		case TIME_OUT: JOptionPane.showMessageDialog(null, "Connection timed out !"); 
			break;

		default: 
			break;
		}
	}

	private void upCliente(){
		setVisible(false);; // janela GUILoginUDP � escondida
		guiCliente.setVisible(true); // janela do cliente � mostrada
	}
	
	private class ActionLogin extends AbstractAction {	
		public ActionLogin() {
			putValue(NAME, "Login");
		}
		public void actionPerformed(ActionEvent e) {
			if(!checkFields()) return;
			String nickname = txtField_Login.getText();
			String ip = textField_IP.getText();
			int port = new Integer(textField_Port.getText());
			action(cliente.login(nickname, ip, port));
		}
	}
}
