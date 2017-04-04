package jus.aor.rmi.server;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {

		int port = 1099;
		int nbChaines = 4;

		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Error : Pas de numero de port.");
				System.exit(1);
			}
		}
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			Registry registre;
			for (int i = 1; i <= nbChaines; i++) {
				//Pour chaque chaine d'hotel on créer un registre avec un port donné
				registre = LocateRegistry.createRegistry(port + i);
				//On pars le fichier associer a cette chaine
				Chaine c = new Chaine("DataStore/Hotels" + i + ".xml");
				//On classe cette chaine dans le registre 
				registre.bind("chaine" + i, c);
			}

			//Le dernier registre contient l'annuaire
			registre = LocateRegistry.createRegistry(port + nbChaines + 1);
			Annuaire a = new Annuaire("DataStore/Annuaire.xml");
			registre.bind("annuaire", a);	


		}catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("Server ready !");
	}
}
