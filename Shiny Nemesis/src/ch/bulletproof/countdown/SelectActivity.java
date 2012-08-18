package ch.bulletproof.countdown;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class SelectActivity extends Activity {
	
	private Context ctx = this;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		String[] times = { "13", "41", "55" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, times);
		ListView list = (ListView) findViewById(R.id.recent_times);
		list.setAdapter(adapter);
		
		Button button = (Button) findViewById(R.id.btn_go);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView minuteView = (TextView) findViewById(R.id.minute);
				
				//TODO validation
				int minute = Integer.parseInt(minuteView.getText().toString());
				
				Intent intent = new Intent(ctx, CountdownActivity.class);
				intent.putExtra(CountdownActivity.MINUTE, minute);
				startActivity(intent);
				
			}
		});
	}

}
