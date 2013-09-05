package ch.bulletproof.countdown.announcer;

import android.os.Vibrator;
import android.util.Log;

public class VibrateAnnouncement extends Announcement {
	
	private long[] pattern;
	private Vibrator vibrator;
	
	/**
	 * @param vibrator the android system vibrator
	 * @param secondsToEnd @see {@link Announcement}
	 * @param pattern a pattern to be passed to @link {@link Vibrator}
	 */
	public VibrateAnnouncement(Vibrator vibrator, int secondsToEnd, long[] pattern){
		super.secondsToEnd = secondsToEnd;
		this.pattern = pattern;
		this.vibrator = vibrator;
	}

	@Override
	public void play() {
		vibrator.vibrate(pattern, -1);
		Log.d("VibrateAnnouncement", Integer.valueOf(secondsToEnd).toString() + " Vibrating " + pattern.toString());
	}

}
