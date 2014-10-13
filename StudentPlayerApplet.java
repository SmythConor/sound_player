import javax.sound.sampled.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Player extends Panel implements Runnable {
    private static final long serialVersionUID = 1L;
    private TextField textfield;
    private TextArea textarea;
		private Font font;
		private String filename;

		public Player(String filename) {

			font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
			textfield = new TextField();
			textarea = new TextArea();
			textarea.setFont(font);
			textfield.setFont(font);
			setLayout(new BorderLayout());
			add(BorderLayout.SOUTH, textfield);
			add(BorderLayout.CENTER, textarea);
			textfield.addKeyListener(new KeyReleased() { 
					public void actionPerformed(KeyEvent e) { 
					int letter = e.getKeyCode(); 
					textarea.append("You said: " + i + "\n");
					}
					});
			/*textfield.addKeyListener(new ActionListener() {
				public void actionPerformed(KeyEvent e) {
				int letter = e.getKeyCode();
				textarea.append("You said: " + e.getActionCommand() + " " + i + "\n");
				textfield.setText("");
				}
				});
			 */

			this.filename = filename;
			new Thread(this).start();
			}

			public void run() {

				try {
					AudioInputStream s = AudioSystem.getAudioInputStream(new File(filename));
					AudioFormat format = s.getFormat();
					System.out.println("Audio format: " + format.toString());

					DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

					if(!AudioSystem.isLineSupported(info)) { 
						throw new UnsupportedAudioFileException();
					}	

					SourceDataLine line;
					line = (SourceDataLine) AudioSystem.getLine(info);
					line.open(format);
					line.start();
					BoundedBuffer buffer = new BoundedBuffer();
					Thread a = new Thread(new Producer(buffer, s));
					Thread b = new Thread(new Consumer(buffer, s, line));

					a.start();
					b.start();
					a.join();
					b.join();


				} catch (UnsupportedAudioFileException e ) {
					System.out.println("Player initialisation failed");
					e.printStackTrace();
					System.exit(1);
				} catch (LineUnavailableException e) {
					System.out.println("Player initialisation failed");
					e.printStackTrace();
					System.exit(1);
				} catch (IOException e) {
					System.out.println("Player initialisation failed");
					e.printStackTrace();
					System.exit(1);
				} catch (InterruptedException e) {

				}
			}
		}

		public class StudentPlayerApplet extends Applet
		{
			private static final long serialVersionUID = 1L;
			public void init() {
				setLayout(new BorderLayout());
				add(BorderLayout.CENTER, new Player(getParameter("file")));
			}
		}

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

						line.write(audioChunk, 0, byteRead);
					}

					line.drain();

					Thread.sleep((int)(Math.random() * 100));
				} catch (InterruptedException e) { } catch (IOException e) {}

				finally {
					line.stop();
					line.close();
					System.out.println("Goodbye from Consumer (" + Thread.currentThread().getName() + ")");
					Thread.currentThread().interrupt();
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
					Thread.currentThread().interrupt();
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
				System.out.println("In: " + nextIn);
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
				System.out.println("Remove: " + nextOut);
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
