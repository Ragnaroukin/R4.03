package miniprojet;

import miniprojet.jdom.AfficherProduits;

public class Main {

	public static void main(String[] args) {
		AfficherProduits afficheur = new AfficherProduits();
		
		afficheur.ajouterProduit("Clavier","10","200");
		
		afficheur.afficher();
	}

}
