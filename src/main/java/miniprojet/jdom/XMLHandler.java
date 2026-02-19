package miniprojet.jdom;

import org.jdom2.Document;
import org.jdom2.Element;

public abstract class XMLHandler {
	protected Document document;
	protected Element racine;
	protected JDBC jdbc;
	
	public XMLHandler(String racine) {
		this.document = new Document();
		this.racine = new Element(racine);
		this.document.setRootElement(this.racine);
		this.jdbc = JDBC.getInstance();
	}
}
