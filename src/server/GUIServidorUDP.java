package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

@SuppressWarnings("serial")
public class GUIServidorUDP extends JFrame {

	private JPanel contentPane;
	
	private JList<?> list = null;
	
	private ServidorUDP servidor = null;
	
	private final Action action = new ActionConnect();

	public GUIServidorUDP() {
		this.servidor = new ServidorUDP(this);
		this.init();
	}
	
	private void init() {
		this.setTitle("Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 468, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.list = new JList<Object>();
		list.setBounds(12, 22, 122, 227);
		contentPane.add(list);
		
		JLabel lblConnected = new JLabel("Connected");
		lblConnected.setBounds(12, 0, 122, 16);
		contentPane.add(lblConnected);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(146, 0, 181, 16);
		contentPane.add(lblLog);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(146, 23, 181, 226);
		contentPane.add(textArea);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(action);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(342, 223, 98, 26);
		contentPane.add(btnNewButton);
	}
	
	@SuppressWarnings("serial")
	private class ActionConnect extends AbstractAction {
		
		public ActionConnect() {
			putValue(NAME, "Connect");
			putValue(SHORT_DESCRIPTION, "Connect server");
		}
		public void actionPerformed(ActionEvent e) {
			servidor.start();
		}
	}
	
	public JList<?> getList() {
		return list;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIServidorUDP frame = new GUIServidorUDP();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
