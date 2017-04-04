package jus.aor.mobilagent.hello;


import java.util.logging.Level;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String listeServeur;
	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		super();
		listeServeur="";
	 }
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void execute() {
			listeServeur=listeServeur+"\n"+as.site();
			logger.log(Level.FINE, this.toString()+" "+as.site()+":  Executing action doIt");
		}
		
		public String toString(){return "Hello DoIt :";}
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
