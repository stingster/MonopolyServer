package com.ea.ja.server.gui;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ea.ja.server.socket.Server;

public class GUI extends JFrame{
	
	JComboBox requiredNoOfClients;
	JTextArea console; 
	JTextField port;
	PrintStream out;
	
	String s;
	int numberOfClients;
	KeyAdapter adapter;
	JButton start;
	
	public GUI (){
		/**
		 *  Title
		 */
		super("Server");		
		
		setLayout(null);
		setSize(400,500);
		
		
		/**
		 * 		Creating elements
		 */
		JLabel portlb = new JLabel(" Port :   ");
		port = new JTextField(6);
		port.setText("8080");
		
		JLabel clients = new JLabel(" Set minimum number of players  :   ");
		Integer[] items = {2,3,4,5,6,7,8};
		requiredNoOfClients = new JComboBox<>(items);

		
		start = new JButton(" Start server ");
		JButton stop = new JButton(" Stop server");
		
		console = new JTextArea();
		out = new PrintStream( new TextAreaOutputStream( console ) );
		System.setOut(out);
		
		JScrollPane scroll = new JScrollPane(console);
		
		
		/**
		 *		 Button Start Server 
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
            start.setEnabled(false);
        });
		
		/**
		 *  	Button Stop Server
		 */
		stop.addActionListener(e -> Server.stopServer());
		
		
		/**
		 * 		Adding elements 
		 */
		add(portlb);
		add(port);
		add(clients);
		add(requiredNoOfClients);
		add(stop);
		add(start);
		//add(console);
		add(scroll);
		
		
		portlb.setBounds(180, -10, 100, 100);
		port.setBounds(225, 30, 100, 20);
		
		clients.setBounds(20, 20, 230, 100);
		requiredNoOfClients.setBounds(225,60, 100, 20);
		
		stop.setBounds(75,90 ,115,30 );
		start.setBounds(240,90 ,115,30 );
		
		//console.setBounds(15,130,360,360 );
		scroll.setBounds(15,130,360,325 );
		
		
		/**
		 * 
		 */
		createAdapter();
		port.addKeyListener(adapter);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 *  
	 */
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

