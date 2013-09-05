package ch.bulletproof.countdown.announcer;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * @author tourn
 * 
 * Uses vibration to announce the time running out
 */
public class VibrateAnnouncer implements Announcer{
	
	private static int I = 200;
	private static int V = 500;
	private static int X = 1000;
	private static int BREAK = 200;
	private Context ctx;
	
	private static long[] PATTERN_X = { 0, X };
	private static long[] PATTERN_V = { 0, V };
	private static long[] PATTERN_III = { 0, I, BREAK, I, BREAK, I };
	private static long[] PATTERN_II = { 0, I, BREAK, I };
	private static long[] PATTERN_I = { 0, I };
	private static long[] PATTERN_1ps = { 0, 125 };
	private static long[] PATTERN_2ps = { 0, 125, 375, 125 };
	private static long[] PATTERN_4ps = { 0, 125, 125, 125, 125, 125, 125, 125, 125 };
	private static long[] PATTERN_LONG = { 0, 2000 };
	
	public VibrateAnnouncer(Context ctx){
		this.ctx = ctx;
	}
	
	@Override
	public Announcement[] generateAnnouncements(){
		Vibrator vibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
		Announcement[] announcements = {
			new VibrateAnnouncement(vibrator, 10*60, PATTERN_X),
			new VibrateAnnouncement(vibrator, 5*60, PATTERN_V),
			new VibrateAnnouncement(vibrator, 3*60, PATTERN_III),
			new VibrateAnnouncement(vibrator, 2*60, PATTERN_II),
			new VibrateAnnouncement(vibrator, 60, PATTERN_I),
			new VibrateAnnouncement(vibrator, 30, PATTERN_V),
			new VibrateAnnouncement(vibrator, 20, PATTERN_V),
			new VibrateAnnouncement(vibrator, 15, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 14, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 13, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 12, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 11, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 10, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 9, PATTERN_1ps),
			new VibrateAnnouncement(vibrator, 8, PATTERN_2ps),
			new VibrateAnnouncement(vibrator, 7, PATTERN_2ps),
			new VibrateAnnouncement(vibrator, 6, PATTERN_2ps),
			new VibrateAnnouncement(vibrator, 5, PATTERN_2ps),
			new VibrateAnnouncement(vibrator, 4, PATTERN_4ps),
			new VibrateAnnouncement(vibrator, 3, PATTERN_4ps),
			new VibrateAnnouncement(vibrator, 2, PATTERN_4ps),
			new VibrateAnnouncement(vibrator, 1, PATTERN_4ps),
			new VibrateAnnouncement(vibrator, 0, PATTERN_LONG)
		};
		return announcements;
	}

}
