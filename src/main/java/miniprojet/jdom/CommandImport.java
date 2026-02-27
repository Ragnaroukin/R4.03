package miniprojet.jdom;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class CommandImport extends XMLHandler {

	public CommandImport() {
		super();
	}

	/**
	 * Importer la commande depuis un fichier XML
	 * @param path le chemin vers le fichier d'import
	 * @return la résussite de l'import
	 */
	public boolean importCommand(String path) {
		File file = new File(path);

		SAXBuilder builder = new SAXBuilder();

		try {
			document = builder.build(file);
			root = document.getRootElement();
			addClient();
			addCommand();
			addCommandLines();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		try {
			jdbc.insertCommand(email, new Date(sdf.parse(date).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insérer les lignes de commandes associées aux produits commandés
	 */
	private void addCommandLines() {
		List<Element> products = root.getChild("produits").getChildren("produit");

		int commandId = jdbc.selectCommands().getLast().getId(); // la dernière commande ajoutée dans la base

		int productId, quantity;
		double price;

		for (Element product : products) {

			productId = jdbc.selectProduct(product.getChildText("nom")).getId();
			quantity = Integer.parseInt(product.getChildText("quantité"));
			price = Double.parseDouble(product.getChildText("prix"));

			jdbc.insertLine(commandId, price, productId, quantity);
		}
	}
}
