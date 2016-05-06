package com.ea.ja.server.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ea.ja.server.socket.Server;

public class GUI extends JFrame{
	
	JComboBox requiredNoOfClients;
	JTextField port;
	String s;
	int numberOfClients;
	KeyAdapter adapter;
	JButton start;
	
	public GUI (){
		super("Server");		
		
		GridBagConstraints grid = new GridBagConstraints();
		
		setLayout(new GridBagLayout());
		setSize(380,150);
		
		port = new JTextField(6);
		port.setText("8080");
		
	
		Integer[] items = {2,3,4,5,6,7,8};
		requiredNoOfClients = new JComboBox<>(items);
		JLabel portlb = new JLabel(" Port :   ");
		JLabel clients = new JLabel(" Set minimum number of players  :   ");
		 start = new JButton(" Start server ");
		JButton stop = new JButton(" Stop server");
		
		/**
		 * Start Server 
		 */
		start.addActionListener(e -> {
            s=port.getText();
            numberOfClients=(int) requiredNoOfClients.getSelectedItem();
			try {
				Server.setRequiredClients(numberOfClients);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
                Server.setListeningPort( Integer.parseInt(s));
            } catch (NumberFormatException e1) {

                e1.printStackTrace();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            Server.startServer();
        });
		
		/**
		 * Stop Server
		 */
		stop.addActionListener(e -> Server.stopServer());
		
		
		
		
		
		// firt col
		grid.anchor = GridBagConstraints.LINE_END;
		grid.weightx = 0;
		grid.weighty = 0.05;
		grid.gridx=0;
		grid.gridy =0;
		add(portlb, grid);
		
		grid.gridx=0;
		grid.gridy =1;
		add(clients,grid);
		
		
		
		// second col
		grid.anchor = GridBagConstraints.LINE_START;
		grid.weightx = 0.5;
		grid.gridx=1;
		grid.gridy =0;
		add(port,grid);
		
		grid.gridx=1;
		grid.gridy =1;
		add(requiredNoOfClients, grid);
		
		//// buttons
		grid.weighty =0.2;
		grid.anchor = GridBagConstraints.CENTER;
		grid.gridx =0;
		grid.gridy =3;
		add(stop,grid);
		
		
		grid.anchor = GridBagConstraints.LINE_START;
		
		grid.gridx =1;
		grid.gridy =3;
		add(start,grid);
		
		
//		
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	public void createAdapter(){
			adapter = new KeyAdapter() {
						public void keyReleased(KeyEvent e) {
							if(e.getKeyCode() == KeyEvent.VK_ENTER) {
								start.doClick();
							}
						}
					}; 

	}

}
