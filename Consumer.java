import java.io.*;
import javax.sound.sampled.*;
class Consumer extends Thread {
	private BoundedBuffer buffer;
	private AudioInputStream s;
	private SourceDataLine line;

	public Consumer(BoundedBuffer b, AudioInputStream s1, SourceDataLine l1) {
		buffer = b;
		s = s1;
		line = l1;
	}

	public void run() {
		try {
			int byteRead = 0;
			byte[] audioChunk = new byte[1];
			while(byteRead != -1) {
				audioChunk = new byte[buffer.removeChunk()];
				byteRead = s.read(audioChunk);

				line.write(audioChunk, 0, byteRead); //play audio chunk
			}

			line.drain();

			Thread.sleep((int)(Math.random() * 100)); //sleep thread
		} catch (InterruptedException e) { } catch (IOException e) {}

		finally { //close line
			line.stop();
			line.close();
			System.out.println("Goodbye from Consumer (" + 
					Thread.currentThread().getName() + ")"); //goodbye message
			Thread.currentThread().interrupt(); //kill thread
		}//goodbye message

	}

}//Consumer
