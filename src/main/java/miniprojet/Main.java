package miniprojet;

import java.util.Scanner;

import miniprojet.jdom.CommandImport;
import miniprojet.jdom.CommandsExport;
import miniprojet.jdom.ProductsImport;

public class Main {

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Bienvenue sur votre gestionnaire de commandes !");

		while (true) {
			System.out.println("\nQue souhaitez vous faire ?\n" + "0 : Quitter\n" + "1 : Importer des produits\n"
					+ "2 : Importer une commande\n" + "3 : Exporter les commandes\n" + "\nEntrez votre choix :");
			switch (sc.nextLine().trim()) {
			case "0" -> {
				sc.close();
				System.exit(0);
			}
			case "1" -> importProducts();
			case "2" -> importCommand();
			case "3" -> exportCommands();
			default -> System.err.println("Choix invalide !\n");
			}
		}
	}

	private static void importProducts() {
		ProductsImport productsImport = new ProductsImport();
		System.out.println("Entrez le fichier d'import des produits :");
		String path = sc.nextLine();
		if (productsImport.importProducts(path))
			System.out.println("\nOpération réussie !\n");
		else
			System.err.println("\nOpération échouée !\n");
	}

	private static void importCommand() {
		CommandImport commandImport = new CommandImport();
		System.out.println("Entrez le fichier d'import de la commande :");
		String path = sc.nextLine();
		if (commandImport.importCommand(path))
			System.out.println("\nOpération réussie !\n");
		else
			System.err.println("\nOpération échouée !\n");
	}

	private static void exportCommands() {
		CommandsExport commandsExport = new CommandsExport();
		if(commandsExport.exportCommands())
			System.out.println("\nOpération réussie !\n");
		else
			System.err.println("\nOpération échouée !\n");
	}
}
