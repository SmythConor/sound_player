import java.io.*;
import javax.sound.sampled.*;
class Producer implements Runnable {
	private BoundedBuffer buffer;
	private AudioInputStream s;
	int i;

	public Producer(BoundedBuffer b, AudioInputStream s1) {
		buffer = b;
		s = s1;
		i = 0;
	}

	public void run() {
		try {
			AudioFormat format = s.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			int oneSecond = (int) (format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8);
			byte[] audioChunk = new byte[oneSecond];
			while(i < oneSecond) {
				/* insert chunk here */
				buffer.insertChunk(audioChunk[i]);
				i++;
				Thread.sleep((int)(Math.random() * 100));
			}
		} catch (InterruptedException e) { }

		finally {
			System.out.println("Goodbye from Producer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}
