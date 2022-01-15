package ie.gmit.dip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * 
 * @author Conor Timlin
 * @version 1.0
 * 
 *          <i>"Where the Magic Happens.."</i>
 * 
 *          Word Cloud Class extends JPanel to access some interesting drawing
 *          features. Class Draws a WordCloud both to the users screen and
 *          outputs the image to the local src directory. Very Basic Collision
 *          detection is included however at larger font sizes/larger word
 *          counts this can cause OverFlow error. Some more time here would have
 *          yielded a cleaner WordCloud but the result isn't too bad.
 *
 */

public class WordCloud extends JPanel {

	// ===============================================================================
	// Fields
	// ========================================================================================

	/**
	 * @param outputImage          - Image that is written to the current directory
	 *                             after wordcloud is generated.
	 * @param graphics             - Graphics2D object that draws to the
	 *                             BufferedImage outputImage.
	 * @param wordBoxes            - List of 2D Rectangles to handle collision with
	 *                             Strings.
	 * @param RANDOM               - random object to help with choosing random
	 *                             values, colours etc.
	 * @param MAX_WORDS_TO_DISPLAY - incorrectly named as if it were a final, value
	 *                             set by user to display desired number of words in
	 *                             the word cloud, default value is 10.
	 * @param AR                   - ArrayList of EntrySets from TreeSet, contains
	 *                             the number of elements user wishes displayed on
	 *                             word cloud.
	 * @param screenSize           - User's screensize. Hopefully will display the
	 *                             wordcloud on the full screen of the user
	 * @param outputFileName       - Name of output image default value, will be set
	 *                             by user
	 */
	private static BufferedImage outputImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_4BYTE_ABGR); // Output
																												// Image
	private static Graphics2D graphics = outputImage.createGraphics(); // Graphics to write to image
	private static List<Rectangle2D> wordBoxes = new ArrayList<Rectangle2D>(); // List of Rectangle Hitboxes
	private static final Random RANDOM = new Random(); // Random Obj
	private static int MAX_WORDS_TO_DISPLAY = 10; // Max words to display in cloud
	private static ArrayList<Entry<String, Integer>> AR; // ArrayList of Entries of Words and Values to display to Word
															// Cloud
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ScreenSize
	private static String outputFileName = "output.png";

	// ================================================================================
	// Behaviours
	// =======================================================================================

	/**
	 * Method to set everything in motion. Calls for the JFrame to initialse,
	 * initialises AR and writes the text based results to local directory.
	 * Complexity: O(1) - Method invocation
	 * 
	 * @param ts - TreeSet of EntrySets from the text analyis
	 * @throws IOException
	 */
	public static void createWordCloud(TreeSet<Entry<String, Integer>> ts) throws IOException {
		initialiseFrame();
		AR = getFirstK(ts, MAX_WORDS_TO_DISPLAY);
		FileOutputWriter.writeResults(AR);
	}

	/**
	 * Method invoked by Swing to draw components. Core method to drawing the word
	 * cloud. For each word to display a randomFont is selected from utility method
	 * pickRandomFontFamily, Font Size is selected with a very, very rudimentary
	 * method getFontSize().
	 *
	 * Font Metrics are used to get the collision box of the String to be drawn and
	 * the rectangle and location are generated in the generateRectangle method.
	 * Finally the String is drawn to the JFrame and to the BufferedImage
	 * Complexity: Arguably O(N) or O(N!) The paint method runs N number of times
	 * where N is MAX_WORDS_TO_DISPLAY. Technically this method Runs O(N) and the
	 * recursive generateRectangle method runs in a worst case of O(N!)
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < MAX_WORDS_TO_DISPLAY; i++) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Font fontToUse = new Font(pickRandomFontFamily(), Font.BOLD + Font.ITALIC, getFontSize(i));
			g2.setFont(fontToUse);
			g2.setPaint(pickRandomColour());

			// ===========FONTMETRICS================//
			FontMetrics FM = g2.getFontMetrics(fontToUse);
			Rectangle2D stringRect = generateRectangle(FM, i, g2);

			// ======================Draw the String, Draw the bounding box around it
			g2.drawString(AR.get(i).getKey(), (float) stringRect.getX(), (float) stringRect.getY() + FM.getAscent());
			g2.draw(stringRect);

			graphics.setFont(fontToUse);
			graphics.setColor(pickRandomColour());
			graphics.drawString(AR.get(i).getKey(), (float) stringRect.getX(),
					(float) stringRect.getY() + FM.getAscent());

			// =====================Add bounding boxes around strings
			wordBoxes.add(stringRect);
		}

		graphics.dispose();
		try {
			ImageIO.write(outputImage, "png", new File(outputFileName));
		} catch (Exception e) {
			System.out.println(ConsoleColour.RED);
			System.out.println("[ERROR] Output Image error");
			System.out.println(ConsoleColour.RESET);
		}

	}

	/**
	 * Iterates through the wordBoxes list and checks an object against existing
	 * objects to detect any potential collisions
	 * 
	 * @param r - Current Rectangle object being drawn to screen
	 * @return boolean - True if the current Rectangle intersects with any of the
	 *         existing Rectangles Complexity: O(N) - Where N is the number of
	 *         Rectangles in the wordBoxes list.
	 */
	private boolean doesIntersect(Rectangle2D r) {
		for (Rectangle2D rects : wordBoxes) {
			if (r.intersects(rects)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility method to set the User selected fileoutputname
	 * 
	 * @param userFileName - User File name Complexity: O(1);
	 */
	public static void setOutputFileName(String userFileName) {
		outputFileName = userFileName;
	}

	/**
	 * First iteration of collision detection in the word cloud. Greedy and
	 * inefficient but prevents collisions with relatively few issues. An exit
	 * clause is needed to be added to prevent stack overflow when no valid
	 * locations for string placement exist If this occurs reduce the number of
	 * words in the word cloud.
	 * 
	 * @param FM - Font Metrics object, used to draw bounding box around the text.
	 * @param i  - counter for current position in the ArrayList AR.
	 * @param g2 - Graphics object
	 * @return Rectangle in a valid location Complexity: O(N!) Worst case scenario
	 *         this method will never stop.
	 */
	private Rectangle2D generateRectangle(FontMetrics FM, int i, Graphics g2) {
		Rectangle2D stringRect = FM.getStringBounds(AR.get(i).getKey(), g2);
		int x = randomLocationX();
		int y = randomLocationY() - (FM.getAscent());

		if (x > screenSize.width) {
			x = screenSize.width - FM.stringWidth(AR.get(i).getKey());
		}
		if (y > screenSize.height) {
			y = screenSize.height - FM.getAscent();
		}

		stringRect.setRect((x), (y), FM.stringWidth(AR.get(i).getKey()), FM.getHeight());
		if (doesIntersect(stringRect)) {
			stringRect = generateRectangle(FM, i, g2);
		}
		return stringRect;
	}

	/**
	 * 
	 * @return x - xcoordinate for drawString Complexity O(1) - According to Docs it
	 *         generates a random number <2 times on average and if both values are
	 *         outside the range it modulo operands on the value.
	 */
	public int randomLocationX() {
		int x = (int) (RANDOM.nextInt((int) screenSize.getWidth()));
		return x;
	}

	/**
	 * 
	 * @return y - ycoordinate for drawString Complexity O(1) - According to Docs it
	 *         generates a random number <2 times on average and if both values are
	 *         outside the range it modulo operands on the value.
	 */
	public int randomLocationY() {
		int y = (int) (RANDOM.nextInt((int) screenSize.getHeight()));
		return y;
	}

	/**
	 * Very rudimentary fontsize algorithm. loosely ties size to occurances. Would
	 * like to do more here.
	 * 
	 * @param i - Current position in AR arraylist
	 * @return Font Size to use in current String Complexity O(1)
	 */
	private int getFontSize(int i) {
		int fontSize = (int) (AR.get(i).getValue() * 2.5);
		if (fontSize > 100) {
			fontSize = 100;
		}
		return fontSize = 40;
	}

	/**
	 * Initialises Jframe for WordCloud Complexity: O(N) - Where N is the size of
	 * the users resolution, i.e. the frame width and height.
	 */
	private static void initialiseFrame() {
		JFrame frame = new JFrame();
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setTitle("Word Cloud");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.add(new WordCloud());
		frame.setResizable(false);
		frame.setSize(screenSize.width, screenSize.height);
		frame.setVisible(true);
		frame.setBackground(Color.black);

	}

	// =======================Utility Method to Iterate k elements from TreeSet to
	// ArrayList for use in word cloud===================//
	// Complexity: O(n) n=MAX_WORDS_TO_DISPLAY, Iterator Complexity is O(n log n) so
	// O(n) overall

	/**
	 * Utility Method to Iterate k elements from TreeSet to ArrayList for use in
	 * word cloud
	 * 
	 * @param set TreeSet of entryset from HashMap
	 * @param k   Number of elements to extract i.e. MAX_WORDS_TO_DISPLAY
	 * @return ArrayList of k elements, i.e. the Words to display to wordcloud and
	 *         their respective values. Complexity: O(N) Where N=
	 *         MAX_WORDS_TO_DISPLAY Iterator Complexity is (N log N) O(N) overall.
	 */
	public static ArrayList<Entry<String, Integer>> getFirstK(TreeSet<Entry<String, Integer>> set, int k) {
		Iterator<Entry<String, Integer>> iterator = set.iterator();
		ArrayList<Entry<String, Integer>> result = new ArrayList<Entry<String, Integer>>(k); // to store first K items
		for (int i = 0; i < MAX_WORDS_TO_DISPLAY; i++)
			result.add(iterator.next());
		return result;
	}

	/**
	 * Utility method to generate random Font for selection Source:
	 * https://stackoverflow.com/a/64491016/15663468 Complexity: 0(1) - Assigning
	 * String array to another String Array and returning Rand.next should be
	 * constant Unsure if returning the availableFontFamilyNames counts as a linear
	 * operation given the available names will vary, n=available names)
	 * 
	 * @return availableFonts - Random available Font
	 */
	private static String pickRandomFontFamily() {
		String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames(Locale.ENGLISH);
		return availableFonts[RANDOM.nextInt(availableFonts.length)];
	}

	/**
	 * Utility Method To Pick Random Color Source:
	 * https://stackoverflow.com/questions/4246351/creating-random-colour-in-java
	 * Complexity: O(1) - Random.next complexity is O(1) according to docs
	 * 
	 * @return Random Colour to use in Drawing String
	 */
	private Color pickRandomColour() {
		float r = RANDOM.nextFloat() / 2f + 0.5f;
		float g = RANDOM.nextFloat() / 2f + 0.5f;
		float b = RANDOM.nextFloat() / 2f + 0.5f;
		return new Color(r, g, b);
	}

	/**
	 * Static Method to Set Words to Display from Menu Complexity: 0(1)
	 * 
	 * @param wordsToDisplay
	 */
	public static void setWordsToDisplay(int wordsToDisplay) {
		MAX_WORDS_TO_DISPLAY = wordsToDisplay;
	}
}
