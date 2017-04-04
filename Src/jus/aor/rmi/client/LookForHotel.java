package jus.aor.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.List;

import jus.aor.rmi.common.*;
import jus.aor.rmi.server.Annuaire;
import jus.aor.rmi.server.Chaine;
import sun.security.util.Length;

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
	private Annuaire annuaire;
	private List<_Chaine> chaineList = new ArrayList<_Chaine>();
	private List<Hotel> hotelList = new ArrayList<Hotel>();

	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String[] args) throws RemoteException, NotBoundException{
		//1er argument localisation, 2ieme port de base, 3ieme nb de chaine

		if(args.length == 0){System.out.println("erreur");}		
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
		long tps = System.currentTimeMillis();
		
		Registry registre;
		
		try {
			for (int i = 1; i <= this.nbChaines; i++) {
				registre = LocateRegistry.getRegistry(this.port + i);
				this.chaineList.add((_Chaine) registre.lookup("chaine" + i)); //On recupere toutes les chaines d'hotel
				this.hotelList.addAll((List<Hotel>) ((_Chaine) registre.lookup("chaine" + i)).get(this.localisation));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Hotels :");
		for(Hotel h : hotelList) {
			System.out.println(h.toString());
		}
		
				
		registre = LocateRegistry.getRegistry(this.port + (this.nbChaines+1));
		this.annuaire = (Annuaire) registre.lookup("annuaire");


		return  (System.currentTimeMillis() - tps);
	}

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		LookForHotel client = new LookForHotel(args);
		long temps = client.call();
		System.out.println("La requête est traité en : " + temps + " ms");

		

	}
	
}
