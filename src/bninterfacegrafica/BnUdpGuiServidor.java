/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bninterfacegrafica;

import bnserver.BnUdpServer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mvoco_000
 */
public class BnUdpGuiServidor extends javax.swing.JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String port;
        
        private DefaultTableModel modeloTabela;
    
    private BnUdpServer server;
    
    /**
     * Creates new form BnUdpGuiServidor
     */
    public BnUdpGuiServidor() {
        initComponents();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fPort = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        tPort1 = new javax.swing.JTextField();
        bConfirmarPort = new javax.swing.JButton();
        lbPort = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaClientes = new javax.swing.JTable();
        tPort = new javax.swing.JTextField();
        bConfirmar = new javax.swing.JButton();

        fPort.setTitle("Servidor");

        jLabel1.setText("Port:");

        tPort1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPort1ActionPerformed(evt);
            }
        });

        bConfirmarPort.setText("Confirmar");
        bConfirmarPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bConfirmarPortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fPortLayout = new javax.swing.GroupLayout(fPort.getContentPane());
        fPort.getContentPane().setLayout(fPortLayout);
        fPortLayout.setHorizontalGroup(
            fPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fPortLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bConfirmarPort)
                    .addComponent(tPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        fPortLayout.setVerticalGroup(
            fPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fPortLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(fPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tPort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(bConfirmarPort)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbPort.setText("Port do Servidor:");

        
        jScrollPane1.setViewportView(tabelaClientes);

        tPort.setName("tPort"); // NOI18N

        bConfirmar.setText("Confirmar");
        bConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tPort, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bConfirmar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPort)
                    .addComponent(tPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bConfirmar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tPort1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tPort1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tPort1ActionPerformed

    private void bConfirmarPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bConfirmarPortActionPerformed

    }//GEN-LAST:event_bConfirmarPortActionPerformed

    private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bConfirmarActionPerformed
        
		bConfirmar.setEnabled(false);
		setPort(tPort.getText());
		System.out.println(port);
	    server = new BnUdpServer();
            server.setPort(Integer.parseInt(port));
	    server.init();
            server.initTableModel();
            modeloTabela = server.getTableModel();
            tabelaClientes.setModel(modeloTabela);
	                     
    }//GEN-LAST:event_bConfirmarActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bConfirmar;
    private javax.swing.JButton bConfirmarPort;
    private javax.swing.JFrame fPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaClientes;
    private javax.swing.JLabel lbPort;
    private javax.swing.JTextField tPort;
    private javax.swing.JTextField tPort1;
    // End of variables declaration//GEN-END:variables

    
    public javax.swing.JTable gettabelaClientes() {
		return tabelaClientes;
	}

	public void settUsuarios(javax.swing.JTable tabelaClientes) {
		this.tabelaClientes = tabelaClientes;
	}
    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
}
