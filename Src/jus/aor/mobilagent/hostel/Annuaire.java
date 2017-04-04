package jus.aor.mobilagent.hostel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Annuaire;
import jus.aor.mobilagent.kernel._Service;

public class Annuaire implements _Service<Numero>, _Annuaire {

	private HashMap<String, Numero> annuaire;

	public Annuaire(Object ...args) throws ParserConfigurationException, SAXException, IOException{

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
			NodeList elements = doc.getElementsByTagName("Telephone");

			for (int i = 0; i<elements.getLength(); i++) { // on boucle sur tous les enfants et on remplis la hashmap
				Node item = elements.item(i);
			    String name = item.getAttributes().getNamedItem("name").getNodeValue();
			    Numero num = new Numero(item.getAttributes().getNamedItem("numero").getNodeValue());
			    
				this.annuaire.put(name, num);
			
			}
		}
	}

	@Override
	public Numero get(String abonne) {
		System.out.println("Hotel :"+abonne);
		return this.annuaire.get(abonne);
	}

	@Override
	public Numero call(Object... params) throws IllegalArgumentException {
		Numero num = get((String) params[0]);
		
		return num;
	}
	
	private static Iterable<Node> iterable(final Node racine, final String element){
		return new Iterable<Node>() {
			public Iterator<Node> iterator(){
				return new Iterator<Node>() {
					NodeList nodelist;
					int current = 0, length;
					{ //init
						try{
							nodelist = ((Document)racine).getElementsByTagName(element);
						}catch(ClassCastException e){
							nodelist = ((Element)racine).getElementsByTagName(element);
						}
						length = nodelist.getLength();
					}
					public boolean hasNext(){return current<length;}
					public Node next(){return nodelist.item(current++);}
					public void remove(){}
				};
			}
		};
	}

}