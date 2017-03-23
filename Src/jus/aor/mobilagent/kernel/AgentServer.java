package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
/**
 * Classe en cours de construction #TODO
 * @author cheva
 *
 */
public class AgentServer extends Thread{
	private Map<String,_Service> listeService;
	private int port;
	private String name;
	private boolean isalive;
	
	public AgentServer(int port, String name ){
		this.port = port;
		this.name = name;
	}
	
	public void run(){
		//accept faire des socket comme le TP socket
		this.isalive = true;
		try 
		{
			
			ServerSocket serverTCPSoc = new ServerSocket(this.port);
			while(this.isalive)
			{
				Socket client = serverTCPSoc.accept();
				//Ecouter
				_Agent a = this.getAgent(client);
				//lancer l'agent 
				new Thread(a).start();
			}
			serverTCPSoc.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public _Agent getAgent(Socket client)
	{
		//Lire le jar
		try 
		{
			ObjectInputStream lecteurJar = new ObjectInputStream(client.getInputStream());
			Object o = lecteurJar.readObject();
			if(o instanceof Jar)
			{
				Jar jarjar = (Jar) o;
				
				BAMAgentClassLoader bamLoader = new BAMAgentClassLoader(this.getContextClassLoader());
				bamLoader.integrateCode(jarjar);
				AgentInputStream input = new AgentInputStream(client.getInputStream(), bamLoader);
				Object possibleAgent = input.readObject();
				if(possibleAgent instanceof _Agent)
				{
					_Agent agent = (_Agent) possibleAgent;
					return agent;
				}
				else
				{
					throw new ClassNotFoundException();
				}
			}
			else
			{
				throw new ClassNotFoundException();
			}
			
		} 
		catch (IOException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
		
		return null;
	}
	
	public void addService(String nom , _Service<?> service)
	{
		this.listeService.put(nom,service);
	}
	
	public String toString()
	{
		String s = "AgentServeur-> Port :"+this.port+",Name :"+ this.name+"\n";
		for (String nom : this.listeService.keySet()) {
			s = s+"Service "+nom+"\n";
		}
		s=s+"En activité? "+isalive+"\n";
		return s;
	}
	
	public _Service<?> getService(String nom)
	{
		return this.listeService.get(nom);
	}
	
	public URI site()
	{
		URI u = null;
		try {u = new URI("MobileAgent://"+InetAddress.getLocalHost().getHostName()+":"+this.port);} 
		catch (URISyntaxException e) {e.printStackTrace();} 
		catch (UnknownHostException e) {e.printStackTrace();}
		return u;
	}
}
