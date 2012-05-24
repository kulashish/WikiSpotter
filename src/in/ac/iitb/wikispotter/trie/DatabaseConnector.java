package in.ac.iitb.wikispotter.trie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp.ConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.DriverManagerConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.PoolableConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.PoolingDriver;
import org.apache.tomcat.dbcp.pool.impl.GenericObjectPool;

public class DatabaseConnector {
	public static String DB_URI = "jdbc:mysql://10.129.1.91:3306/wikispot";
	public static String DB_USER = "pararth";
	public static String DB_PASS = "pararth123";

	// Singleton instance
	protected static DatabaseConnector _instance;

	protected String _uri;
	protected String _username;
	protected String _password;

	/**
	 * Singleton, so no public constructor
	 */
	protected DatabaseConnector(String uri, String username, String password) {
		_uri = uri;
		_username = username;
		_password = password;

		GenericObjectPool connectionPool = new GenericObjectPool(null);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				_uri, _username, _password);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		PoolingDriver driver = new PoolingDriver();
		driver.registerPool("test", connectionPool);
	}

	/**
	 * Returns the singleton instance
	 */
	public static DatabaseConnector getInstance() {
		if (_instance == null) {
			_instance = new DatabaseConnector(DB_URI, DB_USER, DB_PASS);
		}
		return _instance;
	}

	/**
	 * Returns a connection to the database
	 */
	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:apache:commons:dbcp:test");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return con;
	}
}