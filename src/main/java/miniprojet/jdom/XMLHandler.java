package miniprojet.jdom;

import org.jdom2.Document;
import org.jdom2.Element;

import miniprojet.jdbc.JDBC;

public class XMLHandler {
	/**
	 * Le document XML où l'on va écrire l'export
	 */
	protected Document document;
	
	/**
	 * La racine de notre XML
	 */
	protected Element root;
	
	/**
	 * L'objet pour interagir avec la base de données
	 */
	protected JDBC jdbc;
	
	public XMLHandler(String rootLabel) {
		this();
		root = new Element(rootLabel);
		document.setRootElement(root);
	}
	
	public XMLHandler() {
		document = new Document();
		jdbc = JDBC.getInstance();
	}
}
