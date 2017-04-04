package jus.aor.mobilagent.lookforhostel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.hostel.Hotel;
import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.AgentServer;
import jus.aor.mobilagent.kernel.Etape;
import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;

public class LookForHotelTime extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Collection<Hotel> hotels = new LinkedList<Hotel>();
	private ArrayList<Hotel> hotels;
	private HashMap<Hotel,Numero> numeros;
	private String localisation;
	transient private Long timeBegin;
	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public LookForHotelTime(Object... args) {
		super();
		hotels = new ArrayList<Hotel>();
		numeros = new HashMap<>();
		localisation = (String) args[0];
		timeBegin = System.currentTimeMillis();
		
	 }
	 
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action findHotel = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		
		public void execute() {
			_Service<?> service = as.getService("Hotels");
			hotels.addAll((Collection<? extends Hotel>) service.call(localisation));
			logger.log(Level.FINE,this+" ajout d'une liste d'hotels appartenant à la localisation " + localisation+" sur le serveur "+as+" taille hotel : "+hotels.size());
		}
		
		public String toString(){return "LookForHotel getHotel:";}
	};
	
	 /**
	 * l'action à entreprendre sur le serveur annuaire 
	 */
	protected _Action findTelephone = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void execute() {
			_Service<?> service = as.getService("Telephones");
			//System.out.println("Numeros est null :"+(numeros==null)+" - hotel taille :"+hotels.size());
			for(Hotel hotel : hotels) {
				
				//System.out.println("Je cherche :"+hotel.name);
				Numero num = (Numero) service.call(hotel.name);
				
				numeros.put(hotel,num);
			}
			
			logger.log(Level.FINE,this+" récupération des numéros " + localisation+" sur le serveur "+as);
		}
		
		public String toString(){return "LookForHotel getNumero:";}
	};

	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	protected _Action retour(){
		return new _Action(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void execute() {
				_Service<?> service = as.getService("Duration");
				Long finalTime = (Long) service.call();
				
				
				
				timeBegin = (Long) service.call();
				for(Hotel hotel : numeros.keySet()) {
					logger.log(Level.FINE, hotel.name+" : "+numeros.get(hotel).numero);
					//logger.log(Level.FINE, "Tu m'énerves!");
					System.out.println(hotel.name+" : "+numeros.get(hotel).numero);
				}
				logger.log(Level.FINE, "Temps "+(finalTime-timeBegin));
				System.out.println("Temps "+(finalTime-timeBegin));
			}
			
			public String toString(){return "LookForHotel Retour :";}
		};
	}

}
