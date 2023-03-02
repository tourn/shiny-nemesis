package ch.trq.countdown;

public class SoundpackNotPresentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6966786039038107198L;

	public SoundpackNotPresentException() {
		super();
	}

	public SoundpackNotPresentException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SoundpackNotPresentException(String detailMessage) {
		super(detailMessage);
	}

	public SoundpackNotPresentException(Throwable throwable) {
		super(throwable);
	}

}
