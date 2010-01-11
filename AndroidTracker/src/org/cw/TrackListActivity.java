package org.cw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;

public class TrackListActivity extends Activity {
	
	private ListView _list;
	private Button _buttonLoad;
	private Button _buttonUpLoad;
	private Button _buttonDelete;
	private Button _buttonCancel;
		
	//TODO: changes on layout required! Max size an scrolling of list view
	String[] test = {"1","2","3","4"};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tracklist);
		
		_list = (ListView)findViewById(R.id.listTrackList);
		_buttonLoad = (Button)findViewById(R.id.buttonTrackListLoad);
		_buttonUpLoad = (Button)findViewById(R.id.buttonTrackListUpLoad);
		_buttonDelete = (Button)findViewById(R.id.buttonTrackListDelete);
		_buttonCancel = (Button)findViewById(R.id.buttonTrackListCancel);
		
		// store current track first?
		if (Environment.Instance().getCurrentTrack() != null) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Warning possible loose of data");
			alert.setMessage("Store "
					+ Environment.Instance().getCurrentTrack().getName()
					+ " now?");
			alert.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Environment.Instance().getCurrentTrack().save();
						}
					});
			alert.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alert.show();
		}
		
		//TODO: bring in real data
		_list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, test));
		
		_buttonLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO LOAD TRACK for resume
				finish();
			}
		});
		
		_buttonUpLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO UPLOAD track to crossingways
				//finish();
			}
		});
		
		_buttonDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO DELETE track
				
			}
		});
		
		_buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
