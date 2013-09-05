package ch.bulletproof.countdown;

import android.media.SoundPool;

public class SoundAnnouncement extends Announcement {

	private SoundPool soundPool;
	private int id;

	/**
	 * Create a new soundannouncement
	 * @param soundPool the soundpool containing this announcement
	 * @param soundId the id of this sound in the soundpool
	 * @param secondsToEnd the amount of seconds before the end of the countdown this sound should be played
	 */
	public SoundAnnouncement(SoundPool soundPool, int soundId, int secondsToEnd) {
		this.soundPool= soundPool;
		this.id = soundId;
		this.secondsToEnd = secondsToEnd;
	}

	@Override
	public void play() {
		float volume = 1;
		soundPool.play(id, volume, volume, 0, 0, 1);
	}

}
