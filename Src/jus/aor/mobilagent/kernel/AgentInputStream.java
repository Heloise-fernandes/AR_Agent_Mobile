package jus.aor.mobilagent.kernel;

import java.io.InputStream;
import java.io.ObjectStreamClass;

public class AgentInputStream {

	private BAMAgentClassLoader loader;
	private InputStream input;
	
	public AgentInputStream(InputStream in, BAMAgentClassLoader a)
	{
		this.input = in;
		this.loader = a;
	}
	
	public <T> Class<T> resolveClass(ObjectStreamClass c)
	{
		//TODO	
		return null;
	}

}
