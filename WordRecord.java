
/**
 * This is the class that represents a falling word, as well as its associated animations, within the panel of the program. This stores a string value that represent the actual word that is falling. 
 * It also stores both the x and the y positions of the word relative to where it is displayed on the screen. There is also an instance variable integer
 * that represents the furthest point the word can appear within the screen, any point beyond there the word can be considered as missed. There is also a boolean variable
 * called dropped which holds the value 'true' if the word has begun to fall on the screen. There is also an integer called fallingSpeed which holds the speed at which a word is falling.
 * The final variables are the variables that store the minimum and maximum amount of time before a word begins to drop from the top of the screen. 
 * Within the class there are accessor and mutator methods for each variable - these are all synchronized methods. The reason for this is so that the blocks of code within the methods happen as an atomic unit in order for efficient concurrency to occur and so that bad interleaving can never occur when multiple threads are trying to change the same shared variables at the same time.
 * @author ShaiAarons
 * @author James Gain
 */
public class WordRecord {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;
	

	/**
	 * Default constructor that sets all the values to their defaults or zeros 
	 */
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	/**
	 * Parameterized constructor that receives a word.
	 * @param text The word that will drop
	 */
	WordRecord(String text) {
		this();
		this.text=text;
	}
	/**
	 * Second parameterized constructor that sets values of the class according to the parameters 
	 * @param text The word that will drop
	 * @param x The X position of the word
	 * @param maxY The Y position of the word
	 */
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}
	
/**
 * Synchronized mutator method that sets the Y variable to an input value
 * @param y Y position
 */
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
		this.y=y;
	}
	
	/**
	 * Synchronized mutator method that sets the X variable to an input value
	 * @param x X position
	 */
	public synchronized  void setX(int x) {
		this.x=x;
	}
	
	/**
	 * Synchronized mutator method that sets the word variable to an input value
	 * @param text The word to fall from the screen
	 */
	public synchronized  void setWord(String text) {
		this.text=text;
	}

	/**
	 * Synchronized method for accessing the word that is falling 
	 * @return The word 
	 */
	public synchronized  String getWord() {
		return text;
	}
	
	/**
	 * Synchronized accessor method for returning the X position of the falling word
	 * @return X position
	 */
	public synchronized  int getX() {
		return x;
	}	
	
	/**
	 * Synchronized accessor method for returning the X position of the falling word
	 * @return Y position
	 */
	public synchronized  int getY() {
		return y;
	}
	
	/**
	 * Synchronized accessor method for returning the speed of the word that is falling
	 * @return the speed of the word falling
	 */
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	/**
	 * This is a synchronzied method that sets the position of the word to a specified Y and X position values
	 * @param x X position
	 * @param y Y Values
	 */
	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	
	/**
	 * Synchronized method that sets the Y position back to zero
	 */
	public synchronized void resetPos() {
		setY(0);
	}

	/**
	 * This is a synchronized and locked method occurs when a word has been either caught by a user or missed by the user.
	 * The position of the word is set back to the top of the screen. The word is then randomly reselected from the WordDictionary class.
	 * The method also sets the falling speed to a random value. 
	 * There is effective concurrency here as it is critical that this block of code happens as a singular unit, without other outside sources changing the data at the same time
	 */
	public synchronized void resetWord() {
		resetPos();
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());

	}
	
	
	/**
	 * This is a synchronized/locked method that checks if the word the user has typed is the same as the word that is stored in this class
	 * @param typedText The text that the user typed in
	 * @return True if the word typed in matches. False if it does not
	 */
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	
	/**
	 * This is a synchronized method that allows for the animation of the word dropping. 
	 * It receives an amount to increment the words Y position by 
	 * @param inc The amount to increment the words Y position by 
	 */
	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
	
	/**
	 * Synchronized accessor method that returns if the word has dropped or not
	 * @return True if the word is falling/False if the word has not started to fall
	 */
	public synchronized  boolean dropped() {
		return dropped;
	}

}
