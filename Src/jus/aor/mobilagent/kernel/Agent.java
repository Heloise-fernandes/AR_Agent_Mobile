package jus.aor.mobilagent.kernel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Agent implements _Agent {
	
	transient protected AgentServer as;
	private Route route;
	transient private String serverName;
	transient protected Logger logger;
	private int tailleAgent = 0;
	private int tailleJar = 0;

	public void run() {
		System.out.println("Agent run");
		if(route.hasNext()){
			Etape e = route.next();
			e.action.execute();
			if(route.hasNext()){
				this.move();
			}
			else{
				this.move(route.retour.server);
			}
		}
		else{
			this.route.retour.action.execute();
		}
	}

	public void init(AgentServer agentServer, String serverName) {
		System.out.println("Agent init");
		try {
			logger = Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+serverName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.log(Level.FINE, "Construction de l'agent "+this);
		as = agentServer;
		this.serverName = serverName;
		
		//Action au retour
		this.route = new Route(new Etape(agentServer.site(), this.retour()));

		//Action de départ
		this.route.add(new Etape(agentServer.site(), _Action.NIHIL));
		
	}

	public void reInit(AgentServer server, String serverName) {
		as = server;
		this.serverName = serverName;
		try {
			logger = Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+serverName);
			logger.log(Level.FINE, "Reconstruction de l'agent "+ this);
			logger.log(Level.FINE, "Nb octet transmit d'agent "+ tailleAgent);
			logger.log(Level.FINE, "Nb octet transmit de JAR "+ tailleJar);
			logger.log(Level.FINE, "Nb octet transmit TOTAL "+ (tailleJar+tailleAgent));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addEtape(Etape etape) {
		route.add(etape);
	}

	public String toString(){
		return "Agent : serveurName="+serverName+" route="+route;
	}
	
	protected abstract _Action retour();
	
	protected _Service<?> getService(String nomService){
		return as.getService(nomService);
	}
	
	private void move(){
		this.move(route.get().server);
	}
	
	protected void move(URI adresseProchainServeur){
		try {
			Socket s = new Socket(adresseProchainServeur.getHost(), adresseProchainServeur.getPort());
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			//TODO peut être un os2 necessaire pour l'envoie
			
			BAMAgentClassLoader bacl = (BAMAgentClassLoader) this.getClass().getClassLoader();
			Jar jar = bacl.extractCode();
			
			this.tailleJar += getTaille(jar);
			os.writeObject(jar);
			this.tailleAgent += getTaille(this);
			os.writeObject(this);
			logger.log(Level.FINE, "Move de l'agent de "+this+" vers "+adresseProchainServeur);
			os.close();
			s.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected String route(){
		return route.toString();
	}
	
	//Permet de savoir la taille des objets qu'on envoie.
	//C'est absolument immonde comme code
	//src : http://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object
	private int  getTaille(Object o){
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return baos.size();
		
	}
	
	
}
