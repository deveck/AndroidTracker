package org.cw;

import java.util.List;
import java.util.Vector;

import org.cw.TrackListDataAdapter.ViewTypeEnum;
import org.cw.connection.IUiCallback;
import org.cw.connection.RequestProgressInfo;
import org.cw.connection.RequestProgressInfo.StatusTypeEnum;
import org.cw.dataitems.TrackFile;
import org.cw.dataitems.TrackInformation;
import org.cw.utils.AlertBuilder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TrackListActivity extends MainActivity {
	
	public class PendingUploadInfo implements IUiCallback
	{
		/** TrackFile this instance carries about */
		private TrackFile _trackFile;
		
		private TrackListActivity _trackListActivity; 
		
		private TrackListDataAdapter _listAdapter = null;
		
		public PendingUploadInfo(TrackFile trackFile, TrackListDataAdapter listAdapter, TrackListActivity activity)
		{
			_trackFile = trackFile;
			_listAdapter = listAdapter;
			_trackListActivity = activity;
		}
		
		@Override
		public void StatusUpdate(RequestProgressInfo progressInfo) 
		{
			int position = _listAdapter.getPosition(_trackFile);
			if(progressInfo.getStatus() == StatusTypeEnum.ERROR)
			{
				_listAdapter.setViewType(position, ViewTypeEnum.Default);
				_trackListActivity._alerts.ShowInfoBox(
						String.format("There was an error sending the track '%s', please try again later", _trackFile.toString()),
						"Error", "Close");
				progressInfo.setRequeue(false);
				_trackListActivity.RemoveUploadInfo(this);
			}
			else if(progressInfo.getStatus() == StatusTypeEnum.COMPLETED)
			{
				_listAdapter.setViewType(position, ViewTypeEnum.Default);
				_trackListActivity._alerts.ShowInfoBox(
						String.format("The track '%s' was sucessfully sent!", _trackFile.toString()),								
						"Success", 
						"Close");
				_trackListActivity.RemoveUploadInfo(this);
			}			
		}
		
	}
	
	/**
	 * Contains all uploads that are currently in progress, 
	 * as long this list is not empty, it is not allowed to close the activity
	 */
	private List<PendingUploadInfo> _pendingUploads = new Vector<PendingUploadInfo>();
	
	private AlertBuilder _alerts;
	
	private ListView _list;
	private Button _buttonLoad;
	private Button _buttonUpLoad;
	private Button _buttonDelete;

	/**
	 * Stores all the available Tracks and switches view mode 
	 * if an action on the entry is running
	 */
	private TrackListDataAdapter _listAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		super.setViewContent(ActivityConstants.START_TRACKSCREEN);
		
		
		_alerts = new AlertBuilder(this);
//		
//		setContentView(R.layout.tracklist);
//		
		_list = (ListView)findViewById(R.id.listTrackList);
		_buttonLoad = (Button)findViewById(R.id.buttonTrackListLoad);
		_buttonUpLoad = (Button)findViewById(R.id.buttonTrackListUpLoad);
		_buttonDelete = (Button)findViewById(R.id.buttonTrackListDelete);
		//_buttonCancel = (Button)findViewById(R.id.buttonTrackListCancel);
//		
//        
//        ((ImageButton)findViewById(R.id.buttonRecord)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ButtonRecord_Clicked();
//			}
//		});
//        
//        ((ImageButton)findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ButtonSettings_Clicked();
//			}
//		});            
//        		
//		
//		// store current track first?
//		if (Environment.Instance().getCurrentTrack() != null) {
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle("Warning possible loose of data");
//			alert.setMessage("Store "
//					+ Environment.Instance().getCurrentTrack().getName()
//					+ " now?");
//			alert.setPositiveButton("YES",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Environment.Instance().getCurrentTrack().save();
//						}
//					});
//			alert.setNegativeButton("NO",
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						}
//					});
//			alert.show();
//		}
//		
		RebuildTrackList();
//		
		_buttonLoad.setOnClickListener(new OnClickListener() {
//			
			@Override
			public void onClick(View v) {
				ButtonLoad_Clicked();
			}
		});
//		
		_buttonUpLoad.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				ButtonUpload_Clicked();
			}
		});
//		
		_buttonDelete.setOnClickListener(new OnClickListener() {			

			@Override
			public void onClick(View v) {
				ButtonDelete_Clicked();				
			}
		});
//		
//		_buttonCancel.setOnClickListener(new OnClickListener() {			
//
//			@Override
//			public void onClick(View v) {
//				ButtonCancel_Clicked();
//			}
//		});

	}
	
	/**
	 * Checks if any action on the activity other than upload posting is possible.
	 * If any upload is posted, the activity cannot be closed
	 * @return
	 */
	private boolean IsActionPossible()
	{
		if(_pendingUploads.size() > 0)
			_alerts.ShowInfoBox("Please wait till all uploads are ready....", "Please wait", "Close");
		
		return !(_pendingUploads.size() > 0);
	}
	
//	private void ButtonCancel_Clicked()
//	{
//		if(IsActionPossible())
//		{
//			setResult(ActivityConstants.RES_NOTHINGTODO);
//			finish();
//		}
//	}
	
	private void ButtonUpload_Clicked()
	{
		try
		{
			if(_list.getCheckedItemPosition() == ListView.INVALID_POSITION)
				return;
			
			_listAdapter.setViewType(_list.getCheckedItemPosition(), ViewTypeEnum.Working);
			
			TrackFile myTrackFile = (TrackFile)_list.getItemAtPosition(_list.getCheckedItemPosition());
			
			if(_pendingUploads.contains(myTrackFile))
				return;
			
			PendingUploadInfo newUploadInfo = new PendingUploadInfo(myTrackFile, _listAdapter, this);
			_pendingUploads.add(newUploadInfo);
			Environment.Instance().ConnectionInstance().PostGPXUploadRequest(
					Environment.Instance().Settings().getUsername(),
					Environment.Instance().Settings().getPassword(),
					myTrackFile.toString(),
					myTrackFile.getGPXData(),
					newUploadInfo);
		}
		catch(Exception e)
		{
			Environment.Instance().AlertBuilderInstance().ShowInfoBox(
				e.getMessage(),
				"Error", 
				"Close");
		}
	}
	
	/** Load the selected track back to resume */
	private void ButtonLoad_Clicked()
	{
		if(_list.getCheckedItemPosition() == ListView.INVALID_POSITION)
			return;
		
		if(IsActionPossible() == false)
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
	
	
	/** After warning, deletes the selected Track  from disk */
	private void ButtonDelete_Clicked()
	{
		if(_list.getCheckedItemPosition() == ListView.INVALID_POSITION)
			return;

		if(IsActionPossible() == false)
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
		_list.setAdapter(_listAdapter);
		
	}
	
	/** Removes the specified PendingUploadInfo from the pending upload info list */
	private void RemoveUploadInfo(PendingUploadInfo info)
	{
		_pendingUploads.remove(info);
	}
	
	protected final void onButtonRecord_clicked(){
		if(IsActionPossible() == false)
			return;
		super.onButtonRecord_clicked();
	}
	
	protected final void onButtonSettings_clicked(){
		if(IsActionPossible() == false)
			return;
		super.onButtonSettings_clicked();
	}
	
	protected final void onButtonTracks_clicked(){
		super.onButtonTracks_clicked();
	}

}
