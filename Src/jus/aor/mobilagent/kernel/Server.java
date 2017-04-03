/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel._Agent;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server implements _Server {
	/** le nom logique du serveur */
	protected String name;
	
	/** le port où sera ataché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected int port=10140;
	
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	
	/** le nom du logger */
	protected String loggerName;
	
	/** le logger de ce serveur */
	protected Logger logger=null;
	
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écuote du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			
			/* démarrage du server d'agents mobiles attaché à cette machine */
			this.agentServer = new AgentServer(port, name);
			this.agentServer.start();
			
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
			
		}catch(Exception ex){
			System.out.println("erreur durant le lancement du serveur"+this);
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			
			BAMServerClassLoader sLoader = new BAMServerClassLoader( new URL[] {}, this.getClass().getClassLoader());
			sLoader.addURL(new URL(codeBase));
			
			Class<?> sclasse = Class.forName(classeName,true,sLoader);
			
			Constructor<?> sConstructeur = sclasse.getConstructor(Object[].class);
			
			_Service<?> service = (_Service<?>) sConstructeur.newInstance(new Object[] {args});
			
			this.agentServer.addService(name, service);
			
		}catch(Exception ex){
			System.out.println(" erreur durant le lancement du serveur"+this);
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		try {
			System.out.println("Tentative de deploiement de "+classeName);
			//Etape 1 : charger la classe de l'agent
			BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(new URI(codeBase).getPath() ,this.getClass().getClassLoader());
			
			Class<?> aClass = Class.forName(classeName,true,agentLoader);
			
			
			//Etape 2 : Créer une instance l'agent
			Constructor<?> aConstructeur = aClass.getConstructor(Object[].class);
			
			_Agent agent = (_Agent) aConstructeur.newInstance(new Object[] {args});
			agent.init(this.agentServer, this.name);
			
			//Remplir la feuille de route
			if(etapeAction.size()!=etapeAddress.size()){throw new Exception();}
			
			int len = etapeAction.size();

			for(int i =0; i < len ; i++)
			{
				//On recupère le champs
				Field atribut = aClass.getDeclaredField(etapeAction.get(i));
				atribut.setAccessible(true);
				
				//ajoute une étape
				agent.addEtape(new Etape(new URI(etapeAddress.get(i)), (_Action) atribut.get(agent)));
			}
			
			//En terme de startAgent
			this.startAgent(agent, agentLoader);
					
		}catch(Exception ex){
			//System.out.println(" erreur durant le lancement du serveur"+this);
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	
	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son exécution
	 * immédiate.
	 * @param agent l'agent devant être exécuté
	 * @param loader le loader à utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		
		//ON créer un socket à partir de l'agent serveur
		Socket socket = new Socket(this.agentServer.site().getHost(), this.agentServer.site().getPort());
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		
		Jar jar = loader.extractCode();
		
		out.writeObject(jar);
		out.writeObject(agent);
		
		out.close();
		socket.close();
	}
}
