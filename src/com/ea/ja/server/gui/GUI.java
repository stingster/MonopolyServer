package com.ea.ja.server.gui;
import com.ea.ja.server.socket.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame{
	
	JComboBox requiredNoOfClients;
	JTextField port;
	String s;
	int numberOfClients;
	
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
		JButton start = new JButton(" Start server ");
		JButton stop = new JButton(" Stop server");
		start.addActionListener(new Listner());
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Server.stopServer();
				
			}
		});
		
		
		
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
	class Listner implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			s=port.getText();
			numberOfClients=(int) requiredNoOfClients.getSelectedItem();
			Server.setRequiredClients(numberOfClients);
			try {
				Server.setListeningPort( Integer.parseInt(s));
			} catch (NumberFormatException e1) {
				
				e1.printStackTrace();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}		
			Server.startServer();
		}		
	}

}
