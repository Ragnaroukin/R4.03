package miniprojet.jdbc;

import java.sql.*;
import java.util.ArrayList;

import miniprojet.model.CommandLine;
import miniprojet.model.Product;
import miniprojet.model.Client;
import miniprojet.model.Command;

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
			Connection conn = DriverManager.getConnection(url, user,password);
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

		double price = selectProduct(product).getPrix();

		String sql = "INSERT INTO LIGNE_COMMANDE(commande_id, id_produit, quantite, prix_vendu) VALUES (?,?,?,?)";

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
	public ArrayList<Client> selectClients() {
		String sql = "SELECT * FROM CLIENTS";
		ResultSet res;
		ArrayList<Client> list = new ArrayList<Client>();

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			
			while (res.next()) {
				String email = res.getString("email");
				String nom = res.getString("nom");
			    String ville = res.getString("ville");
			    
			    list.add(new Client(email, nom, ville));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Product> selectProducts() {
		String sql = "SELECT * FROM PRODUITS";
		ResultSet res;
		ArrayList<Product> list = new ArrayList<Product>();

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			
			while (res.next()) {
				int id = res.getInt("id");
				String nom = res.getString("nom");
			    double prix = res.getDouble("prix");
			    int quantite = res.getInt("quantite");
			    
			    list.add(new Product(id, nom, prix, quantite));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Command> selectCommands() {
		String sql = "SELECT * FROM COMMANDES";
		ResultSet res;
		ArrayList<Command> list = new ArrayList<Command>();

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			
			while (res.next()) {
				int id = res.getInt("id");
				String emailClient = res.getString("email_client");
			    Date date = res.getDate("date");
			    
			    list.add(new Command(id, emailClient, date));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<CommandLine> selectLines(int command) {
		String sql = "SELECT * FROM LIGNE_COMMANDE WHERE commande_id = ?";
		ResultSet res;
		ArrayList<CommandLine> list = new ArrayList<CommandLine>();

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, command);
			res = ps.executeQuery();
			
			while (res.next()) {
				int id = res.getInt("id");
				int commandeId = res.getInt("commande_id");
				int produitId = res.getInt("id_produit");
				int quantite = res.getInt("quantite");
				double prixVendu = res.getDouble("prix");

				list.add(new CommandLine(id, commandeId, produitId, quantite, prixVendu));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Client selectClient(String email) {
		String sql = "SELECT * FROM CLIENTS WHERE email = ?";
		ResultSet res;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			res = ps.executeQuery();

			res.next();

			String nom = res.getString("nom");
		    String ville = res.getString("ville");
		    
		    return new Client(email, nom, ville);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Product selectProduct(int id) {
		String sql = "SELECT * FROM PRODUITS WHERE id = ?";
		ResultSet res;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			res = ps.executeQuery();

			res.next();

			String nom = res.getString("nom");
		    double prix = res.getDouble("prix");
		    int quantite = res.getInt("quantite");
		    
		    return new Product(id, nom, prix, quantite);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Command selectCommand(int id) {
		String sql = "SELECT * FROM COMMANDES WHERE id = ?";
		ResultSet res;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			res = ps.executeQuery();

			res.next();

			String emailClient = res.getString("email_client");
		    Date date = res.getDate("date");
		    
		    return new Command(id, emailClient, date);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public CommandLine selectLine(int id) {
		String sql = "SELECT * FROM LIGNE_COMMANDE WHERE id = ?";
		ResultSet res;

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			res = ps.executeQuery();

			res.next();

			int commandeId = res.getInt("commande_id");
			int produitId = res.getInt("id_produit");
			int quantite = res.getInt("quantite");
			double prixVendu = res.getDouble("prix");

			return new CommandLine(id, commandeId, produitId, quantite, prixVendu);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
