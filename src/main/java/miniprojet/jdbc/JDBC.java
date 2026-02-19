package miniprojet.jdbc;

import java.sql.*;

public class JDBC {
	private String host;
	private int port;
	private String db;
	private String user;
	private String password;
	private String url;
	private Connection conn;
	private static JDBC instance;

	private JDBC() {
		host = "localhost";
		port = 3306;
		db = "4.03";
		user = "root";
		password = "";
		url = "jdbc:mysql://" + host + ":" + port + "/" + db
				+ "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
		conn = connexion();
	}

	private Connection connexion() {
		try {
			Connection conn = DriverManager
					.getConnection(JDBC.getInstance().getUrl(), 
							JDBC.getInstance().getUser(),
							JDBC.getInstance().getPassword());
			System.out.println("Connexion réussie !");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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

	// Verify methods
	public boolean clientExists() {
		return false;

	}

	public boolean productExists() {
		return false;
	}
	
	private boolean commandExists() {
		return false;
	}

	public boolean verifyQuantity(int idProduct, int quantity) {
		return false;
	}

	// Insert methods
	public void insertClient(String email) {
		if (email.length() > 60)
			return;

		if (clientExists())
			return;

		String sql = "INSERT INTO CLIENTS(email) VALUES (?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, email);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertClient(String email, String nom, String ville) {
		if (email.length() > 60 || nom.length() > 30 || ville.length() > 30)
			return;

		if (clientExists())
			return;

		String sql = "INSERT INTO CLIENTS(email, nom, ville) VALUES (?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, email);
			ps.setString(2, nom);
			ps.setString(3, ville);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertProduit(String nom, int quantity, double price) {
		if (nom.length() > 60 || quantity > 999_999 || price > 999_999.99)
			return;

		if (productExists())
			return;

		String sql = "INSERT INTO CLIENTS(nom, quantite, prix) VALUES (?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, nom);
			ps.setInt(2, quantity);
			ps.setDouble(3, price);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertCommand(String email) {
		if (email.length() > 60)
			return;
		
		if (!clientExists())
			insertClient(email);
			

		String sql = "INSERT INTO COMMANDES(email_client) VALUES (?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, email);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertLine(int command, int product, int quantity) {
		if (!commandExists() || !productExists())
			return;
		
		if (!verifyQuantity(product, quantity))
			return;
		
		modifyQuantity(product, -quantity);
		
		double price = 0;
				
		try {
			price = selectProduct(product).getDouble("prix");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String sql = "INSERT INTO LIGNE_COMMAND(commande_id, id_produit, quantite, prix_vendu) VALUES (?,?,?,?)";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, command);
			ps.setInt(2, product);
			ps.setInt(3, quantity);
			ps.setDouble(4, price);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Modify methods
	public void modifyQuantity(int idProduct, int ajustement) {

	}
	
	// Select methods
	public ResultSet selectClients() {
		return null;
	}
	
	public ResultSet selectProducts() {
		return null;
	}

	public ResultSet selectCommands() {
		return null;
	}

	public ResultSet selectLines() {
		return null;
	}

	public ResultSet selectClient(int id) {
		return null;
	}
	
	public ResultSet selectProduct(int id) {
		return null;
	}

	public ResultSet selectCommand(int id) {
		return null;
	}

	public ResultSet selectLine(int id) {
		return null;
	}
}
