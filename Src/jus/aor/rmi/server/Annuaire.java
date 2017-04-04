package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.rmi.common.*;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8828635014943186320L;
	private HashMap<String, Numero> annuaire = new HashMap<String, Numero>();

	protected Annuaire(String fichier) throws ParserConfigurationException, SAXException, IOException{
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    final DocumentBuilder builder = factory.newDocumentBuilder();       
		    final Document document= builder.parse(new File( fichier ));
			
		    final NodeList elements = document.getElementsByTagName("Telephone"); // on se met a la racine "root"

			
			for (int i = 0; i<elements.getLength(); i++) { // on boucle sur tous les enfants et on remplis la hashmap
				Node item = elements.item(i);
			    String name = item.getAttributes().getNamedItem("name").getNodeValue();
			    Numero num = new Numero(item.getAttributes().getNamedItem("numero").getNodeValue());
			    
			    System.out.println(name+ num);
				this.annuaire.put(name, num);
			
			}

		
		
	}
	@Override
	public Numero get(String abonne) {
		return this.annuaire.get(abonne);
	}

}
