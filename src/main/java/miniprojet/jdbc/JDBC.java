package miniprojet.jdbc;

import java.sql.*;
import java.util.ArrayList;

import miniprojet.model.CommandLine;
import miniprojet.model.Product;
import miniprojet.model.Client;
import miniprojet.model.Command;

/**
 * Classe JDBC permettant de gérer la connexion à la base de données ainsi que
 * les opérations CRUD (Create, Read, Update, Delete) sur les entités : - Client
 * - Produit - Commande - Ligne de commande
 *
 * Cette classe utilise le pattern Singleton : une seule instance de connexion
 * est créée pour toute l'application.
 *
 * La connexion est établie automatiquement lors de l'initialisation.
 */
public class JDBC {
	private String host;
	private int port;
	private String db;
	private String user;
	private String password;
	private String url;
	private Connection conn;
	private static JDBC instance;

	/**
	 * Constructeur privé. Initialise les paramètres de connexion et ouvre la
	 * connexion à la base de données.
	 */
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
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retourne l'unique instance de la classe JDBC.
	 *
	 * @return instance unique de JDBC
	 */
	public static JDBC getInstance() {
		if (instance == null) {
			instance = new JDBC();
		}
		return instance;
	}

	// -------------------------
	// Verify methods
	// -------------------------

	/**
	 * Vérifie si un client existe à partir de son email.
	 *
	 * @param email Email du client
	 * @return true si le client existe, false sinon
	 */
	public boolean clientExists(String email) {
		String sql = "SELECT 1 FROM CLIENTS WHERE email = ? LIMIT 1";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Vérifie si un produit existe à partir de son identifiant.
	 *
	 * @param idProduct Identifiant du produit
	 * @return true si le produit existe, false sinon
	 */
	public boolean productExists(int idProduct) {
		String sql = "SELECT 1 FROM PRODUITS WHERE id = ? LIMIT 1";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idProduct);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Vérifie si une commande existe.
	 *
	 * @param idCommand Identifiant de la commande
	 * @return true si elle existe, false sinon
	 */
	private boolean commandExists(int idCommand) {
		String sql = "SELECT 1 FROM COMMANDES WHERE id = ? LIMIT 1";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idCommand);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Vérifie si la quantité demandée est disponible en stock.
	 *
	 * @param idProduct      Identifiant du produit
	 * @param quantityWanted Quantité demandée
	 * @return true si le stock est suffisant, false sinon
	 */
	public boolean verifyQuantity(int idProduct, int quantityWanted) {
		if (quantityWanted <= 0)
			return false;

		String sql = "SELECT quantite FROM PRODUITS WHERE id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idProduct);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return false;
				int stock = rs.getInt("quantite");
				return stock >= quantityWanted;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// -------------------------
	// Insert methods
	// -------------------------

	/**
	 * Insère un client avec uniquement son email.
	 *
	 * @param email Email du client
	 */
	public void insertClient(String email) {
		if (email == null || email.length() > 60)
			return;
		if (clientExists(email))
			return;

		String sql = "INSERT INTO CLIENTS(email) VALUES (?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insère un client complet.
	 *
	 * @param email Email du client
	 * @param nom   Nom du client
	 * @param ville Ville du client
	 */
	public void insertClient(String email, String nom, String ville) {
		if (email == null || nom == null || ville == null)
			return;
		if (email.length() > 60 || nom.length() > 30 || ville.length() > 30)
			return;
		if (clientExists(email))
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

	/**
	 * Insère un nouveau produit.
	 *
	 * @param nom      Nom du produit
	 * @param quantity Quantité en stock
	 * @param price    Prix du produit
	 */
	public void insertProduct(String nom, int quantity, double price) {
		if (nom == null || nom.length() > 30)
			return;
		if (quantity < 0 || quantity > 999_999)
			return;
		if (price < 0 || price > 999_999.99)
			return;

		String sql = "INSERT INTO PRODUITS(nom, quantite, prix) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nom);
			ps.setInt(2, quantity);
			ps.setDouble(3, price);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crée une nouvelle commande pour un client. Si le client n'existe pas, il est
	 * créé automatiquement.
	 * 
	 * La commande est enregistrée à la date courante.
	 *
	 * @param email Email du client
	 */
	public void insertCommand(String email) {
		if (email == null || email.length() > 60)
			return;

		Date today = new Date(System.currentTimeMillis());

		insertCommand(email, today);
	}

	/**
	 * Crée une nouvelle commande pour un client. Si le client n'existe pas, il est
	 * créé automatiquement.
	 * 
	 * @param date  Date d'enregistrement de la commande
	 * @param email Email du client
	 */
	public void insertCommand(String email, Date date) {
		if (email == null || email.length() > 60 || date == null)
			return;

		if (!clientExists(email)) {
			insertClient(email);
		}

		String sql = "INSERT INTO COMMANDES(email_client, date) VALUES (?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setDate(2, date);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute une ligne à une commande. Diminue automatiquement le stock du produit.
	 *
	 * @param command  Identifiant de la commande
	 * @param product  Identifiant du produit
	 * @param quantity Quantité commandée
	 */
	public void insertLine(int command, int product, int quantity) {
		if (!commandExists(command) || !productExists(product))
			return;
		if (!verifyQuantity(product, quantity))
			return;

		boolean okStock = modifyQuantity(product, -quantity);
		if (!okStock)
			return;

		Product p = selectProduct(product);
		if (p == null)
			return;
		double price = p.getPrix();

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

	// -------------------------
	// Modify methods
	// -------------------------

	/**
	 * Modifie la quantité d'un produit.
	 *
	 * @param idProduct  Identifiant du produit
	 * @param ajustement Valeur à ajouter (négatif pour retirer du stock)
	 * @return true si la modification a été effectuée, false sinon
	 */
	public boolean modifyQuantity(int idProduct, int ajustement) {
		if (!productExists(idProduct))
			return false;

		String sql = "UPDATE PRODUITS " + "SET quantite = quantite + ? " + "WHERE id = ? AND (quantite + ?) >= 0";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, ajustement);
			ps.setInt(2, idProduct);
			ps.setInt(3, ajustement);
			int updated = ps.executeUpdate();
			return updated == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// -------------------------
	// Select methods
	// -------------------------

	/**
	 * Récupère tous les clients.
	 *
	 * @return liste des clients
	 */
	public ArrayList<Client> selectClients() {
		String sql = "SELECT * FROM CLIENTS";
		ArrayList<Client> list = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet res = ps.executeQuery()) {

			while (res.next()) {
				list.add(new Client(res.getString("email"), res.getString("nom"), res.getString("ville")));
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère tous les produits.
	 *
	 * @return liste des produits
	 */
	public ArrayList<Product> selectProducts() {
		String sql = "SELECT * FROM PRODUITS";
		ArrayList<Product> list = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet res = ps.executeQuery()) {

			while (res.next()) {
				list.add(new Product(res.getInt("id"), res.getString("nom"), res.getDouble("prix"),
						res.getInt("quantite")));
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère tous les produits.
	 *
	 * @param name Nom du produit.
	 *
	 * @return liste des produits
	 */
	public ArrayList<Product> selectProducts(String name) {
		String sql = "SELECT * FROM PRODUITS where nom = ?";
		ArrayList<Product> list = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);

			try (ResultSet res = ps.executeQuery()) {

				while (res.next()) {
					list.add(new Product(res.getInt("id"), res.getString("nom"), res.getDouble("prix"),
							res.getInt("quantite")));
				}
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère toutes les commandes.
	 *
	 * @return liste des commandes
	 */
	public ArrayList<Command> selectCommands() {
		String sql = "SELECT * FROM COMMANDES";
		ArrayList<Command> list = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet res = ps.executeQuery()) {

			while (res.next()) {
				list.add(new Command(res.getInt("id"), res.getString("email_client"), res.getDate("date")));
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère toutes les lignes d'une commande.
	 *
	 * @param command Identifiant de la commande
	 * @return liste des lignes de commande
	 */
	public ArrayList<CommandLine> selectLines(int command) {
		String sql = "SELECT * FROM LIGNE_COMMANDE WHERE commande_id = ?";
		ArrayList<CommandLine> list = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, command);

			try (ResultSet res = ps.executeQuery()) {
				while (res.next()) {
					list.add(new CommandLine(res.getInt("id"), res.getInt("commande_id"), res.getInt("id_produit"),
							res.getInt("quantite"), res.getDouble("prix_vendu")));
				}
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère un client à partir de son email.
	 *
	 * @param email Email du client
	 * @return client correspondant ou null si inexistant
	 */
	public Client selectClient(String email) {
		String sql = "SELECT * FROM CLIENTS WHERE email = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);

			try (ResultSet res = ps.executeQuery()) {
				if (res.next()) {
					return new Client(email, res.getString("nom"), res.getString("ville"));
				}
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère un produit à partir de son identifiant.
	 *
	 * @param id Identifiant du produit
	 * @return produit correspondant ou null si inexistant
	 */
	public Product selectProduct(int id) {
		String sql = "SELECT * FROM PRODUITS WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);

			try (ResultSet res = ps.executeQuery()) {
				if (res.next()) {
					return new Product(id, res.getString("nom"), res.getDouble("prix"), res.getInt("quantite"));
				}
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère une commande à partir de son identifiant.
	 *
	 * @param id Identifiant de la commande
	 * @return commande correspondante ou null si inexistante
	 */
	public Command selectCommand(int id) {
		String sql = "SELECT * FROM COMMANDES WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);

			try (ResultSet res = ps.executeQuery()) {
				if (res.next()) {
					return new Command(id, res.getString("email_client"), res.getDate("date"));
				}
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère une ligne de commande à partir de son identifiant.
	 *
	 * @param id Identifiant de la ligne
	 * @return ligne correspondante ou null si inexistante
	 */
	public CommandLine selectLine(int id) {
		String sql = "SELECT * FROM LIGNE_COMMANDE WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);

			try (ResultSet res = ps.executeQuery()) {
				if (res.next()) {
					return new CommandLine(id, res.getInt("commande_id"), res.getInt("id_produit"),
							res.getInt("quantite"), res.getDouble("prix_vendu"));
				}
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}