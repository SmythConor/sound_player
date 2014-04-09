class BoundedBuffer {
	byte[] audioChunks;

	int nextIn;
	int nextOut;
	int size;
	boolean dataAvailable;
	boolean roomAvailable;

	public BoundedBuffer() {
		audioChunks = new byte[10];
		size = 10;
		roomAvailable = true;
		dataAvailable = false;
		nextIn = 0;
		nextOut = 0;
	}

	public synchronized void insertChunk(byte chunk) {
		if(!roomAvailable) { //if buffer is full waits for signal to contiute
			try {
				wait();
			} catch (InterruptedException e) { }

		}

		else {
			audioChunks[nextIn % 10] = chunk;
			size++;
			if(size > 9) {
				roomAvailable = false;
			}

		}

	}//insetChunk

	public synchronized byte removeChunk() {
		byte out = 0;
		if(!dataAvailable) { //if no data wait for signal
			try {
				wait();
			} catch (InterruptedException e) { }

		}

		else {
			out = audioChunks[nextOut % 10];
			//audioChunks[nextOut % 10] = null;

			nextOut++;
			size--;

			if(size == 0) {
				dataAvailable = false;
			}
			notifyAll();
		}

		return out;
	}//removeChunk
}//BoundedBuffer
