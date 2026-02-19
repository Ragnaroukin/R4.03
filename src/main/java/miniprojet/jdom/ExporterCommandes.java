package miniprojet.jdom;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ExporterCommandes extends XMLHandler {
	
	public ExporterCommandes() {
		super("commandes");
	}
	
	/**
	 * Ajouter la commande a la liste des commandes
	 * @param id l'identifiant de la commande
	 */
	public void ajouterCommande(int id) {
		Element commande = new Element("commande");
		commande.setAttribute("id", "C"+id);
		commande.setAttribute("nb-produits", "3");
		
		racine.addContent(commande);
		
		document.getContent();
	}
	
	/**
	 * Exporter la liste des commandes au format XML
	 */
	public void exporter() {
	    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());

	    try (OutputStream out = new FileOutputStream("export_commandes.xml")) {
	        sortie.output(document, out);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
