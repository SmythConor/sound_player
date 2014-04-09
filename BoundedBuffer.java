/* nextIn (integer)
 * nextOut (integer)
 * size (integer)
 * occupied (integer)
 * ins (integer)
 * outs (integer)
 * dataAvailable (boolean)
 * roomAvailable (boolean)
 */

class BoundedBuffer {
	byte[] audioChunk;
	
	int nextIn;
	int nextOut;
	int size;
	int occupied;
	int ins;
	int outs;
	boolean dataAvailable;
	boolean roomAvailable;

	public BoundedBuffer(size) {
		audioChunk = new Byte[size];
		roomAvailable = true;
		dataAvailable = false;
	}

	public void insertChunk() {
		if(!roomAvailable) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}

		if(size > 9) {
			:
		}
	}

	public void removeChunk() {

	}
}
