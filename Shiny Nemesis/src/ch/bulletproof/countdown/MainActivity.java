package ch.bulletproof.countdown;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {
	private final String defaultVoiceSetDir = "default";
	
	private Player player;
	private boolean loaded = false;
	private Timer timer;
	private String baseVoiceSetDir = "VoiceSets";
	private String currentVoiceSetDir = defaultVoiceSetDir;
  
/** Called when the activity is first created. */

	@Override
  	public void onCreate(Bundle savedInstanceState) {
		//Setup
		//this.setUpDefaultSounds();
		//End Setup
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.add(Calendar.MINUTE, 5);
		endCalendar.add(Calendar.SECOND, 20);
		timer = new Timer();
		player = new Player(baseVoiceSetDir,currentVoiceSetDir);
		
		long endTime = endCalendar.getTimeInMillis(); 
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.activity_main);
//		View view = findViewById(R.id.foo);
//		view.setOnTouchListener(this);
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		for (Sound sound : player.getSounds()) {
			timer.schedule(new SoundTask(sound), new Date(endTime - 1000 * sound.getSecToEnd()));
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
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