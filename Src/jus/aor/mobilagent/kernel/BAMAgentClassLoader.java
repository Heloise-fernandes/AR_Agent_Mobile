package jus.aor.mobilagent.kernel;
/**
 * #TODO
 * @author cheva
 *
 */
public class BAMAgentClassLoader extends ClassLoader {

	Jar code;
	public BAMAgentClassLoader(String host, ClassLoader parent) {
		super(parent);
		//TODO
		
	}

	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	public void integrateCode(Jar jar){
		code = jar;
	}
	
	public String className(String name){
		//TODO
		return "";
	}
	
	public Jar extractCode(){
		return code;
	}
	
	public String toString(){
		//TODO
		return "BMA ";
	}
}
