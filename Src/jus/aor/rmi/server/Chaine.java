package jus.aor.rmi.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common.Numero;
import jus.aor.rmi.common._Chaine;

public class Chaine implements _Chaine {
	
	private List<Hotel> ListH = new ArrayList<Hotel>();

	public Chaine(String fichier){
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
		    final DocumentBuilder builder = factory.newDocumentBuilder();       
		    final Document document= builder.parse(new File( fichier ));
			
		    final Element racine = document.getDocumentElement(); // on se met a la racine "root"
			final NodeList racineNoeuds = racine.getChildNodes(); //on se met sur un enfant "Hotel"
			final int nbRacineNoeuds = racineNoeuds.getLength(); // On recupere le nombre d'enfant 
			
			for (int i = 0; i<nbRacineNoeuds; i++) { // on boucle sur tous les enfants et on remplis la hashmap
				Node item = racineNoeuds.item(i);
			    String name = item.getAttributes().getNamedItem("name").getNodeValue();
			    String loca = item.getAttributes().getNamedItem("localisation").getNodeValue();
				this.ListH.add(new Hotel(name, loca));
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
	
	public List<Hotel> get(String localisation) {
		List<Hotel> finish = new ArrayList<Hotel>();
		
		for (Hotel hotel : this.ListH) {
			if (localisation.equals(hotel.localisation)) {
				finish.add(hotel);
			}		
		}
		return finish;
	}

}
