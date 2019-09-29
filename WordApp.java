

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;

/**
 * This class is the class that essentially functions as the main class or driver of the program. This class sets up the graphical user interface and implements the associated classes and the WordPanel class.
 * This class will allow the user to start the game by clicking a button that will start the WordPanel thread, as well as its individual wordFalling threads.
 * The user can end the game by clicking a button that will end all the panel thread as well as all the individual threads of words falling.
 * Every time the user types a word in a text box and presses the enter key, the program will spawn a new WordCatcher thread that will evaluate if the user successfully typed a word or not
 * The user can also exit the program by pressing the 'Quit' Button. 
 * This class has an inner class called WordCatcher that serves as individual threads that will evaluate if the user typed a word correctly or not. More on this can be found below
 * @author ShaiAarons
 *
 */
public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();

	static WordPanel w;
	
	public static JLabel missed;
	public static JButton endB;
	public static JButton startB;
	public static JLabel caught;
	public static JLabel scr;
	static Thread thread;
	
	
	/**
	 * This is the method that sets up the GUI and its associated functionality.
	 * @param frameX The X size of the GUI
	 * @param frameY The Y size of the GUI
	 * @param yLimit The furthest point words can fall to.
	 */
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit,score);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed: " + score.getMissed()+ "    ");
	    scr =new JLabel("Score: " + score.getScore()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    

	    final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          String input = textEntry.getText();
	          WordCatcher wordCatcher = new WordCatcher(input);
	          Thread wordThread = new Thread(wordCatcher);
	          wordThread.start();
	          
	          
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	startB = new JButton("Start");;
		
		
		endB = new JButton("End");;
		startB.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  startB.setEnabled(false);
	    	  endB.setEnabled(true);
	    	  thread = new Thread(w);
	    	  thread.start();
	    	  textEntry.requestFocus();  //return focus to the text entry field
	      }
	    });
				endB.addActionListener(new ActionListener()
			    {
				public void actionPerformed(ActionEvent e)
			      {
			    	  done = true;
			    	  w.done = true;
			    	  try {
						thread.join();;
						w.endAllThreads();
					} catch (InterruptedException e1) {
			 			// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	  
			    	  score.resetScore();
			    	  setupGUI(frameX, frameY, yLimit);
			    	  
			    	  int x_inc=(int)frameX/noWords;

			  		for (int i=0;i<noWords;i++) {
			  			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
			  		}
			    	  
			    	  
			    	  
			    	  endB.setEnabled(false);
			    	  startB.setEnabled(true);
			      }
			    });
				
				
		JButton quitB = new JButton("Quit");;
		quitB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}

	/**
	 * This is the method that opens the file and reads in the amount of words to be used by the game.
	 * The words are then stored into the WordDictionary class.
	 * @param filename The name of the file
	 * @return The dictionary of words to be used by the game
	 */
public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}


/**
 * This is the main method of the program that will initialize the process of the game. It reads in the words from the text file 
 * and then sets up the graphical user interface.
 * @param args command line arguments
 */
	public static void main(String[] args) {
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
	
		
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}


	}
	
	/**
	 * This is the inner class that evaluates if a string that the user has typed in matches to a word that is falling on the screen
	 * This class implements the Runnable interface so that it can be used as a thread to perform its own operations separately from the rest of the program.
	 * An instance of this class will be created every time a user types the enter button in the main GUI. 
	 * @author ShaiAarons
	 *
	 */
	public static class WordCatcher implements Runnable{

		String word;
		
		/**
		 * Constructor for this class
		 * @param word The word the user has typed in 
		 */
		public WordCatcher(String word) {
			this.word = word;
		}
		
		/**
		 * Run method that will iterate through the array of WordRecords use the WordRecord matchWord() method to evaluate if the user has correctly typed in a word
		 * If the user has correctly typed the word - the score will be incremented and the word will be reset using the resetWord() method
		 */
		@Override
		public void run() {
			for(int i = 0;i<noWords;i++) {
	        	  if(words[i].matchWord(word)) {
	        		  score.caughtWord(word.length());
	        		  words[i].resetWord();
	    
	        			  caught.setText("Caught: " + score.getCaught() + "    ");
					break;
	        		  
	        	  }
	          }
			scr.setText("Score: " + score.getScore()+ "    ");
			
		}
		
		
	}

}
