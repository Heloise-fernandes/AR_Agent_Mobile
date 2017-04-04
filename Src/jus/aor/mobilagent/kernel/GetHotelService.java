package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.hostel.Hotel;

public class GetHotelService implements _Service<List<Hotel>>, _Chaine{

	private ArrayList<Hotel> listeHotel;
	
	public GetHotelService(Object... args) {
		Document doc = null;
		DocumentBuilder docBuilder;
		try 
		{
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File((String) args[0]));
		} catch (ParserConfigurationException | SAXException | IOException e) {e.printStackTrace();}
		
		this.listeHotel = new ArrayList<>();
		
		if(doc!=null)
		{
			NamedNodeMap attrs;
			String localisation, name;
			for(Node item : iterable(doc,"Hotel")) {
				attrs = item.getAttributes();
				localisation = attrs.getNamedItem("localisation").getNodeValue();
				name = attrs.getNamedItem("name").getNodeValue();
				this.listeHotel.add(new Hotel(name, localisation));
			}
		}
		
	}
	
	@Override
	public List<Hotel> get(String localisation) {
		
		ArrayList<Hotel> hotelCherche = new ArrayList<>();
		for (Hotel hotel : this.listeHotel) {
		    if(hotel.equals(localisation))
		    {
		    	hotelCherche.add(hotel);
		    }
		}
		return hotelCherche;
	}

	@Override
	public List<Hotel> call(Object... params) throws IllegalArgumentException {
		
		ArrayList<Hotel> hotelCherche = new ArrayList<>();
		
		hotelCherche = (ArrayList) get((String) params[0]);
		
		return hotelCherche;
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
