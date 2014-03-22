class Consumer extends Thread {
	private BoundedBuffer buffer;

	public Consumer(BoundedBuffer b) {
		buffer = b;
	}

	public void run() {

	}

}

/* Must take chunks off buffer and
 * write to audio device
 */
