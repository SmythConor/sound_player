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
		roomAvailable = false;
		dataAvailable = false;
	}

	public void insertChunk() {
		
	}

	public void removeChunk() {

	}
}
