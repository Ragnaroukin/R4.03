package miniprojet;

import miniprojet.jdom.AfficherProduits;
import miniprojet.jdom.ExporterCommandes;

public class Main {

	public static void main(String[] args) {
		AfficherProduits afficheur = new AfficherProduits();
		
		afficheur.ajouterProduit("Clavier","10","200");
		
		afficheur.afficher();
		
		ExporterCommandes export = new ExporterCommandes();
		
		export.ajouterCommande(1);
		
		export.exporter();
	}

}
