import java.io.*;
import javax.sound.sampled.*;

class Player1 {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		/* Check file type supported */ 
		AudioInputStream s = AudioSystem.getAudioInputStream(new File(args[0]));
		AudioFormat format = s.getFormat();
		System.out.println("Audio format: " + format.toString());

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		if(!AudioSystem.isLineSupported(info)) { 
			throw new UnsupportedAudioFileException();
		}	

		int oneSecond = (int) (format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8);
		byte[] audioChunk = new byte[oneSecond];
		int bytesRead = s.read(audioChunk);


		SourceDataLine line;
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		BoundedBuffer buffer = new BoundedBuffer();
		Thread a = new Thread(new Producer(buffer, s));
		Thread b = new Thread(new Consumer(buffer, s, line));

		a.start();
		b.start();
		a.join();
		b.join();

	}//main

}//class

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
			line.start();
			int byteRead = 0;
			while(byteRead != -1) {
				byte[] audioChunk = new byte[buffer.removeChunk()];
				byteRead = s.read(audioChunk);

				line.write(audioChunk, 0, byteRead);

			}

			Thread.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { } catch (IOException e) {}

		finally {
			line.drain();
			line.stop();
			line.close();
			System.out.println("Goodbye from Consumer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}

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
			int oneSecond = (int) (format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8);
			byte[] audioChunk = new byte[oneSecond];
			int chunk = 0;

			while(chunk != -1) {
				chunk = s.read(audioChunk);
				buffer.insertChunk(chunk);
			}

			Thread.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { } catch (IOException e) {}

		finally {
			System.out.println("Goodbye from Producer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}

class BoundedBuffer {
	int[] audioChunks;

	int nextIn;
	int nextOut;
	int size;
	int occupied;
	boolean dataAvailable;
	boolean roomAvailable;

	public BoundedBuffer() {
		size = 10;
		audioChunks = new int[size];
		roomAvailable = true;
		dataAvailable = false;
		nextIn = 0;
		nextOut = 0;
		occupied = 0;
	}

	public synchronized void insertChunk(int chunk) {
		while(!roomAvailable) { //if buffer is full waits for signal to contiute
			notifyAll();
			try {
				wait();
			} catch (InterruptedException e) { }
		}

		//System.out.println("hai from insert");
		//System.out.println(nextIn % size);

		audioChunks[nextIn % size] = chunk;
		nextIn++;
		dataAvailable = true;
		occupied++;

		if(occupied == 10) {
			roomAvailable = false;
		}

	}//insetChunk

	public synchronized int removeChunk() {
		int out = 0;
		while(!dataAvailable) { //if no data wait for signal
			notifyAll();
			try {
				wait();
			} catch (InterruptedException e) { }

		}

		//System.out.println("hai from remove");
		//System.out.println(nextOut % size);

		out = audioChunks[nextOut % size];

		nextOut++;
		occupied--;

		if(occupied == 0) {
			dataAvailable = false;
			roomAvailable = true;
		}

		return out;
	}//removeChunk
}//BoundedBuffer
