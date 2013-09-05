package ch.bulletproof.countdown.announcer;


/**
 * @author tourn
 * Represents a single announcement made by an announcer
 */
public abstract class Announcement {
	/**
	 * The amount of seconds this Announcement happens before the end of the countdown
	 */
	protected int secondsToEnd;
	
	/**
	 * Executes the Media action of this Announcement: Toasts, Vibration, Sounds...
	 */
	public abstract void play();

	public int getSecondsToEnd() {
		return secondsToEnd;
	}

	public void setSecondsToEnd(int secondsToEnd) {
		this.secondsToEnd = secondsToEnd;
	}
	
}
