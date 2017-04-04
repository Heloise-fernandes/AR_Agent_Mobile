package jus.aor.mobileagent.lookforhostel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import jus.aor.mobilagent.hostel.Hotel;
import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;

public class LookForHotel extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Hotel> hotels;
	private ArrayList<Numero> numeros;
	private String localisation;
	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public LookForHotel(Object... args) {
		super();
		hotels = new ArrayList<Hotel>();
		numeros = new ArrayList<Numero>();
		localisation = (String) args[0];
	 }
	 
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action getHotel = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		
		public void execute() {
			_Service<?> service = as.getService("Hotels");
			hotels.addAll((Collection<? extends Hotel>) service.call(localisation));
			logger.log(Level.FINE,this+" ajout d'une liste d'hotels appartenant à la localisation " + localisation+" sur le serveur "+as);
		}
		
		public String toString(){return "LookForHotel getHotel:";}
	};
	
	 /**
	 * l'action à entreprendre sur le serveur annuaire 
	 */
	protected _Action getNumero = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void execute() {
			
			numeros.addAll();
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
				//listeServeur=listeServeur+"\n"+as.site();
				logger.log(Level.FINE, this.toString()+listeServeur);
			}
			
			public String toString(){return "Hello Retour :";}
		};
	}

}
