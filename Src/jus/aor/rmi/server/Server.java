package jus.aor.rmi.server;


import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
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
		System.setSecurityManager(new SecurityManager());
		try {
			Registry registre;
			for (int i = 1; i <= nbChaines; i++) {
				registre = LocateRegistry.createRegistry(port + i);
				// Bind de chaine dans registry
				Chaine c = new Chaine("DataStore/Hotels" + i+ ".xml");
				registre.bind("chaine" + i, c);
				System.out.println("Chemin " + i + " bind avec succès.");
			}

			registre = LocateRegistry.createRegistry(port + nbChaines + 1);
			Annuaire a = new Annuaire("DataStore/Annuaire.xml");
			registre.bind("annuaire", a);	


		}catch (Exception e) {
			e.printStackTrace();

		}
	}
}
