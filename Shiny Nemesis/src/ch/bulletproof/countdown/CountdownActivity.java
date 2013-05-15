package ch.bulletproof.countdown;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private static final String LOG_TAG = "Countdown";



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
				Intent intent = new Intent(getApplicationContext(), CountdownService.class);
				stopService(intent);
				finish();
				return true;
			}
		});

		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);


		// make a calendar pointing to the given minute in this hour
		long endTime = getNextMinuteOccurrence(minutes).getTimeInMillis();

		displayCountdown(endTime);

		//start the service to play the sound
		Intent serviceIntent = new Intent(this, CountdownService.class);
		serviceIntent.putExtra(CountdownService.EXTRA_END_TIME, endTime);
		startService(serviceIntent);
	}

	/**
	 * Displays the countdown from now to the endTime in a view.
	 * @param endTime
	 */
	private void displayCountdown(long endTime) {
		long now = Calendar.getInstance().getTimeInMillis();

		new CountDownTimer(endTime - now + 1000, 1000) {
			TextView text = (TextView) findViewById(R.id.countdown);

			@Override
			public void onTick(long millisUntilFinished) {
				Calendar remainingTime = Calendar.getInstance();
				remainingTime.setTimeInMillis(millisUntilFinished - 1000);

				text.setText(remainingTime.get(Calendar.MINUTE) + ":" + secondsAsString(remainingTime.get(Calendar.SECOND)));
			}

			@Override
			public void onFinish() {
				Calendar remainingTime = Calendar.getInstance();
				remainingTime.setTimeInMillis(0);
				text.setText("0:00");
				finish();
			}
		}.start();
	}
	
	/**
	 * Returns the given second integer as a zero-padded string
	 * @param seconds
	 * @return
	 */
	private String secondsAsString(int seconds){
		String out = Integer.valueOf(seconds).toString();
		if(out.length() == 1) return "0" + out;
		return out;
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

}
