package ie.gmit.dip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * 
 * @author Conor Timlin
 * @version 1.0
 * @since 1.8
 *
 *        The Menu class handles control flow of console driven application.
 *        Contains UI oriented methods <b>displayMenu()<b>,
 *        <b>displayOptions()</b> and <b>displayHeader()</b>. Utility method
 *        <b>getInput</b> takes user input to direct flow of control. Utility
 *        method <b>customIgnoreWords(Parser p)</b> takes input from user to
 *        allow them provide a custom file of <i>ignoreword.txt</i>. If user
 *        opts to use system default <i>ignorewords.txt</i> file, or inputs an
 *        invalid entry, the system default will be used.
 * 
 */

public class Menu {

	/**
	 * Displays Menu options to user Directs flow of control of application based on
	 * user selection.
	 * Complexity: O(1) - Simple Printing Statements
	 * 
	 * @throws Exception
	 */

	static void displayMenu() throws Exception {

		displayHeader();

		// Menu Loop Boolean
		boolean menuLoop = true;
		FileParser fileParser = new FileParser();
		URLParser urlParser = new URLParser();
		do {
			displayOptions();
			System.out.println(ConsoleColour.RESET);
			int choice = getInput();

			switch (choice) {

			case 1: // Enter File You'd like to analyze
				try {
					customIgnoreWords(fileParser);
					File textToAnalyse = (new File(FileSearcher.enterFile())); // Attempt to get File info from user
					fileParser.parse(textToAnalyse.toString());
					WordCloud.createWordCloud(fileParser.sortByWordFrequency(fileParser.getHashMap()));

				} catch (Exception e) {
					System.out.print(ConsoleColour.RED);
					System.out.print("[Error] Retrieving file");
					System.out.println(ConsoleColour.RESET);
				}
				break;
			case 2:
				customIgnoreWords(urlParser);
				System.out.println(ConsoleColour.BLUE);
				URL urlToAnalyse = (FileSearcher.enterFileURL());
				if (urlToAnalyse != null) {
					urlParser.parse(urlToAnalyse.toString());
					WordCloud.createWordCloud(urlParser.sortByWordFrequency(urlParser.getHashMap()));
				} else
					break;
				System.out.println(ConsoleColour.RESET);
				break;
			case 3:

				// Loop boolean to keep prompting user for How many words to use (Must be
				// greater than 0) (-1 exits the loop and returns to main menu)
				boolean wordLoop = true;
				int wordsToDisplay;
				do {

					System.out.println(ConsoleColour.BLUE);
					System.out.println("How Many Words to Display");
					System.out.println(ConsoleColour.RESET);
					wordsToDisplay = getInput();
					try {
						if (wordsToDisplay == (-1)) { // User can enter -1 to return to main menu
							System.out.println(ConsoleColour.RED);
							System.out.println("Going back to main Menu");
							System.out.println(ConsoleColour.RESET);
							break;
						} else if (wordsToDisplay < 100 && wordsToDisplay > 0) {
							wordLoop = false;
							break;
						} else {
							System.out.println(ConsoleColour.RED);
							System.out.println("[Error] Not a valid entry, please enter number between 1-100");
							wordLoop = true;
						}

					} catch (Exception e) {
						System.out.println(ConsoleColour.RED);
						System.out.println("[ERROR] Not a valid entry, Try Again");
						System.out.println(ConsoleColour.RESET);
						wordLoop = true;
					}
				} while (wordLoop);
				if (!wordLoop) {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Confirmed: will display " + wordsToDisplay + " words."); // when the user
																									// changes words to
																									// display
					WordCloud.setWordsToDisplay(wordsToDisplay);
					System.out.println(ConsoleColour.RESET);
					System.out.println();
				}
				break;

			case 4:
				System.out.println(ConsoleColour.RED);
				System.out.println("GOING TO EXPERIMENTAL FILE WRITE MODE"); // EXP.
				System.out.println(ConsoleColour.RESET);
				FileOutputWriter.appendToFile();
				break;
			case 5:
				System.out.println(ConsoleColour.RED);
				System.out.println("Quitting"); // V straightforward, Break from loop to quit
				System.out.println(ConsoleColour.RESET);
				menuLoop = false;
				break;

			default:
				System.out.println(ConsoleColour.RED);
				System.out.println("Not a valid input, Select a Valid Input"); // If users input a nonvalid value,prompt
																				// them for a valid one
				System.out.println(ConsoleColour.RESET);
			}
		} while (menuLoop);
	}// End Display Menu

	/**
	 * Allows use of user defined custom ignorewords.txt file. Any invalid path
	 * provided results in use of system default ignorewords.txt file.
	 * Complexity: O(1) - Printing and accepting user input
	 * 
	 * @param p - Parser object (URLParser or FileParser)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void customIgnoreWords(Parser p) throws FileNotFoundException, IOException {
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Do you have a custom ignorewords file or would you like to use the system default?");
		System.out.println(
				"Enter the full directory of your custom file (Name must match 'ignorewords.txt'), or simply enter N to proceed with default");
		System.out.println(ConsoleColour.RESET);
		Scanner ignoreScanner = new Scanner(System.in);
		String customIgnoreWords = ignoreScanner.nextLine();

		if (new File(customIgnoreWords).exists()) {
			p.initialiseIgnoreWords(new File(customIgnoreWords));
		} else {
			p.initialiseIgnoreWords();
		}
	}

	/**
	 * UI Method to display Header each time the program is initially started.
	 * Complexity O(1) - Simple Printing Statment
	 */
	private static void displayHeader() {
		System.out.println(ConsoleColour.WHITE_BOLD);
		System.out.println(ConsoleColour.BLUE_BACKGROUND);
		System.out.println("***************************************************");
		System.out.println("* GMIT - Dept. Computer Science & Applied Physics *");
		System.out.println("*                                                 *");
		System.out.println("*           Image Filtering System V0.1           *");
		System.out.println("*     H.Dip in Science (Software Development)     *");
		System.out.println("*                                                 *");
		System.out.println("***************************************************");
		System.out.print(ConsoleColour.RESET);
	}// End Header

	/**
	 * UI method to display options on each iteration of the menu. 
	 * Complexity: O(1) - Simple Printing Statements
	 */
	private static void displayOptions() {
		System.out.println();
		System.out.println(ConsoleColour.RED);
		System.out.println(ConsoleColour.BLUE_BACKGROUND_BRIGHT);
		System.out.println("1) Select Local Text File to Analyze"); // Ask user to specify the file to process.
		System.out.println("2) Select URL to Analyze"); // Ask user to specify URL to Process
		System.out.println("3) Number of Words To Display"); // Ask user to select Number of Words in word cloud
		System.out.println("4) Append Words to Local IgnoreWords file");
		System.out.println("5) Quit"); // Terminate program
		System.out.println("\nSelect Option [1-5]>");
		System.out.println(ConsoleColour.RESET);
	}// end displayOptions

	/**
	 * Scans for user input from menu options. 
	 * Complexity: O(1) - Simple user input and return
	 * 
	 * @return int corresponding to user choice from menu.
	 */
	private static int getInput() {
		Scanner scanForInput = new Scanner(System.in);
		int choice = -1;
		try {
			choice = Integer.parseInt(scanForInput.nextLine());
		} catch (Exception e) {
			// ERROR
		}
		return choice;
	}// End GetInput Method
}
