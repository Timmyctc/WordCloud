package ie.gmit.dip;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;


/**
 *
 * @author Conor Timlin
 * Parseable interface describes any class or object that parses a source for text to analyse.
 * See Parser
 */
public interface Parseable {
	
	/**
	 * Accepts source to analyse text
	 * @param source
	 * @throws IOException
	 */
	public void parse(String source) throws IOException;
}
