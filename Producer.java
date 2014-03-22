class Producer implements Runnable {
	private BoundedBuffer buffer;

	public Producer(BoundedBuffer b) {
		buffer = b;
	}

	public void run() {
		try {
			buffer.insertChunk();
			Thread.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { }

	}

}

/* Must constantly read from audio stream 
 * and add to buffer 
 */

/* Must close thread properly too
 *	Not usre if relevant here
 */
