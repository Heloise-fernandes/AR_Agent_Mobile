package jus.aor.mobilagent.hostel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Annuaire;
import jus.aor.mobilagent.kernel._Service;

public class Annuaire implements _Service<Numero>, _Annuaire {

	private HashMap<String, Numero> annuaire;

	protected Annuaire(Object ...args) throws ParserConfigurationException, SAXException, IOException{

		Document doc = null;
		DocumentBuilder docBuilder;
		try 
		{
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File((String) args[0]));
		} catch (ParserConfigurationException | SAXException | IOException e) {e.printStackTrace();}
		
		this.annuaire = new HashMap<String, Numero>();
		
		if(doc!=null)
		{
			NodeList elements = doc.getElementsByTagName("Téléphone");

			for (int i = 0; i<elements.getLength(); i++) { // on boucle sur tous les noeuds et on remplis la hashmap
				Node item = elements.item(i);
				String name = item.getAttributes().getNamedItem("name").getNodeName();
				Numero num = new Numero(item.getAttributes().getNamedItem("name").getNodeName());
				this.annuaire.put(name, num);
	
			}
		}
	}

	@Override
	public Numero get(String abonne) {
		return this.annuaire.get(abonne);
	}

	@Override
	public Numero call(Object... params) throws IllegalArgumentException {
		Numero num = get((String) params[0]);
		
		return num;
	}

}