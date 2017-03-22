package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader{

	
	public BAMServerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls,parent);
	}
	
	public void addURL(URL u)
	{
		super.addURL(u);
	}
	
	public String toString()
	{
		return super.toString();
	}

}
