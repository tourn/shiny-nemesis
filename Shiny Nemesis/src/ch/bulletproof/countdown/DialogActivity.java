package ch.bulletproof.countdown;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;

public class DialogActivity extends Activity {
	public static final String INTENT_EXTRA_KILL = "ch.bulletproof.countdown.DialogActivity.kill";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new Setup(this).verify();
		
		if(getIntent().getBooleanExtra(INTENT_EXTRA_KILL, false)){
			stopCountdown();
			finish();
		}
		setContentView(R.layout.activity_dialog);
		
		final TextView hour = (TextView) findViewById(R.id.hour);
		final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.minute);
		numberPicker.setMaxValue(59);
		numberPicker.setMinValue(0);
		int minute = getMinute();
		numberPicker.setValue(minute);
		hour.setText(getMatchingHour(minute)+":");
		
		final CheckBox vibrate = (CheckBox) findViewById(R.id.cbVibrate);
		vibrate.setChecked(getCheckboxStatus(vibrate));
		final CheckBox sound = (CheckBox) findViewById(R.id.cbSound);
		sound.setChecked(getCheckboxStatus(sound));
		
		Button goButton = (Button) findViewById(R.id.buttonGo);
		goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				numberPicker.clearFocus();
				storeCheckboxStatus(vibrate);
				storeCheckboxStatus(sound);
				storeMinute(numberPicker.getValue());
				startCountdown(numberPicker.getValue(), vibrate.isChecked(), sound.isChecked());
				finish();
				
			}
		});
		
		numberPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				hour.setText(getMatchingHour(newVal) + ":");
			}
		});
		
		findViewById(R.id.button_settings).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DialogActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog, menu);
		return true;
	}
	
	/**
	 * Starts countdown up to the given minute
	 * @param minute
	 */
	private void startCountdown(int minute, boolean vibrate, boolean sound){
		if( minute >= 0 && minute <= 59){
			
			long endTime = getNextMinuteOccurrence(minute).getTimeInMillis();
			Intent serviceIntent = new Intent(this, CountdownService.class);
			serviceIntent.putExtra(CountdownService.EXTRA_END_TIME, endTime);
			serviceIntent.putExtra(CountdownService.EXTRA_VIBRATE, vibrate);
			serviceIntent.putExtra(CountdownService.EXTRA_SOUND, sound);
			startService(serviceIntent);
		} else {
			Toast.makeText(getApplicationContext(), "Invalid minute", Toast.LENGTH_SHORT).show();
		}
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
	
	private int getMatchingHour(int minute){
		return getNextMinuteOccurrence(minute).get(Calendar.HOUR_OF_DAY);
	}
	
	private void stopCountdown(){
		Intent intent = new Intent(this, CountdownService.class);
		stopService(intent);
	}
	
	private void storeCheckboxStatus(CheckBox box){
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		prefs.edit().putBoolean(getStorageKey(box), box.isChecked()).apply();
	}

	private boolean getCheckboxStatus(CheckBox box){
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		return prefs.getBoolean(getStorageKey(box), false);
	}
	
	private String getStorageKey(View view){
		return "ch.bulletproof.view."+view.getId();
	}
	
	private boolean isRememberMinute(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return !prefs.getBoolean("use_offset", true);
	}
	
	private void storeMinute(int minute){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.edit().putString("minute", Integer.valueOf(minute).toString()).apply();
	}
	
	private int getMinute(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(isRememberMinute()){
			return Integer.valueOf(prefs.getString("minute", "0"));
		} else {
			int offset = Integer.valueOf(prefs.getString("minute_offset", "0"));
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			return (offset + minute) % 60;
		}
	}
}
