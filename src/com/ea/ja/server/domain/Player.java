package com.ea.ja.server.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import com.ea.ja.server.socket.*;

import com.ea.ja.server.socket.InvalidRequestedCode;
import com.ea.ja.server.socket.*;
import com.ea.ja.server.socket.Server;
import com.sun.corba.se.spi.activation.*;

public class Player implements Runnable{

	private String username;
	private String password;
	private int position;
	private int token;
	private int money;
	private List<Commodity> commodities = new ArrayList<>();
	private boolean isLogged;
    private Socket socket;
    private Thread thread;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    
	public Player(String username) {
		this.username = username;
	}

	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
    public Player(String username, Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.username = username;
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        thread = new Thread(this);
        thread.start();
    }
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public List<Commodity> getCommodities() {
		return commodities;
	}

	public void setCommodities(List<Commodity> commodities) {
		this.commodities = commodities;
	}

	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commodities == null) ? 0 : commodities.hashCode());
		result = prime * result + (isLogged ? 1231 : 1237);
		result = prime * result + money;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + position;
		result = prime * result + token;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (commodities == null) {
			if (other.commodities != null)
				return false;
		} else if (!commodities.equals(other.commodities))
			return false;
		if (isLogged != other.isLogged)
			return false;
		if (money != other.money)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (position != other.position)
			return false;
		if (token != other.token)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public void sendMessage(MessageCodes code, Object serializedObject) throws InvalidRequestedCode, IOException {
        objectOutputStream.writeObject(new Message(code, serializedObject));
    }

    @Override
    public void run() {
        // asteptare mesaje de la client
        Message resp;
        try {
            while ((resp = (Message) objectInputStream.readObject()) != null) {
				if(resp.getMessageCodes() == MessageCodes.USER_POSITION){
					setPosition((Integer)resp.getSerializableObject());
					Server.updateUserPostion(getUsername(),getPosition());
				}
				if(resp.getMessageCodes() == MessageCodes.USER_END_TURN)
					Server.nextPlayerTurn();
			}
        }catch (SocketException e){
            System.out.println(username + " disconnected.");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
	
}
