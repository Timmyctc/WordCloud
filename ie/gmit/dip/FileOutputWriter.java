package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * 
 * @author Conor Timlin
 * FileOutputWriter class handles method allowing user to append to system default <b>ignorewords.txt</b> file from the console
 * and writing the text results of wordcloud to the local directory.
 *
 */

public class FileOutputWriter {
	
	/**
	 * Allows user to append words to <b>ignorewords.txt</b> file from console.
	 * 
	 * Complexity: O(1) - Standard user input, writing to file is constant complexity
	 * 
	 */
	public static void appendToFile() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("./ignorewords.txt", true)));
			System.out.println(ConsoleColour.BLUE);
			System.out.println("Entering new ignorewords to ignorewords.txt in src folder. TYPE 'exit' to return to menu!");
			System.out.println(ConsoleColour.RESET);

			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String userInput = "";
			while (!userInput.equals("exit")) {
				System.out.println(">");
				userInput = input.readLine();
				if (userInput.equals("exit")) {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Returning to menu...");
					System.out.println(ConsoleColour.RESET);
					break;
				}
				out.println(userInput);
				System.out.println(ConsoleColour.BLUE);
				System.out.println("Successfully Added " + userInput + " to ignorewords file");
				System.out.println(ConsoleColour.RESET);
			}
		} catch (IOException e) {
			System.out.println(ConsoleColour.RED);
			System.out.println("[ERROR] Input error");
			System.err.println(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 
	 * @param results ArrayList of Entry<String,Integer> containing the max words the user wants to display in their word cloud
	 * Complexity: O(N) where N is results.size() (I.E. MAX_WORDS_TO_DISPLAY)
	 * @throws IOException
	 */
	public static void writeResults(ArrayList<Entry<String, Integer>> results) throws IOException {
		BufferedWriter outWriter = (new BufferedWriter(new FileWriter("./wordcloudresults.txt")));

		for (int i = 0; i < results.size(); i++) {
			outWriter.write("[" + results.get(i).getKey() + " : " + results.get(i).getValue().toString() + "]");
			outWriter.append("\n");
		}
		outWriter.append(LocalDateTime.now().toString());
		outWriter.close();
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Saved Results of your Word Cloud locally!");
		System.out.println(ConsoleColour.RESET);
	}

}
