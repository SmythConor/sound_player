class BoundedBuffer {
	byte[] audioChunks;

	int nextIn;
	int nextOut;
	int size;
	boolean dataAvailable;
	boolean roomAvailable;

	public BoundedBuffer() {
		size = 10;
		audioChunks = new byte[size];
		roomAvailable = true;
		dataAvailable = false;
		nextIn = 0;
		nextOut = 0;
	}

	public synchronized void insertChunk(byte chunk) {
		while(!roomAvailable) { //if buffer is full waits for signal to contiute
			try {
				wait();
			} catch (InterruptedException e) { }

		}

//		else {
			byte audioChunk = chunk;
			audioChunks[nextIn % size] = audioChunk;
			nextIn++;
			size++;

			if(size > 9) {
				roomAvailable = false;
			}

			notifyAll();
	//	}

	}//insetChunk

	public synchronized byte removeChunk() {
		byte out = 0;
		while(!dataAvailable) { //if no data wait for signal
			try {
				wait();
			} catch (InterruptedException e) { }

		}

		//else {
			out = audioChunks[nextOut % size];

			nextOut++;
			size--;

			if(size == 0) {
				dataAvailable = false;
			}

			notifyAll();
		//}

		return out;
	}//removeChunk
}//BoundedBuffer
