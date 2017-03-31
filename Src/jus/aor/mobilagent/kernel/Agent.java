package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

public abstract class Agent implements _Agent {
	
	protected AgentServer as;
	private Route route;
	private String serverName;

	public void run() {
		if(route.hasNext()){
			Etape e = route.next();
			e.action.execute();
			if(route.hasNext()){
				this.move();
			}
		}
	}

	public void init(AgentServer agentServer, String serverName) {
		as = agentServer;
		this.serverName = serverName;
		
		//Action au retour
		this.route = new Route(new Etape(agentServer.site(), _Action.NIHIL));

		//Action de départ
		this.route.add(new Etape(agentServer.site(), _Action.NIHIL));
	}

	public void reInit(AgentServer server, String serverName) {
		as = server;
		this.serverName = serverName;

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
			os.writeObject(jar);
			os.writeObject(this);
			
			os.close();
			s.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected String route(){
		return route.toString();
	}
	
	
}
