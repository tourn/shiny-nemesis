package ch.bulletproof.countdown;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundAnnouncer implements Announcer {

	private static final String LOG_TAG = "SoundAnnouncer";
	private SoundPool soundPool;
	private Context ctx;
	private List<SoundAnnouncement> announcements = new ArrayList<SoundAnnouncement>();

	public SoundAnnouncer(Context ctx) throws SoundpackNotPresentException {
		this(ctx, "default");
	}

	public SoundAnnouncer(Context ctx, String soundPackName)
			throws SoundpackNotPresentException {
		this.ctx = ctx;

		// verify soundpack directory
		File soundPack = new Setup(ctx).getSoundpackDir(soundPackName);

		// XXX might make the stream configurable
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

		for (File sound : soundPack.listFiles()) {
			add(sound);
		}


	}

	private void add(File sound) {
		int secondsToEnd = parseSecondsToEnd(sound.getName());
		if (secondsToEnd == -1) {
			return;
		}
		int soundId = soundPool.load(sound.getPath(), 1);
		announcements.add(new SoundAnnouncement(soundPool, soundId, secondsToEnd));
	}

	/**
	 * Parses the filename, decoding how many seconds before the end of the
	 * countdown it is meant to be played
	 * 
	 * @param filename
	 * @return the seconds or -1 on an error
	 */
	private int parseSecondsToEnd(String filename) {
		try {
			Pattern minute = Pattern.compile("^min([0-9]+)\\.");
			Matcher matcher = minute.matcher(filename);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1)) * 60;
			}
			Pattern second = Pattern.compile("^sec([0-9]+)\\.");
			matcher = second.matcher(filename);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			}
		} catch (NumberFormatException e) {
			// this actually shouldn't happen since the regex only matches
			// numbers
			Log.e(LOG_TAG, "error parsing seconds to end", e);
		}
		return -1;
	}

	@Override
	public Announcement[] generateAnnouncements() {
		return announcements.toArray(new Announcement[announcements.size()]);
	}

}
