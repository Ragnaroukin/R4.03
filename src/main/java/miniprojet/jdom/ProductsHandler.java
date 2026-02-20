package miniprojet.jdom;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.util.List;

public class ProductsHandler extends XMLHandler{
	
	public ProductsHandler() {
		super();
	}
	
	/**
	 * Importer les produits et les stocker dans le document XML de la classe
	 * @param file le fichier dont viennent les produits
	 */
	public void importProducts(String path) {
		File file = new File(path);
		
		SAXBuilder builder = new SAXBuilder();
	    
	    try {
	        document = builder.build(file);
	        root = document.getRootElement();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Ajouter les produits dans la base de données
	 */
	public void addProducts() {
		List<Element> products = root.getChildren("produit");
		
		for (Element product : products) {
            String name = product.getChildText("nom");
            double price = Double.parseDouble(product.getChildText("prix"));
            int quantity = Integer.parseInt(product.getChildText("quantité"));

            jdbc.insertProduit(name, quantity, price);
        }
	}
	
	/**
	 * Afficher la liste des produits au format XML
	 */
	public void printProducts() {
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());

	    try {
	        output.output(document, System.out);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
