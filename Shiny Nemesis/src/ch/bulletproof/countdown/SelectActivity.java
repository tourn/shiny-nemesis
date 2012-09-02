package ch.bulletproof.countdown;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author latzer
 *
 * this activity lets the user select the minute the countdown ends.
 * the last X used choices will be presented in a list for quick access.
 */
public class SelectActivity extends Activity {
	
	private static final String PREF_RECENT_TIMES = "recent";
	private static final String PREF_RECENT_TIMES_SEPARATOR = ";";
	private Context ctx = this;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		Button button = (Button) findViewById(R.id.btn_go);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView minuteView = (TextView) findViewById(R.id.minute);
				
				//TODO validation
				int minute = Integer.parseInt(minuteView.getText().toString());
				if( minute >= 0 && minute <= 59){
				
					appendRecentTime(minute); //save recent_times to prefs
					
					Intent intent = new Intent(ctx, CountdownActivity.class);
					intent.putExtra(CountdownActivity.MINUTES, minute);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Invalid minute", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
	public void onResume(){
		super.onResume();
		//populate the recent list on resume
		String[] recentTimes = getRecentTimes();
		Log.d(SelectActivity.class.toString(), "got recent times, gonna display "  + recentTimes.toString());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, recentTimes);
		ListView list = (ListView) findViewById(R.id.recent_times);
		list.setAdapter(adapter);
	}
	
	/**
	 * @return the last times the user entered, the first being the most recent
	 */
	private String[] getRecentTimes(){
		//reading out the last times the user put in
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String recents = prefs.getString(PREF_RECENT_TIMES, null);
		return recents != null ? prefs.getString(PREF_RECENT_TIMES, "").split(PREF_RECENT_TIMES_SEPARATOR) : new String[0];
	}
	
	/**
	 * Puts this int in front of the list of recent times
	 * @return
	 */
	private void appendRecentTime(Integer in){
		HashSet<String> recents = new LinkedHashSet<String>();
		recents.add(in.toString());
		for(String recent : getRecentTimes()){
			recents.add(recent);
		}
		StringBuffer cat = new StringBuffer();
		for(Iterator<String> i = recents.iterator(); i.hasNext(); ){
			String recent = i.next();
			cat.append(recent);
			if(i.hasNext())
				cat.append(PREF_RECENT_TIMES_SEPARATOR);
			
			
		}
		
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Editor prefEditor = prefs.edit();
		prefEditor.putString(PREF_RECENT_TIMES, cat.toString());
		prefEditor.commit();
		Log.d(SelectActivity.class.toString(), "setting recent times to \"" + cat + "\"");
		
		//TODO maybe add a cap, so the list doesn't get endless?
		
	}

}
