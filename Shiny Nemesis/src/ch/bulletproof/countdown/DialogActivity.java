package ch.bulletproof.countdown;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		
		final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.minute);
		numberPicker.setMaxValue(59);
		numberPicker.setMinValue(0);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		numberPicker.setValue(minute);
		
		Button goButton = (Button) findViewById(R.id.buttonGo);
		goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startCountdown(numberPicker.getValue());
				finish();
				
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
	private void startCountdown(int minute){
		if( minute >= 0 && minute <= 59){
		
			Intent intent = new Intent(this, CountdownActivity.class);
			intent.putExtra(CountdownActivity.MINUTES, minute);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "Invalid minute", Toast.LENGTH_SHORT).show();
		}
	}

}
