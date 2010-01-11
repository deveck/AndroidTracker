package org.cw;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;

public class TrackListActivity extends Activity {
	
	String[] test = {"1","2","3","4"};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tracklist);
		
		ListView list;
		list=(ListView)findViewById(R.id.listTrackList);
		TextView label = new TextView(this);
		label.setText("test");
		list.addHeaderView(label, null, false);
		
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, test));

	}

}
