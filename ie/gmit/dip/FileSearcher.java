package ie.gmit.dip;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Conor Timlin Utility class that provides a lot of error detection,
 *         input validation and file retrieval
 * 
 */

public class FileSearcher {

	// extension default is txt

	/**
	 * @param extension - default file extension to append to txt files.
	 * @param sc        - Scanner to parse for user input
	 */
	private final static String extension = ".txt";
	private final static Scanner sc = new Scanner(System.in);

	// Public Method facilitating user input of File and Directory(Checks if they
	// exist, returns it if true)

	/**
	 * 
	 * @return Full path to user file. Provides decent degree of error detection and
	 *         input validation
	 * @throws Exception Potential Error: Line 55 in cases where users enter file
	 *                   with backslashes? Complexity: O(1) - Simple print
	 *                   statements and user input.
	 */
	public static String enterFile() throws Exception {

		// Prompts and other bits
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter File To Analyse Name: ");
		System.out.println(ConsoleColour.RESET);

		String fileName = sc.nextLine().trim() + extension; // Get the file name, append the extension.
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter Directory Path to Search");
		System.out.println(ConsoleColour.RESET);

		String directory = sc.nextLine().trim(); // Get directory path

		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter Output image filename: ");
		System.out.println(ConsoleColour.RESET);

		String userFileName = sc.nextLine().trim() + ".png";
		WordCloud.setOutputFileName(userFileName);

		String path = directory + "//" + fileName;
		if (path.endsWith(".txt.txt")) {
			path = path.substring(0, path.length() - 4);
		}

		File target = new File(path);
		if (target.exists()) {
			System.out.println(ConsoleColour.RED);
			System.out.println("Retrieving File...");
			System.out.println(ConsoleColour.RESET);
			return target.getAbsolutePath();
		} else {
			System.out.println(ConsoleColour.RED);
			System.out.println("No Such File Found!"); // If file or Directory is incorrect.
			System.out.println(ConsoleColour.RESET);
			return null;
		}

	}// End enterFile

	/**
	 * Parses user input for URL to analyse for word cloud. Provides decent error detection. 
	 * In case URL can not be found a null URL is returned and handled
	 * @return Returns URL from user input
	 * @throws MalformedURLException
	 * Complexity O(1) - Simple print statments and user input
	 */
	public static URL enterFileURL() throws MalformedURLException {
		URL url = null;
		boolean loop = true;
		
		

		do {
			System.out.println(ConsoleColour.BLUE);
			System.out.println("Enter URL, or -1 to exit to menu");
			System.out.println(ConsoleColour.RESET);
			try {

				String urlPath = sc.nextLine().trim();
				if (urlPath.equals("-1")) {
					break;
				}
				url = new URL(urlPath);
				loop = false;

			} catch (MalformedURLException e) {
				System.out.println(ConsoleColour.RED);
				System.out.println("[ERROR] Check your URL and try again");
				System.out.println(ConsoleColour.RESET);

			}
		} while (loop);
		if (loop) {
			System.out.println(ConsoleColour.RED);
			System.out.println("Returning to menu");
			return url = null;
		}
		System.out.println(ConsoleColour.RED);
		System.out.println("URL found, loading");
		System.out.println(ConsoleColour.RESET);
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter Output image filename: ");
		System.out.println(ConsoleColour.RESET);

		String userFileName = sc.nextLine().trim() + ".png";
		WordCloud.setOutputFileName(userFileName);
		return url;
	}
}
