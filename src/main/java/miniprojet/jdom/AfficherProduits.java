package miniprojet.jdom;

import java.sql.ResultSet;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class AfficherProduits extends XMLHandler {
	
	public AfficherProduits() {
		super("produits");
	}
	
	/**
	 * Ajouter le produit à la liste des produits
	 * @param nom le nom du produit
	 * @param prix le prix du produit
	 * @param quantite la quantité du produit
	 */
	public void ajouterProduit(int id) {
		ResultSet res = jdbc.selectProduit(id);
		Element produit= new Element("produit");
		Element nom = new Element("nom").addContent(res.getString("nom"));
		Element prix = new Element("prix").addContent(res.getString("prix"));
		Element quantite = new Element("quantité").addContent(res.getString("quantite"));
		produit.addContent(nom).addContent(prix).addContent(quantite);
		racine.addContent(produit);
	}
	
	/**
	 * Afficher la liste des produits au format XML
	 */
	public void afficher() {
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());

		try {
			sortie.output(document, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
