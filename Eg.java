import javax.sound.sampled.*;
import java.io.*;

public class Eg {

	private static AudioInputStream validate(String[] args) throws UnsupportedAudioFileException, IOException {
		if (args.length != 1) {
			System.out.println("Please supply an audio file");
			System.exit(1);
		}

		return (AudioSystem.getAudioInputStream(new File(args[0])));
	}

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream s = validate(args);
		AudioFormat format = s.getFormat();	    

		System.out.println("Audio format: " + format.toString());

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("Cannot handle that audio format");
			System.exit(1);
		}

		int oneSecond = (int) (format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8);
		byte[] audioChunk = new byte[oneSecond];

		SourceDataLine line;

		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		int bytesRead = s.read(audioChunk);	

		line.write(audioChunk, 0, bytesRead);
		line.drain();
		line.stop();

		line.close();
	}

}
