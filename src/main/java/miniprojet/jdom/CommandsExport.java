package miniprojet.jdom;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import miniprojet.jdbc.JDBC;
import miniprojet.model.*;

public class CommandsExport extends XMLHandler{
	
	
	public CommandsExport() {
		super("commandes");
	}
	
	/**
	 * Exporter la commande
	 * @param id l'identifiant de la commande
	 */
	public void exporterCommande(int id) {
		
		Command command = jdbc.selectCommand(id);
		
		Element commandElement = new Element("commande");
		
		exportClient(command, commandElement);
		exportProducts(id, commandElement);
		
		commandElement.setAttribute("id", "C"+id);
		
		root.addContent(commandElement);
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
	 * Exporte les d'une commande
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
			priceElement = new Element("prix").addContent(""+product.getPrix());
			quantityElement = new Element("quantité").addContent(""+product.getQuantite());
			productElement.addContent(nameElement).addContent(priceElement).addContent(quantityElement);
			parent.addContent(productElement);
		}
	}

	/**
	 * Exporter la liste des commandes au format XML
	 */
	public void export() {
	    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());

	    try (OutputStream out = new FileOutputStream("export_commandes.xml")) {
	        output.output(document, out);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
