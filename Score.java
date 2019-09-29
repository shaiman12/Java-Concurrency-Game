
/**
 * This class represents the score of the user whilst playing the game. This class stores an integer called missedWords which
 * represents the amount of words that the user did not type in quick enough time before the word reached the bottom of the screen. 
 * The class also stores an integer value called caughtWords which represents the amount of words that the user did indeed type in time before reaching the bottom of the screen
 * The final instance variable that the class holds is an integer called gameScore which represents the score of the user - this is incremented by the length of any word caught in time by the user.
 * Within the class there are accessor and incrementor methods for each variable - these are all synchronized methods. The reason for this is so that the blocks of code within the methods 
 * happen as an atomic unit in order for effecient concurrency and so that bad interleaving can never occur when multiple threads are trying to change the same shared variables at the same time
 * @author ShaiAarons
 * @author James Gain
 */
public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;
	
	/**
	 * Default constructor that sets the instance variables all to 0
	 */
	Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
		
	
	/**
	 * Accessor method for the amount of missed words
	 * @return Amount of missed words
	 */
	public synchronized  int getMissed() {
		return missedWords;
	}

	/**
	 * Accessor method for the amount of words caught by the user
	 * @return amount of words caught by the user
	 */
	 public synchronized int getCaught() {
		return caughtWords;
	}
	
	 /**
	  * Method that returns the total amount of words that have either been caught or missed 
	  * @return Total amount of words that have either been caught or missed 
	  */
	public synchronized int getTotal() {
		return (missedWords+caughtWords);
	}

	/**
	 * Accessor method for the score acheived by the user
	 * @return the score acheived by the user
	 */
	public synchronized int getScore() {
		return gameScore;
	}
	
	/**
	 * Synchronized Method to increment the missed word counter by one when the user does not type a word in time
	 */
	public synchronized void missedWord() {
		missedWords++;
	}

	/**
	 * Synchronized Method to increment the caught word counter by one when the user types a word in time. 
	 * It also increments the score by the length of the word input by the user.
	 * @param length - the length of the word typed in
	 */
	public synchronized void caughtWord(int length) {
		caughtWords++;
		gameScore+=length;
	}
/**
 * Synchronized method to set all the instance variables to 0 when the game is over or restarted
 */
	public synchronized void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
