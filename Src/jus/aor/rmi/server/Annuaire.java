package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
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

public class Annuaire implements _Annuaire{

	
	private HashMap<String, Numero> annuaire = new HashMap<String, Numero>();
	
	private Annuaire(String fichier){
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
		    final DocumentBuilder builder = factory.newDocumentBuilder();       
		    final Document document= builder.parse(new File("repertoire.xml"));
			
		    final Element racine = document.getDocumentElement(); // on se met a la racine "root"
			final NodeList racineNoeuds = racine.getChildNodes(); //on se met sur un enfant "Téléphone"
			final int nbRacineNoeuds = racineNoeuds.getLength(); // On recupere le nombre d'enfant 
			
			for (int i = 0; i<nbRacineNoeuds; i++) { // on boucle sur tous les enfants et on remplis la hashmap
				Node item = racineNoeuds.item(i);
			    String name = item.getAttributes().getNamedItem("name").getNodeName();
			    Numero num = new Numero(item.getAttributes().getNamedItem("name").getNodeName());
				this.annuaire.put(name, num);
			}


		}
		catch (final ParserConfigurationException e) {
		    e.printStackTrace();
		}
		catch (final SAXException e) {
		    e.printStackTrace();
		}
		catch (final IOException e) {
		    e.printStackTrace();
		}
		


		


		
		
	}
	@Override
	public Numero get(String abonne) {
		return this.annuaire.get(abonne);
	}

}
