package miniprojet.jdom;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import miniprojet.model.*;

public class CommandsExport extends XMLHandler{
	
	
	public CommandsExport() {
		super("commandes");
	}
	
	/**
	 * Exporter une commande
	 * @param id l'identifiant de la commande
	 */
	private void exportCommand(int id) {
		
		Command command = jdbc.selectCommand(id);
		
		Element commandElement = new Element("commande");
		Element dateElement = new Element("date");
		Element totalElement = new Element("total");
		
		commandElement.setAttribute("id", "C"+id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		dateElement.addContent(sdf.format(command.getDate()));
		totalElement.addContent(""+getTotalCommand(id));
		
		exportClient(command, commandElement);
		exportProducts(id, commandElement);

		root.addContent(commandElement);
	}
	
	/**
	 * Obtenir le total d'une commande
	 * @param id l'identifiant de la commande
	 * @return le total de la commande
	 */
	public int getTotalCommand(int id) {
		ArrayList<CommandLine> lines = jdbc.selectLines(id);
		int total = 0;
		for(CommandLine line : lines)
			total += line.getPrixVendu() * line.getQuantite();
		return total;
	}
	
	/**
	 * Exporte le client d'une commande
	 * @param command la commande exportée
	 * @param parent l'élément qui représente la commande
	 */
	private void exportClient(Command command, Element parent) {
		Client client = jdbc.selectClient(command.getEmailClient());
		Element nameElement = new Element("nom-client").addContent(client.getNom());
		Element emailElement = new Element("email").addContent(client.getEmail());
		Element cityElement = new Element("ville").addContent(client.getVille());
		
		parent.addContent(nameElement).addContent(emailElement).addContent(cityElement);
	}

	/**
	 * Exporte les produits d'une commande
	 * @param id l'id de la commande
	 * @param parent l'élément qui représente la commande
	 */
	private void exportProducts(int id, Element parent) {
		ArrayList<CommandLine> lines = jdbc.selectLines(id);
		Product product;
		Element productElement, nameElement, priceElement, quantityElement;
		
		parent.setAttribute("nb-produits",""+lines.size());
		parent.addContent(new Element("produits"));
		
		for(CommandLine line : lines) {
			product = jdbc.selectProduct(line.getProduitId());
			productElement = new Element("produit");
			nameElement = new Element("nom").addContent(product.getNom());
			priceElement = new Element("prix").addContent(""+line.getPrixVendu());
			quantityElement = new Element("quantité").addContent(""+line.getQuantite());
			productElement.addContent(nameElement).addContent(priceElement).addContent(quantityElement);
			parent.addContent(productElement);
		}
	}

	/**
	 * Exporter la liste des commandes au format XML
	 * @return la réussite de l'export
	 */
	public boolean exportCommands() {
		
		ArrayList<Command> commands = jdbc.selectCommands();
		
		commands.forEach(command -> exportCommand(command.getId()));
		
	    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());

	    try (OutputStream out = new FileOutputStream("export_commandes.xml")) {
	        output.output(document, out);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
