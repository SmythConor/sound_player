import java.io.*;
import javax.sound.sampled.*;;
class Consumer extends Thread {
	private BoundedBuffer buffer;
	private SourceDataLine line;

	public Consumer(BoundedBuffer b, SourceDataLine l1) {
		buffer = b;
		line= l1;
	}

	public void run() {
		try {
			/* read a chunk and play it here */

			byte[] audioChunk = new byte[buffer.removeChunk()];
			//int bytesRead = s.read(audioChunk);

			line.write(audioChunk, 0, 1);
			line.drain();
			line.stop();

			Thread.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { }

		finally {
			line.close();
			System.out.println("Goodbye from Consumer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}
