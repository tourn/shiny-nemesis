package ch.bulletproof.countdown;
import java.sql.Date;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
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
	private NotificationManager notificationManger;
	private CountDownTimer countdownTimer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Schedules sounds of the default voiceset
	 * @param endTime
	 */
	private void scheduleSounds(final long endTime){
		timer = new Timer();
		notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Announcement[] announcements = new VibrateAnnouncer(this).generateAnnouncements();
		for(final Announcement announcement : announcements){
			long schedule = endTime - 1000 * announcement.getSecondsToEnd() - 1000;
			if(schedule >= Calendar.getInstance().getTimeInMillis()){
				timer.schedule(new TimerTask(){

					@Override
					public void run() {
						announcement.play();

					}

				}, new Date(schedule));
			}
		}

		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				//wakeLock.release();
				stopSelf();
			}
		}, new Date(endTime));

		countdownTimer = new CountDownTimer(endTime - Calendar.getInstance().getTimeInMillis() + 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				Calendar remainingTime = Calendar.getInstance();
				remainingTime.setTimeInMillis(millisUntilFinished - 1000);
				notificationManger.notify(1, buildNotification(endTime));


			}

			@Override
			public void onFinish() {
			}
		}.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "shiny nemesis");
		wakeLock.acquire();

		long endTime = intent.getLongExtra(EXTRA_END_TIME, -1);

		scheduleSounds(endTime);

		Log.d(LOG_TAG,"Starting Countdown Service in Foreground");
		startForeground(1, buildNotification(endTime));
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
	}

	private Notification buildNotification(long endTime){
		long millisUntilFinished = endTime - Calendar.getInstance().getTimeInMillis();
		
		Calendar remainingTime = Calendar.getInstance();
		remainingTime.setTimeInMillis(millisUntilFinished - 1000);
		String remainingString = remainingTime.get(Calendar.MINUTE) + ":" + CountdownActivity.secondsAsString(remainingTime.get(Calendar.SECOND));
		
		Intent notificationIntent = new Intent(this, CountdownActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setContentTitle(getString(R.string.app_name))
		.setContentText(remainingString + " left")
		.setSmallIcon(R.drawable.ic_stat_general)
		.setOngoing(true)
		.setWhen(endTime)
		.setContentIntent(contentIntent);
		return builder.build();
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "Service destroyed");
		timer.cancel();
		countdownTimer.cancel();
		if(wakeLock.isHeld())
			wakeLock.release();
		super.onDestroy();
	}

}
