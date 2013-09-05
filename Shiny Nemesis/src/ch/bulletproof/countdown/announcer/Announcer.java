package ch.bulletproof.countdown.announcer;


public interface Announcer {
	/**
	 * @return an array containing all the announcements this Announcer provides
	 */
	public Announcement[] generateAnnouncements();

}
