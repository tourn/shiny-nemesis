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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class CountdownActivity extends Activity implements OnTouchListener{
	public static final String MINUTES = "tasty pie";
	
	Player player;
	boolean loaded = false;
	Timer timer;
	long endTime;
  
	/** Called when the activity is first created. */

	@Override
  	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_countdown);
		View view = findViewById(R.id.btn_cancel);
		view.setOnTouchListener(this);
		
		Intent intent = getIntent();
		int minutes = intent.getIntExtra(CountdownActivity.MINUTES, -1);
		
		if(minutes > 59 || minutes < 0)
			throw new IllegalArgumentException();
		
		Calendar endCalendar = Calendar.getInstance();
		if(endCalendar.get(Calendar.MINUTE) >= minutes)
			endCalendar.add(Calendar.HOUR, 1);
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MINUTE, minutes);
		
		timer = new Timer();
		player = new Player(Setup.BASE_VOICESET_DIR,Setup.DEFAULT_VOICESET_DIR);
		
		endTime = endCalendar.getTimeInMillis(); 
		super.onCreate(savedInstanceState);
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Schedule sounds
		for (Sound sound : player.getSounds()) {
			long schedule = endTime - 1000 * sound.getSecToEnd(); 
			if(schedule >= Calendar.getInstance().getTimeInMillis())
				timer.schedule(new SoundTask(sound), new Date(schedule));
		}
		
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				finish();
			}
		}, new Date(endTime));
		
//		timer.schedule(new TimerTask(){
//
//			@Override
//			public void run() {
//				TextView text = (TextView) findViewById(R.id.countdown);
//				long now = Calendar.getInstance().getTimeInMillis();
//				
//				Calendar remainingTime = Calendar.getInstance();
//				remainingTime.setTimeInMillis(endTime - now);
//				
//				text.setText(remainingTime.get(Calendar.MINUTE) + ":" + remainingTime.get(Calendar.SECOND));
//			}
//		}, 0, 1000);
		
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
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		timer.cancel();
		finish();
		return true;
	}
	
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
}
