package miniprojet.jdbc;

import java.sql.*;

public class JDBC {
	private String host;
	private int port;
	private String db;
	private String user;
	private String password;
	private String url;
	private static JDBC instance;
	
	private JDBC() {
		host = "localhost";
	    port = 3306;
	    db = "4.03";
	    user = "root";
	    password = "";
	    url = "jdbc:mysql://" + host + ":" + port + "/" + db
	            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	}
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDb() {
		return db;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public static JDBC getInstance() {
		if (instance == null) {
			instance = new JDBC();
		}
		return instance;
	}
}
