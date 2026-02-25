package miniprojet.jdom;

import java.io.File;
import java.sql.Date;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import miniprojet.model.Client;

public class CommandImport extends XMLHandler {

	public CommandImport() {
		super();
	}

	/**
	 * Importer la commande depuis un fichier XML
	 */
	public void importCommand(String path) {
		File file = new File(path);

		SAXBuilder builder = new SAXBuilder();

		try {
			document = builder.build(file);
			root = document.getRootElement();
			addClient();
			addCommand();
			addCommandLines();
			updateProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insérer le client de la commande s'il n'existe pas dans la base
	 */
	private void addClient() {
		Element clientElement = root.getChild("client");

		String email = clientElement.getChildText("email");
		String name = clientElement.getChildText("nom-client");
		String city = clientElement.getChildText("ville");

		jdbc.insertClient(email, name, city);
	}

	/**
	 * Insérer la nouvelle commande
	 */
	private void addCommand() {
		String email = root.getChild("client").getChildText("email");
		String date = root.getChildText("date");

		jdbc.insertCommand(email, Date.valueOf(date));
	}

	/**
	 * Insérer les lignes de commandes associées aux produits commandés
	 */
	private void addCommandLines() {
		List<Element> products = root.getChildren("produit");

		int commandId = jdbc.selectCommands().getLast().getId(); // la dernière commande ajoutée dans la base

		int productId, quantity;

		for (Element product : products) {

			productId = jdbc.selectProduct(product.getChildText("nom"));
			quantity = Integer.parseInt(product.getChildText("quantité"));

			jdbc.insertLine(commandId, productId, quantity);
		}
	}

	/**
	 * Mettre à jour les quantités des produits commandés
	 */
	private void updateProducts() {
		List<Element> products = root.getChildren("produit");

		int commandId = jdbc.selectCommands().getLast().getId(); // la dernière commande ajoutée dans la base

		int productId, quantity;

		for (Element product : products) {

			productId = jdbc.selectProduct(product.getChildText("nom"));
			quantity = Integer.parseInt(product.getChildText("quantité"));

			jdbc.modifyQuantity(productId, quantity);
		}
	}
}
