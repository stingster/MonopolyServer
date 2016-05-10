package com.ea.ja.server.DAO;

import java.sql.Connection;

import com.ea.ja.server.domain.Player;

public interface DAO {

	// CREATE
	boolean createUser(Player player);
	
	
	// READ
	Player logIn(String username, String password);
	int checkNumberOfLoggedPlayers();
	
	
	
	// UPDATE
	Player move(String username, int initialPosition, int dice);
	boolean resetBoard();
	void logPlayer(String username);
	
	
	// DELETE

	
}
