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
		setSize(400,200);
		
		port = new JTextField(10);
		Integer[] items = {2,3,4,5,6,7,8};
		requiredNoOfClients = new JComboBox<>(items);
		JLabel portlb = new JLabel(" Port :");
		JLabel clients = new JLabel(" Set minimum number of players  :");
		JButton start = new JButton(" Start server ");
		JButton stop = new JButton(" Stop");
		start.addActionListener(new Listner());
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		
		
		// firt col
		grid.weightx = 0.5;
		grid.weighty = 0.5;
		grid.gridx=0;
		grid.gridy =0;
		add(portlb, grid);
		
		grid.gridx=0;
		grid.gridy =1;
		add(clients,grid);
		
		// second col
		
		grid.gridx=1;
		grid.gridy =0;
		add(port,grid);
		
		grid.gridx=1;
		grid.gridy =1;
		add(requiredNoOfClients, grid);
		
		
		grid.weighty = 2;
		grid.gridx =1;
		grid.gridy =3;
		add(start,grid);
		
		grid.weighty = 2;
		grid.gridx =0;
		grid.gridy =3;
		add(stop,grid);
//		
		
		this.setVisible(true);
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
