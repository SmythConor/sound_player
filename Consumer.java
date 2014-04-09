import java.io.*;
import javax.sound.sampled.*;;
class Consumer extends Thread {
	private BoundedBuffer buffer;

	public Consumer(BoundedBuffer b) {
		buffer = b;
	}

	public void run() {
		try {
			/* read a chunk and play it here */
			byte[] audioChunk = buffer.removeChunk();
			SourceDataLine line;

			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();

			int bytesRead = s.read(audioChunk);

			line.write(audioChunk, 0, bytesRead);
			line.drain();
			line.stop();

			line.close();

			Thead.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { }

		finally {
			System.out.println("Goodbye from Consumer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}
