package miniprojet.jdom;

import org.jdom2.Document;
import org.jdom2.Element;

public abstract class XMLHandler {
	protected Document document;
	protected Element racine;
	
	public XMLHandler() {
		document = new Document();
		racine = new Element("produits");
		document.setRootElement(racine);
	}
}
