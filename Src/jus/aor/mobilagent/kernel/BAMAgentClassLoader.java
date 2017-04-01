package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarException;


public class BAMAgentClassLoader extends ClassLoader {

	Jar code;
	public BAMAgentClassLoader(String jarName, ClassLoader parent) {
		super(parent);
		try {
			integrateCode(new Jar(jarName));
		} catch (JarException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	public void integrateCode(Jar jar){
		code = jar;
		
		// On define les classe une par une (On est sur que toutes seront utilisées)
		for(Iterator<Map.Entry<String,byte[]>> iter = code.iterator(); iter.hasNext();) {
			Map.Entry<String, byte[]> currentClass = iter.next();
			this.defineClass(className(currentClass.getKey()), currentClass.getValue(), 0, currentClass.getValue().length);
		}
	}
	
	public String className(String name){
		return name.substring(0, name.length()-6).replace('/', '.');
	}
	
	public Jar extractCode(){
		return code;
	}
	
	public String toString(){
		return "BMA - " + code.toString();
	}
}
