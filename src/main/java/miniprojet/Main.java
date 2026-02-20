package miniprojet;

import miniprojet.jdom.ProductsHandler;

public class Main {

	public static void main(String[] args) {
		ProductsHandler productsHandler =  new ProductsHandler();
		
		productsHandler.importProducts("produits.xml");
	}
	

}
