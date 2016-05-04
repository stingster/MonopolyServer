package com.ea.ja.server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ea.ja.server.domain.Player;

public class DAOImpl implements DAO {

	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet resultSet;
	private String sql;

	@Override
	public Connection createConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Monopoly", "root", "");
			}
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Connection wasn't created!");
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public synchronized boolean createUser(Player player) {

		connection = createConnection();

		sql = "INSERT INTO player values(?, ?, ?, ?, ?);";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, player.getUsername());
			pStatement.setString(2, player.getPassword());
			pStatement.setInt(3, 0);
			pStatement.setString(4, "");
			pStatement.setInt(5, 0);
			pStatement.execute();
			return true;
		} catch (SQLException e) {
			System.out.println("User already exists!");
			e.printStackTrace();
			return false;
		} finally {
			closeConnection();
		}
	}

	@Override
	public synchronized Player logIn(String username, String password) {

		connection = createConnection();

		sql = "SELECT * FROM player WHERE username = ? AND password = ?;";

		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, username);
			pStatement.setString(2, password);
			resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				Player player = new Player(username, password);
				player.setPosition(resultSet.getInt("position"));
				player.setToken(resultSet.getInt("token"));
				player.setMoney(resultSet.getInt("money"));
				return player;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return null;
	}

	@Override
	public int checkNumberOfLoggedPlayers() {
		connection = createConnection();

		sql = "SELECT COUNT(isLogged) FROM player WHERE isLogged = ?";

		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, 1);
			resultSet = pStatement.executeQuery();

			if (resultSet != null) {
				return resultSet.getInt(0);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return -1;
	}

	@Override
	public synchronized Player move(String username, int initialPosition, int dice) {
		connection = createConnection();

		if (verifyPosition(username, initialPosition)) {

			initialPosition = (initialPosition + dice) % 40;
			sql = "UPDATE player SET position = ? WHERE username = ?";
			try {
				pStatement.setInt(1, initialPosition);
				pStatement.setString(2, username);
				pStatement.executeUpdate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			sql = "SELECT * FROM player WHERE username = ?;";
			try {
				pStatement.setString(1, username);
				resultSet = pStatement.executeQuery();

				Player player = new Player(resultSet.getString("username"));
				player.setPosition(resultSet.getInt("position"));
				player.setToken(resultSet.getInt("token"));
				player.setMoney(resultSet.getInt("money"));
				return player;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}
		return null;
	}

	private boolean verifyPosition(String username, int initialPosition) {

		sql = "SELECT position FROM player WHERE username = ?;";

		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, username);
			resultSet = pStatement.executeQuery();
			if (resultSet != null && resultSet.getInt(0) == initialPosition) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
			if (pStatement != null) {
				pStatement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			System.out.println("Connection wasn't closed!");
			e.printStackTrace();
		}
	}
}
