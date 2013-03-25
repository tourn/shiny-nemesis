package ch.bulletproof.countdown;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * @author tourn
 * 
 * Starts a countdown ending in the next given MINUTE of the hour, the minute being passed
 * over an intent
 *
 */
public class CountdownActivity extends Activity{
	public static final String MINUTES = "minutes";
	
	private Player player;
	private Timer timer;
  
	@Override
  	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//check if the intent gave us a valid minute
		Intent intent = getIntent();
		int minutes = intent.getIntExtra(CountdownActivity.MINUTES, -1);
		
		if(minutes > 59 || minutes < 0)
			throw new IllegalArgumentException();
		
		//set up the view
		setContentView(R.layout.activity_countdown);
		
		View vbCancel = findViewById(R.id.btn_cancel);
		vbCancel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				timer.cancel();
				finish();
				return true;
			}
		});
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		
		// make a calendar pointing to the given minute in this hour
		long endTime = getNextMinuteOccurrence(minutes).getTimeInMillis();
		
		scheduleSounds(endTime);
		
		displayCountdown(endTime);
		
	}
	
	/**
	 * Displays the countdown from now to the endTime in a view.
	 * @param endTime
	 */
	private void displayCountdown(long endTime) {
		long now = Calendar.getInstance().getTimeInMillis();
		
		new CountDownTimer(endTime - now + 1000, 1000) {
			TextView text = (TextView) findViewById(R.id.countdown);
			
			public void onTick(long millisUntilFinished) {
				Calendar remainingTime = Calendar.getInstance();
				remainingTime.setTimeInMillis(millisUntilFinished - 1000);
				
				text.setText(remainingTime.get(Calendar.MINUTE) + ":" + remainingTime.get(Calendar.SECOND));
		     }

		     public void onFinish() {
		    	Calendar remainingTime = Calendar.getInstance();
				remainingTime.setTimeInMillis(0);
					
				text.setText(remainingTime.get(Calendar.MINUTE) + ":" + remainingTime.get(Calendar.SECOND));
		     }
		  }.start();
	}

	/**
	 * Returns a calendar pointing to the closest time having the given minute as minute
	 * Example: it's 14:22, input is 7
	 * The output calendar is set to 15:07 today
	 * @param minute
	 * @return
	 */
	private Calendar getNextMinuteOccurrence(int minute){
		Calendar out = Calendar.getInstance();
		if(out.get(Calendar.MINUTE) >= minute)
			out.add(Calendar.HOUR, 1);
		out.set(Calendar.SECOND, 0);
		out.set(Calendar.MINUTE, minute);
		return out;
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
	
	/**
	 * Schedules sounds of the default voiceset
	 * @param endTime
	 */
	private void scheduleSounds(long endTime){
		timer = new Timer();
		player = new Player(Setup.BASE_VOICESET_DIR,Setup.DEFAULT_VOICESET_DIR);
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "shiny nemesis");
		
		wl.acquire();
		
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
				wl.release();
				finish();
			}
		}, new Date(endTime));
	}
}
