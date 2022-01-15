package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * 
 * @author Conor Timlin
 * Abstract Class Parser, is a Parseable therefore implements the Parseable interface. 
 * Admittedly provides the majority of the implementation for Parsing text from Both URL and File.
 * Provides a great degree of abstraction from FileParser and UrlParser.
 *
 */

public abstract class Parser implements Parseable {

	/**
	 * @param ignoreWordsSet - HashSet of common words to ignore from Word Cloud
	 * @param frequencyTable - HashMap of Words and their Frequencies
	 * @param FREQUENCY_COMPARATOR	- Comparator object to sort HashMap entrySet
	 * 
	 * 
	 */
	
	public final Set<String> ignoreWordsSet = new HashSet<String>();

	// HashMap that counts words and their frequency
	public HashMap<String, Integer> frequencyTable = new HashMap<String, Integer>();

	public static final EntryComparator FREQUENCY_COMPARATOR = new EntryComparator();

	
	/**
	 * Abstract Parse Method, Implementation is delegated to implementing classes
	 */
	@Override
	public abstract void parse(String source) throws IOException;

	/**
	 * Abstract method to initialse IgnoreWords file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract void initialiseIgnoreWords() throws FileNotFoundException, IOException;

	/**
	 * Overloaded method containing File argument, allows user to provide custom ignorewords file
	 * @param f - File containing common words to ignore
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract void initialiseIgnoreWords(File f) throws FileNotFoundException, IOException;

	
	/**
	 * 
	 * @param frequencyTable - HashMap of Words(keys) and their Frequencies(values)
	 * @return sortedSetByWordFrequency - TreeSet of Entrysets generated from the hashmap's .entrySet() method, sorted by our EntryComparator.
	 * 
	 */
	public TreeSet<Entry<String, Integer>> sortByWordFrequency(HashMap<String, Integer> frequencyTable) {

		TreeSet<Entry<String, Integer>> sortedSetByWordFrequency = new TreeSet<Entry<String, Integer>>(
				FREQUENCY_COMPARATOR);

		sortedSetByWordFrequency.addAll(frequencyTable.entrySet()); // ADDING ENTRY SET
		return sortedSetByWordFrequency;
	}

	/**
	 * Method that takes File f, common words to ignore, and adds them to our ignoreWordsSet
	 * Uses bufferedReader to readLines of text and splits with regex "\\s"
	 * @param f - Text File containing common words to ignore
	 * @throws IOException
	 * @throws NullPointerException
	 * Complexity O(N) - Where N is the length of the file. Adding to set is constant operation.
	 */
	public void addToIgnoreWords(File f) throws IOException, NullPointerException {

		BufferedReader ignoreWordsReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line;
		while ((line = ignoreWordsReader.readLine()) != null) {
			String[] words = line.split("\\s");
			for (String word : words) {
				word = (word.replaceAll("[^a-zA-Z]", "").toLowerCase());
				ignoreWordsSet.add(word);
			}
		}
		ignoreWordsReader.close();
	}
	
	/**
	 * Method that checks if words (keys) are present in ignoreWordsSet and if not, add them to HashMap/Increment the appropriate value
	 * @param s - String containing user provided source. Either a URL or File (DataSource enum will indicate which)
	 * @param ds - DataSource enum indicating if String s is a File source or a URL source
	 * @throws IOException
	 * Complexity O(N) - Where N is length of Source File, Checking Set and Adding to HashMap are constant operations.
	 */
	public void addToHashMap(String s, DataSource ds) throws IOException {
		BufferedReader hashMapReader;
		if (ds == DataSource.FILE) {
			hashMapReader = new BufferedReader(new FileReader(new File(s)));
		} else {
			hashMapReader = new BufferedReader(new InputStreamReader(new URL(s).openStream()));
		}
		String line;
		while ((line = hashMapReader.readLine()) != null) {

			String[] words = line.split(" ");
			for (String word : words) {
				word = parseHTMLTags(word);
				word = word.trim().replaceAll("[^a-zA-Z]", "").toLowerCase();
				if (!ignoreWordsSet.contains((word)) && word.length() > 1) {
					frequencyTable.compute((word), (k, v) -> v == null ? 1 : v + 1);
				}
			}
		}
		hashMapReader.close();

	}

	/**
	 * Utility method to parse symbols from String
	 * @param word - String word parsed from file
	 * @return word - String word with html tags removed
	 * Complexity O(N) - Method likely has to iterate through each char of the word in order to replace individual chars 
	 */
	private String parseHTMLTags(String word) {
		String target = word.replaceAll("<[^>]*>", " ");
		return target;
	}

	/**
	 * Utility method to display IgnoreWords
	 * Complexity: O(N) - Where N is length of file.
	 */
	public void printIgnoreWords() {
		for (String word : ignoreWordsSet) {
			System.out.println(word);
		}
	}

	/**
	 * Returns the HashMap from Parser class
	 * @return frequencyTable - HashMap<String, Integer>
	 */
	public HashMap<String, Integer> getHashMap() {
		return this.frequencyTable;
	}

	/**
	 * Returns the HashSet of words to ignore from Parser Class
	 * @return ignoreWordsSet 
	 */
	public Set<String> getIgnoreWords() {
		return this.ignoreWordsSet;
	}

}
