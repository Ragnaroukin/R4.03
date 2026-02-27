package miniprojet;

import miniprojet.jdom.CommandImport;
import miniprojet.jdom.CommandsExport;
import miniprojet.jdom.ProductsImport;

public class Main {

	public static void main(String[] args) {
		CommandImport commandImport = new CommandImport();
		CommandsExport commandsExport = new CommandsExport();
		ProductsImport productsImport = new ProductsImport();
		
		//productsImport.importProducts("produits.xml");
		
		//commandImport.importCommand("commande1.xml");
		//commandImport.importCommand("commande2.xml");
		//commandImport.importCommand("commande3.xml");
		
		commandsExport.exportCommands();

	}
}
