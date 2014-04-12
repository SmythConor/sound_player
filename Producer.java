import java.io.*;
import javax.sound.sampled.*;
class Producer implements Runnable {
	private BoundedBuffer buffer;
	AudioInputStream s;

	public Producer(BoundedBuffer b, AudioInputStream s1) {
		buffer = b;
		s = s1;
	}

	public void run() {
		try {
			AudioFormat format = s.getFormat();
			int oneSecond = (int) (format.getChannels() * 
					format.getSampleRate() * 
					format.getSampleSizeInBits() / 8); //get one second chunk
			byte[] audioChunk = new byte[oneSecond];
			int chunk = 0;

			while(chunk != -1) {
				chunk = s.read(audioChunk);
				buffer.insertChunk(chunk);
			}//insert chunks into buffer

			Thread.sleep((int)(Math.random() * 100)); //sleep thread
		} catch (InterruptedException e) { } catch (IOException e) {}

		finally {
			System.out.println("Goodbye from Producer (" + 
					Thread.currentThread().getName() + ")"); //goodbye message
			Thread.currentThread().interrupt(); //kill thread
		}//goodbye message

	}

}//Producer
