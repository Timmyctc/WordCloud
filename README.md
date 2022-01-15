# WordCloud
Console driven application that takes a txt file as input and produces a Word cloud as output

Program lifecycle is maintained by the console driven menu. Allows user 5 options 
1) Parse Text File

Prompts user to provide a custom ignorewords.txt file, if the user provides 
an invalid path (Or inputs N) the program will use the locally available ignorewords.txt file instead.
The File Parser class delegates much of its behaviour to the Parent Parser class. Theres a deep level of abstraction and the intention 
was to flesh out the concrete implementation differences in the implementing class File Parser but due to time constraints this wasnt possible. 
User then is able to provide custom filename for the output file of the word cloud generated from their input file.


2) Parse URL Link

Prompts user to provide a custom ignorewords.txt file, if the user provides 
an invalid path (Or inputs N) the program will use the locally available ignorewords.txt file instead.
Prompts user to provide a Static Url. Like File Parser much of the implementaton is delegated to the calls to the super methods.
User provides custom filename for the output of the word cloud. 
Again a generally high level of input validation and error detection means malformed url exceptions are caught and handled.

3) Number of words to analyse

By default set to 10, allows the user to input a value between 1 and 100 words to display in the word cloud.

4) Append words to ignorewords file

This allows the user to append words to the ignorewords file directly from the console. Typing exit allows the user to return to the main flow of control in the main menu. 
Combining this with the text results provided by the program help you parse out common terms/tags/words that may clog up the word cloud

5) Quit
Exits the program.

The WordCloud class has included a rudimentary collision detection check (as a result the font size has been reduced) 
The results of each word cloud are included as a text file along with a timestamp. 
This can be useful in parsing out any words that should be added to the ignore words file via the append function in the console menu.
Along with providing an output image, a JFrame display will display the wordcloud to the user. 

