

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * This is the class that represents the panel whereby the words will be falling. This is essentially a placeholder for all the WordRecord instances that will be falling.
 * This class will be used by the WordApp class to essentially represent the graphical user interface that occupies the majority of the screen. 
 * The class extends and inherits from the JPanel class and implements the runnable interface so that it can be used an independent thread.
 * The class holds the following important variables:
 * The volatile Boolean method done that stores the value if the game has been completed or not and the volatile keyword is used as an indicator to the Java compiler and Thread to not cache value of this variable and always read it from main memory.
 * An array of WordRecord instances that are the array of words (and their associated animations) that fall from the screen
 * An instance of the score class to hold the users score whilst playing the game
 * An array of the Thread class that hold the individual words falling as independent threads
 * An instance variable called message count that stores the amount of times a success or fail message has been displayed to the user
 * This class has an inner class called wordFalling that serves as individual threads of singular words dropping from the top of the screen. More on this can be found below
 * @author ShaiAarons
 * @author James Gain
 */
public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private Score score;
		private Thread[] threads;
		public static int messageCount;


		/**
		 * This is the method that sets up the graphical user interface of the words falling from the top of the panel
		 */
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words	
		    }
		   
		  }
		
		/**
		 * This is the parameterized constructor for the panel
		 * @param words The words that will fall from the screen
		 * @param maxY The farthest point that words can fall to before being missed by the user
		 * @param score The current score of the user
		 */
		WordPanel(WordRecord[] words, int maxY, Score score) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
			this.score = score;
			messageCount = 0;
		}
		
		
		/**
		 * This is the method that will end all of the threads when a user wishes to end the game
		 * This passes through all the individual threads and uses the join() functionality to ensure all the threads have run enough computation in order to end the game
		 */
		public void endAllThreads() {
			for(int i=0;i<threads.length;i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * This is a synchronized method that will display a message to the user that shows if the user has won or lost the game as well as the users score for that iteration of the game
		 * This message will only be displayed if the messageCount instance variable is equal to zero. The messageCount instance variable will be incremented every time the method is called
		 * hence why the method is synchronized due to multiple threads potentially accessing this method at the same time
		 * @param won
		 */
		public synchronized void displayMessage(boolean won) {
			if(messageCount==0) {
				if(won) {
			JOptionPane.showMessageDialog(null, "Congradulations! You Win.\nYou scored: "+score.getScore()+"\nClick 'End' to begin new game\nClick 'Quit' to exit");}
				else {
					JOptionPane.showMessageDialog(null, "You Lost! Better luck next time. \nYou scored: "+score.getScore()+"\nClick 'End' to begin new game\nClick 'Quit' to exit");
				}
				WordApp.endB.setEnabled(true);
				
			}
			messageCount++;
		}
		
		
		/**
		 * This is the run method for the panel of the GUI. 
		 * This method creates and starts all the threads of wordFalling threads using the threads array and the start() method.
		 */
		public void run() {
			threads = new Thread[noWords];
			for(int i= 0;i<noWords;i++) {
				wordFalling wordThread = new wordFalling(words[i]);
				Thread thread = new Thread(wordThread);
				threads[i] = thread;
			}
			
			for(int i = 0;i<threads.length;i++) {
				threads[i].start();
			}
			
		}
		

		 
		
	/**
	 * This is the inner class that represents a singular word falling within the panel on the graphical user interface. 
	 * This implements the runnable interface so that it can be implemented as an individual thread in this multithreaded game.
	 * Many instances of this class will be created by the WordPanel class so that there can be multiple independent words falling from the top of the screen, all enclosed within their own thread. 
	 * The class holds a WordRecord instance so that it can manage the word falling from the screen.
	 * @author ShaiAarons
	 *
	 */
	public class wordFalling implements Runnable{
			WordRecord word;
			
			public wordFalling(WordRecord word) {
				this.word = word;
			}
			
			
			
			
			/**
			 * This is the run() method that will be activated once the panel starts this as an individual thread. 
			 * This thread has a while loop that will essentially run until the game is over. Within the while loop, the following process occurs:
			 * 1) If the word has dropped the missed word counter is incremented and the associated messages are displayed on the WordApp. The word is then reset using the .resetWord() method
			 * 2) The word will temporarily sleep using the .sleep() method
			 * 3) The word will then drop by a certain amount - using the wordRecord.drop() functionality
			 * 4) If the game is over a message will displayed
			 */
			@Override
			public void run() {
				while(done!=true) {
					if(word.dropped()) {
					score.missedWord();
					WordApp.missed.setText("Missed: "+ score.getMissed()+ "    ");
					word.resetWord();
					}
					
						try {
							Thread.sleep(word.getSpeed());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					
					word.drop(10);
					repaint();
				
					if(score.getTotal()>=WordApp.totalWords) {
						done=true;
						WordApp.endB.setEnabled(false);
						if(score.getCaught()>score.getMissed()) {
							displayMessage(true);
						}else {
							displayMessage(false);
						}
						

					}
			
				
				}
				
			}
			
		}
	
	

		
		
		
	}


