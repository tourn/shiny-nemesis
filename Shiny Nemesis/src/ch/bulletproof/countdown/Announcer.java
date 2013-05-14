package ch.bulletproof.countdown;


public interface Announcer {
	/**
	 * @return an array containing all the announcements this Announcer provides
	 */
	public Announcement[] generateAnnouncements();

}
