class Producer implements Runnable {
	private BoundedBuffer buffer;

	public Producer(BoundedBuffer b) {
		buffer = b;
	}

	public void run() {
		try {
		/* read in one second chunk here */
			int oneSecond = (int) (format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8);
			byte[] audioChunk = new byte[oneSecond];
			
			/* insert chunk here */
			buffer.insertChunk(chunk);
			Thread.sleep((int)(Math.random() * 100));
		} catch (InterruptedException e) { }

		finally {
			System.out.println("Goodbye from Producer (" + Thread.currentThread().getName() + ")");
		}//goodbye message

	}

}
