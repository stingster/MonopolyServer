package com.ja.ea.server.DAO;

import java.sql.Connection;

import com.ea.ja.server.domain.Player;

public interface DAO {

	// CREATE
	Connection createConnection();
	boolean createUser(Player player);
	
	
	// READ
	Player logIn(String username, String password);
	int checkNumberOfLoggedPlayers();
	
	
	
	// UPDATE
	Player move(String username, int initialPosition, int dice);
	
	
	
	
	// DELETE
	void closeConnection();
	
}
