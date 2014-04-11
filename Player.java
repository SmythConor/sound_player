import java.io.*;
import javax.sound.sampled.*;

class Player {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		/* Check file type supported */
		AudioInputStream s = validate(args);
		AudioFormat format = s.getFormat();

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		if(!AudioSystem.isLineSupported(info)) { 
			System.out.println("Format not supported");
			System.exit(1);
		}	
		/***********************************/

		SourceDataLine line;
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		BoundedBuffer buffer = new BoundedBuffer();
		Thread a = new Thread(new Producer(buffer, s));
		Thread b = new Thread(new Consumer(buffer, line));

		a.start();
		b.start();
		a.join();
		b.join();

	}//main

	private static AudioInputStream validate(String[] args) throws UnsupportedAudioFileException, IOException {
		if(args.length != 1) {
			System.out.println("Please supply an audio file");
			System.exit(1);
		}

		return AudioSystem.getAudioInputStream(new File(args[0]));
	}//check if audio file supplied as argument

}//class
