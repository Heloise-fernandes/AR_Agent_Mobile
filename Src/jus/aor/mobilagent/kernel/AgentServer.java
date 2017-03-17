package jus.aor.mobilagent.kernel;

import java.util.List;
/**
 * Classe en cours de construction #TODO
 * @author cheva
 *
 */
public class AgentServer extends Thread{
	private List<_Service> listeService;
	private int port;
	private String name;
	
	public AgentServer(int port, String name ){
		this.port = port;
		this.name = name;
	}
	
	
	
	
	public void run(){
		while(true){
			//#TODO accept faire des socket comme le TP socket
		}
	}
}
