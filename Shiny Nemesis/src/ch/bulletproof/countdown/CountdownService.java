package ch.bulletproof.countdown;
import java.sql.Date;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class CountdownService extends Service {
	public static final String EXTRA_END_TIME = "endtime";

	private static final String LOG_TAG = "CountdownService";

	private Player player;
	private Timer timer;
	private PowerManager.WakeLock wakeLock;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Schedules sounds of the default voiceset
	 * @param endTime
	 */
	private void scheduleSounds(long endTime){
		timer = new Timer();
		player = new Player(Setup.BASE_VOICESET_DIR,Setup.DEFAULT_VOICESET_DIR);

		//Schedule sounds
		for (Sound sound : player.getSounds()) {
			//			long schedule = endTime - 1000 * sound.getSecToEnd();
			long schedule = endTime - 1000 * sound.getSecToEnd() - 1000;
			if(schedule >= Calendar.getInstance().getTimeInMillis())
				timer.schedule(new SoundTask(sound), new Date(schedule));
		}

		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				//wakeLock.release();
				stopSelf();
			}
		}, new Date(endTime));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "shiny nemesis");
		wakeLock.acquire();

		long endTime = intent.getLongExtra(EXTRA_END_TIME, -1);

		scheduleSounds(endTime);

		Log.d(LOG_TAG,"Starting Countdown Service in Foreground");
		startForeground(1, buildNotification());
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * @author tourn
	 * A TimerTask to play a sound from a SoundPlayer.
	 *
	 */
	private class SoundTask extends TimerTask{
		Sound sound;

		public SoundTask(Sound sound){
			this.sound = sound;
		}

		@Override
		public void run() {
			player.play(sound);
		}
	}

	@Override
	public void onCreate() {
	}

	private Notification buildNotification(){
		Intent notificationIntent = new Intent(this, CountdownActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setContentTitle(getString(R.string.app_name))
		.setContentText("The Nemesis is running...")
		.setSmallIcon(R.drawable.ic_stat_general)
		.setOngoing(true)
		.setContentIntent(contentIntent);
		return builder.build();
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "Service destroyed");
		timer.cancel();
		if(wakeLock.isHeld())
			wakeLock.release();
		super.onDestroy();
	}

}
