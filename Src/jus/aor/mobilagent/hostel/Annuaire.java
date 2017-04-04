package jus.aor.mobilagent.hostel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Annuaire;

public class Annuaire implements _Annuaire {

	private HashMap<String, Numero> annuaire = new HashMap<String, Numero>();

	protected Annuaire(String fichier) throws ParserConfigurationException, SAXException, IOException{

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();       
		final Document document= builder.parse(new File( fichier ));

		final NodeList elements = document.getElementsByTagName("Téléphone");


		for (int i = 0; i<elements.getLength(); i++) { // on boucle sur tous les noeuds et on remplis la hashmap
			Node item = elements.item(i);
			String name = item.getAttributes().getNamedItem("name").getNodeName();
			Numero num = new Numero(item.getAttributes().getNamedItem("name").getNodeName());
			this.annuaire.put(name, num);

		}
	}

	@Override
	public Numero get(String abonne) {
		return this.annuaire.get(abonne);
	}

}