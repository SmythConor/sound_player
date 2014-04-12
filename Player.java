/*
 	Members: 
	Conor Smyth 12452382
	Adam O'Flynn 12378651 
	Kate Falahee 12417922
	Owen Lennon 12379716
 */

import java.io.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import java.awt.*;

class Player {

	public static void main(String[] args) 
	throws UnsupportedAudioFileException, IOException, 
			LineUnavailableException, InterruptedException {
		AudioInputStream s = AudioSystem.getAudioInputStream(
							new File(args[0]));
	 	AudioFormat format = s.getFormat();
	 	System.out.println("Audio format: " + format.toString());

	 	DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

	 	if(!AudioSystem.isLineSupported(info)) { 
			 throw new UnsupportedAudioFileException();
	 	}	

	 	SourceDataLine line; //initalise line
	 	line = (SourceDataLine) AudioSystem.getLine(info);
	 	line.open(format);
	 	line.start();
	 	BoundedBuffer buffer = new BoundedBuffer(); //initailise buffer
	 	Thread a = new Thread(new Producer(buffer, s)); /* Threads */
	 	Thread b = new Thread(new Consumer(buffer, s, line));

	 	a.start();
	 	b.start();
	 	a.join();
	 	b.join();

 }//main

}//class
