package ie.gmit.dip;

import java.util.Comparator;
import java.util.Map.Entry;

/**
 * @author Conor Timlin
 * Comparator class that compares EntrySet of <b><String, Integer></b>
 *
 */
public class EntryComparator implements Comparator<Entry<String, Integer>> {

	
	/**
	 * Compares Integer values of two EntrySet objects. In case objects are equal they are sorted based on String key.
	 * Complexity: O(1) - Comparing two values. 
	 */
	@Override
	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		if ((o1.getValue()) == (o2.getValue()))
			return o1.getKey().compareTo(o2.getKey());
		else if ((o1.getValue()) > (o2.getValue()))
			return -1;
		else
			return 1;
	}

}
