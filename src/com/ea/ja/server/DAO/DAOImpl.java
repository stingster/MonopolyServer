package com.ea.ja.server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;
import com.ea.ja.server.domain.Player;

public class DAOImpl implements DAO {

	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet resultSet;
	private String sql;
	private static final BasicDataSource dataSource;

	static {
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://10.45.52.104:3306/Monopoly");
		dataSource.setUsername("root");
		dataSource.setPassword("");
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/*
	 * @Override public Connection createConnection() { try { if (connection ==
	 * null || connection.isClosed()) { Class.forName("com.mysql.jdbc.Driver");
	 * connection =
	 * DriverManager.getConnection("jdbc:mysql://localhost:3306/Monopoly",
	 * "root", ""); } } catch (SQLException | ClassNotFoundException e) {
	 * System.out.println("Connection wasn't created!"); e.printStackTrace(); }
	 * return connection; }
	 */
	@Override
	public synchronized boolean createUser(Player player) {
		try {
			connection = getConnection();
			sql = "INSERT INTO player values(?, ?, ?, ?, ?);";
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
			closeResources();
		}
	}

	@Override
	public synchronized Player logIn(String username, String password) {

		try {

			connection = getConnection();
			sql = "SELECT * FROM player WHERE username = ? AND password = ?;";
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, username);
			pStatement.setString(2, password);
			resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				Player player = new Player(username, password);
				player.setPosition(resultSet.getInt("position"));
				player.setToken(resultSet.getInt("token"));
				player.setMoney(resultSet.getInt("money"));

				logPlayer(username);

				return player;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return null;
	}

	@Override
	public void logPlayer(String username) {
		sql = "UPDATE player SET isLogged = 1 WHERE username = ?";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, username);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Couldn't modify state of the player");
			e.printStackTrace();
		}
	}

	@Override
	public int checkNumberOfLoggedPlayers() {
		try {
			connection = getConnection();
			sql = "SELECT COUNT(isLogged) FROM player WHERE isLogged = ?";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, 1);
			resultSet = pStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return -1;
	}

	@Override
	public synchronized Player move(String username, int initialPosition, int dice) {
		try {

			connection = getConnection();
			if (verifyPosition(username, initialPosition)) {
				initialPosition = (initialPosition + dice) % 40;
				sql = "UPDATE player SET position = ? WHERE username = ?";
				pStatement = connection.prepareStatement(sql);
				pStatement.setInt(1, initialPosition);
				pStatement.setString(2, username);
				pStatement.executeUpdate();

				sql = "SELECT * FROM player WHERE username = ?;";
				pStatement = connection.prepareStatement(sql);
				pStatement.setString(1, username);
				resultSet = pStatement.executeQuery();

				if (resultSet.next()) {
					Player player = new Player(resultSet.getString("username"));
					player.setPosition(resultSet.getInt("position"));
					player.setToken(resultSet.getInt("token"));
					player.setMoney(resultSet.getInt("money"));
					return player;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return null;
	}

	private boolean verifyPosition(String username, int initialPosition) {
		sql = "SELECT position FROM player WHERE username = ?;";
		try {
			pStatement = connection.prepareStatement(sql);
			pStatement.setString(1, username);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet.getInt("position") == initialPosition) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void closeResources() {
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

	@Override
	public boolean resetBoard() {
		try {
			connection = getConnection();
			sql = "UPDATE player SET position = 0, token = 0, money = 0, isLogged = 0;";
			pStatement = connection.prepareStatement(sql);
			pStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}