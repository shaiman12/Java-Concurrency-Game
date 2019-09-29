
/**
 * This class merely functions as essentially a dictionary of all the words that will be displayed and fall on the screen
 * Here an array stores all the words that will be displayed. In the case that the user does not input a text file name, there is a default set of words.
 * The class has two instance variables: A dictionary array that stores a set of words (in the form of a string) as well as an integer value representing the size of the array.
 * @author ShaiAarons
 * @author James Gain notes
 */
public class WordDictionary {

	int size;
	static String [] theDict= {"litchi","banana","apple","mango","pear","orange","strawberry",
		"cherry","lemon","apricot","peach","guava","grape","kiwi","quince","plum","prune",
		"cranberry","blueberry","rhubarb","fruit","grapefruit","kumquat","tomato","berry",
		"boysenberry","loquat","avocado"}; //default dictionary
	/**
	 * This is the parameterized constructor that receives a set of words and stores them in the dictionary. 
	 * It also sets the size to the amount of words 
	 * @param tmp the array of words represented as string values
	 */
	WordDictionary(String [] tmp) {
		size = tmp.length;
		theDict = new String[size];
		for (int i=0;i<size;i++) {
			theDict[i] = tmp[i];
		}
		
	}
	
	/**
	 * The default constructor which sets the size of the class equal to the length of the default dictionary
	 */
	WordDictionary() {
		size=theDict.length;
		
	}
	
	/**
	 * This is an important method that returns a random word within the dictionary to be displayed on the screen. 
	 * This uses the Math.Random function, which provides sufficient enough randomization of the words.
	 * @return A random word from the array of strings that represent words
	 */
	public synchronized String getNewWord() {
		int wdPos= (int)(Math.random() * size);
		return theDict[wdPos];
	}
	
}
