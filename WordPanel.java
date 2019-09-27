

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

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private Score score;
		private Thread[] threads;
		public static int messageCount;


		
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
		
		WordPanel(WordRecord[] words, int maxY, Score score) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
			this.score = score;
			messageCount = 0;
		}
		
		
		
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
		

		 
		
		//Inner class
	public class wordFalling implements Runnable{
			WordRecord word;
			
			public wordFalling(WordRecord word) {
				this.word = word;
			}
			
			
			
			
			
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
					
					
					word.drop(3);
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


