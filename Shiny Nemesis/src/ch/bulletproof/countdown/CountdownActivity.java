package ch.bulletproof.countdown;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CountdownActivity extends Activity implements OnTouchListener{
	public static final String MINUTES = "tasty pie";
	
	Player player;
	boolean loaded = false;
	Timer timer;
  
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
		
		endCalendar.set(Calendar.MINUTE, minutes);
		
		timer = new Timer();
		player = new Player(this);
		
		long endTime = endCalendar.getTimeInMillis(); 
		super.onCreate(savedInstanceState);
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Schedule sounds
		for (Sound sound : player.getSounds()) {
			timer.schedule(new SoundTask(sound), new Date(endTime - 1000 * sound.getSecToEnd()));
		}
		
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				finish();
			}
		}, new Date(endTime));
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
			player.play(sound.getID());
		}
	}
}
