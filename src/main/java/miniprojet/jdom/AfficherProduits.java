package miniprojet.jdom;

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
	public void ajouterProduit(String nom, String prix, String quantite) {
		Element produit = new Element("produit");
		Element elementNom = new Element("nom").addContent(nom);
		Element elementPrix = new Element("prix").addContent(prix);
		Element elementQuantite = new Element("quantité").addContent(quantite);
		produit.addContent(elementNom).addContent(elementPrix).addContent(elementQuantite);
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
