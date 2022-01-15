package ie.gmit.dip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map.Entry;


/**
*
* @author Conor Timlin
* Impl Class of Abstract Parser class. Provides small amount of additional refining of behaviors of Abstract class Parser, which it extends.
* (In good place to be expanded upon)
* 
*/

public class URLParser extends Parser {

	/**
	 * Parse method accepts string source for WordCloud analysis. Delegates to its parent, abstract class Parser and provides DataSource enum
	 * to indicate how to process the source. (More concrete implementation should have been here)
	 * @param source - String containing source to a Static URL
	 * Complexity: O(1) - Simple method invocation
	 */
	@Override
	public void parse(String source) throws IOException {
		super.addToHashMap(source, DataSource.URL);
	}
	
	
	/** Method to initialise ignorewords file. Converts source string to File and invokes superclass' addtoIgnoreWords method
	 * @param source - Text file of common words to ignore in Word Cloud
	 * Complexity: O(1) - Simple method invocation 
	 */
	public void initialiseIgnoreWords() throws NullPointerException, IOException {
		File defaultIgnoreWords = new File("./ignorewords.txt");
		super.addToIgnoreWords((defaultIgnoreWords));
	}

	
	/**
	 * SortByWordFrequency Abstracts sorting the frequencyTable and assigning to TreeSet to its parent class
	 * @param frequencyTable - Hashmap<String,Integer> that will track words(keys) and their Integer values. 
	 * @param numberOfWords  - Number of words user wishes to track in their word cloud
	 * @return - Returns sorted TreeSet<Entry<String,Integer> based on frequencyTable and the Supplied EntryComparator
	 * Complexity: O(1) - Simple Method Invocation
	 */
	public TreeSet<Entry<String, Integer>> sortByWordFrequency(HashMap<String, Integer> frequencyTable,
			int numberOfWords) {
		return super.sortByWordFrequency(frequencyTable);
	}

	/**
	 * Overloaded method to intialiseIgnoreWords. If user doesn't provide a good path for custom ignorewords.txt file
	 * this parameterless overloaded method is instead invoked and the default ignorewords.txt used
	 * Complexity: O(1) - Assigning file to known value and simple method invocation
	 */
	@Override
	public void initialiseIgnoreWords(File source) throws FileNotFoundException, IOException {
		super.addToIgnoreWords((source));
	}

}
