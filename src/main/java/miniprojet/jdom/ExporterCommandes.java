package miniprojet.jdom;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import miniprojet.Main;

public class ExporterCommandes extends XMLHandler {
	
	public ExporterCommandes() {
		super("commandes");
	}
	
	public void exporter() {
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		
		try(OutputStream out = new FileOutputStream(Main.class.getResource("export_commandes.xml").getFile())) {
			sortie.output(document, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
