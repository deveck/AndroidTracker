package org.cw;

import java.util.Vector;

import org.cw.dataitems.TrackFile;
import org.cw.dataitems.TrackInformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class TrackListActivity extends Activity {
	
	private ListView _list;
	private Button _buttonLoad;
	private Button _buttonUpLoad;
	private Button _buttonDelete;
	private Button _buttonCancel;

	/**
	 * Stores all the available Tracks and switches view mode 
	 * if an action on the entry is running
	 */
	private TrackListDataAdapter _listAdapter = null;
	
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
		
        
        ((ImageButton)findViewById(R.id.buttonRecord)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonRecord_Clicked();
			}
		});
        
        ((ImageButton)findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonSettings_Clicked();
			}
		});            
        		
		
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
		
		RebuildTrackList();
		
		_buttonLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ButtonLoad_Clicked();
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
				ButtonDelete_Clicked();				
			}
		});
		
		_buttonCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setResult(ActivityConstants.RES_NOTHINGTODO);
				finish();
			}
		});

	}
	
	/** Load the selected track back to resume */
	private void ButtonLoad_Clicked()
	{
		if(_list.getCheckedItemPosition() == ListView.INVALID_POSITION)
			return;
		
		TrackFile fileToLoad = (TrackFile)_list.getItemAtPosition(_list.getCheckedItemPosition());
		
		TrackInformation trackInformation = TrackInformation.CreateFromTrackFile(fileToLoad);
		
		if(trackInformation == null)
		{
			Environment.Instance().AlertBuilderInstance().ShowInfoBox(
					"Unknown error loading track information file...", 
					"Error", 
					"OK");
		}
		else
		{
			Environment.Instance().registerTrack(trackInformation);
			
			setResult(ActivityConstants.RES_OK);
			finish();
		}
	}
	
    private void ButtonSettings_Clicked()
    {
    	startActivity(new Intent(this, SettingsActivity.class));
    }
    private void ButtonRecord_Clicked()
    {
		setResult(ActivityConstants.RES_NOTHINGTODO);
		finish();
	}   	
	
	/** After warning, deletes the selected Track  from disk */
	private void ButtonDelete_Clicked()
	{
		if(_list.getCheckedItemPosition() == ListView.INVALID_POSITION)
			return;
		
		final TrackFile selectedTrackFile = (TrackFile)_list.getItemAtPosition(_list.getCheckedItemPosition());
				
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Removal");
		alert.setMessage("Really remove "
				+ selectedTrackFile.toString()
				+ " permanently?");
		alert.setPositiveButton("Yes, remove",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						selectedTrackFile.DeleteMe();
						RebuildTrackList();
					}
				});
		alert.setNegativeButton("No", null);
		alert.show();
	}
	
	/** Relists the files from disk and adds them to the list */
	private void RebuildTrackList()
	{
		Vector<TrackFile> files = new Vector<TrackFile>();
		
		for(String file : fileList())
		{
			if(TrackFile.IsTrackFile(file))
			{
				TrackFile myFile = new TrackFile(this, file);
				files.add(myFile);
			}
		}
		
		_listAdapter = new TrackListDataAdapter(this, files);
		_list.setAdapter(new ArrayAdapter<TrackFile>(this,
				android.R.layout.simple_list_item_multiple_choice, files.toArray(new TrackFile[0])));
		
	}

}
