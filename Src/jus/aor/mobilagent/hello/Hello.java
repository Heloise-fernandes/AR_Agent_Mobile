package jus.aor.mobilagent.hello;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.AgentServer;
import jus.aor.mobilagent.kernel.Etape;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Agent;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		super();
	 }
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){

		@Override
		public void execute() {
			System.out.println(as.site());	
		}
	};
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	@Override
	protected _Action retour(){
		 return this.doIt;
	}
	
}
