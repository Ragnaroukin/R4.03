package miniprojet.jdom;

import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;

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
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Ajouter les produits dans la base de données
	 */
	public void addProducts() {
		
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
