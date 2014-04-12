import java.io.*;
import javax.sound.sampled.*;
class BoundedBuffer {
	int[] audioChunks; //buffer

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

		audioChunks[nextIn % size] = chunk; //Insert chunk into buffer

		nextIn++;
		dataAvailable = true; //indicate dataAvailable
		occupied++;

		if(occupied == 10) { //indicate no roomAvailable
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


		out = audioChunks[nextOut % size]; //remove chunk to return

		nextOut++; //update where to take from
		occupied--;

		if(occupied == 0) { //if none left change variables //changed from 0
			dataAvailable = false;
			roomAvailable = true;
		}

		return out; //return chunk
	}//removeChunk
}//BoundedBuffer
