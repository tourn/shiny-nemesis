package ch.bulletproof.countdown.legacy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import ch.bulletproof.countdown.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
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
	private String[] recentTimes;
	private Context ctx = this;
	
	public void onCreate(Bundle savedInstanceState){
		Setup.init(getApplicationContext());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		Button button = (Button) findViewById(R.id.btn_go);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView minuteView = (TextView) findViewById(R.id.minute);
				
				int minute = Integer.parseInt(minuteView.getText().toString());
				startCountdown(minute);
				minuteView.setText("");
				
			}
		});
	}
	
	/**
	 * Starts countdown up to the given minute
	 * @param minute
	 */
	private void startCountdown(int minute){
		if( minute >= 0 && minute <= 59){
		
			appendRecentTime(minute); //save recent_times to prefs
			
			Intent intent = new Intent(ctx, CountdownActivity.class);
			intent.putExtra(CountdownActivity.MINUTES, minute);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "Invalid minute", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onResume(){
		super.onResume();
		//populate the recent list on resume
		recentTimes = getRecentTimes();
		Log.d(SelectActivity.class.toString(), "got recent times, gonna display "  + recentTimes.toString());
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recentTimes){
			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				View view = super.getView(position, convertView, parent);
//				view.setActivated(true);
				//XXX why is this necessary?
				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				textView.setTextColor(Color.BLACK);
				
				return view;
			}
			
		};
		GridView list = (GridView) findViewById(R.id.recent_times);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String minute_s = adapter.getItem(position);
				startCountdown(Integer.parseInt(minute_s));
				
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				clearRecentTime(Integer.valueOf(textView.getText().toString()));
				recentTimes=getRecentTimes();
				adapter.notifyDataSetChanged();
				return true;
			}
		});
//		list.setActivated(true);
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
	
	/**
	 * Removes all recent time entries
	 */
	private void clearRecentTimes(){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Editor prefEditor = prefs.edit();
		prefEditor.remove(PREF_RECENT_TIMES);
		prefEditor.commit();
	}
	
	/**
	 * Removes the given recent time entry
	 * @param in
	 */
	private void clearRecentTime(Integer in){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Editor prefEditor = prefs.edit();
		HashSet<String> recents = new LinkedHashSet<String>();
		for(String recent : getRecentTimes()){
			if(Integer.valueOf(recent) == in) continue;
			recents.add(recent);
		}
		StringBuffer cat = new StringBuffer();
		for(Iterator<String> i = recents.iterator(); i.hasNext(); ){
			String recent = i.next();
			cat.append(recent);
			if(i.hasNext())
				cat.append(PREF_RECENT_TIMES_SEPARATOR);
			
			
		}
		prefEditor.putString(PREF_RECENT_TIMES, cat.toString());
		Log.d("SelectActivity","saving " + cat.toString());
		prefEditor.commit();
	}

}
