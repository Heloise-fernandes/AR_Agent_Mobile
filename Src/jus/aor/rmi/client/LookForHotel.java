package jus.aor.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.List;

import jus.aor.rmi.common.*;


/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel{
	/** le critère de localisaton choisi */
	private String localisation;
	private int port= 1099;
	private int nbChaines = 4;
	private _Annuaire annuaire;
	private List<Hotel> hotelList = new ArrayList<Hotel>();

	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String[] args) throws RemoteException, NotBoundException{
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		if(args.length == 0){System.out.println("Error : Veuillez entrez une localisation");System.exit(1);}		
		if(args.length == 1){
			this.localisation = args[0];
			System.out.println("Location : " + localisation);
		}
	}
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 * @throws NotBoundException 
	 */
	
	public long call() throws RemoteException, NotBoundException {
	
		long tpsD = System.currentTimeMillis(); // On recupere le temps de debut de traitement 

		Registry registre;
		
		try {
			//Pour chaque chaine d'hotel on va regarder dans son registre a son port attibué les hotels quelle possede dans la localisation 
			for (int i = 1; i <= this.nbChaines; i++) {
				registre = LocateRegistry.getRegistry(this.port + i);
				this.hotelList.addAll((List<Hotel>) ((_Chaine) registre.lookup("chaine" + i)).get(this.localisation));				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//Dans le dernier egistre on recupère l'annuaire qui va avec 
		registre = LocateRegistry.getRegistry(this.port + (this.nbChaines+1));
		this.annuaire = (_Annuaire) registre.lookup("annuaire");
		
		long tpsF = System.currentTimeMillis(); // On recupere le temps de fin de traitement 
		
		//Affichage des Hotels
		System.out.println("Hotels :");
		for(Hotel h : hotelList) {
			System.out.println(h.toString());
		}

		return  ( tpsF - tpsD);
	}

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		LookForHotel client = new LookForHotel(args);
		long value = client.call();
		System.out.println("La requête est traité en : " + value + " ms.");

		

	}
	
}
