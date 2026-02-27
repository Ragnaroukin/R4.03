package miniprojet.jdom;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.util.List;

public class ProductsImport extends XMLHandler{
	
	public ProductsImport() {
		super();
	}
	
	/**
	 * Importer les produits depuis un fichier XML
	 * @param path le chemin du fichier d'import
	 * @return la réussite de l'import
	 */
	public boolean importProducts(String path) {
		File file = new File(path);
		
		SAXBuilder builder = new SAXBuilder();
	    
	    try {
	        document = builder.build(file);
	        root = document.getRootElement();
	        printProducts();
	        addProducts();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Ajouter les produits dans la base de données
	 */
	private void addProducts() {
		List<Element> products = root.getChildren("produit");
		
		String name;
		double price;
		int quantity;
		
		for (Element product : products) {
            name = product.getChildText("nom");
            price = Double.parseDouble(product.getChildText("prix"));
            quantity = Integer.parseInt(product.getChildText("quantité"));

            jdbc.insertProduct(name, quantity, price);
        }
	}
	
	/**
	 * Afficher la liste des produits au format XML
	 */
	private void printProducts() {
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());

	    try {
	        output.output(document, System.out);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
