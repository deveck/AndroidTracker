package org.cw;

import org.cw.gps.IGpsStatusReceiver;
import org.cw.gps.LocationIdentifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * Represents the main application view.
 * The view sources are located at res/layout/main.xml
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackerApp extends MainActivity implements IGpsStatusReceiver, ICallback<Object>  {
	
	public enum RecordingUiEnum
	{
		/**
		 * Show the start recording button,
		 * stop and pause are invisible
		 */
		NotRecording,
		
		/**
		 * Show the recording button,
		 * stop and pause are visible
		 */
		Recording,
		
		/**
		 * Show the start recording button
		 * stop and pause are visible
		 */
		PausedRecording
	}
	
	
	private ImageButton _buttonStartRecording;
	private ImageButton _buttonPauseRecording;
	private ImageButton _buttonStopRecording;
	
	private ToggleButton _toggleLiveTracker;
	
	private TextView _labelLatitudeDegree;
	private TextView _labelLatitudeMinutes;
	private TextView _labelLatitudeSeconds;
	private TextView _labelLongitudeDegree;
	private TextView _labelLongitudeMinutes;
	private TextView _labelLongitudeSeconds;
	private TextView _labelAltitude;
	private TextView _labelSpeed;
	private TextView _labelDuration;
	private TextView _labelDistance;
	
	

	/** 
	 * Timer to update the ui regulary
	 */
	private UiTimer _timer;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setViewContent(ActivityConstants.START_MAINSCREEN);
       
        Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(this);

        _timer = new UiTimer(this);
        _timer.setInterval(200);
        _timer.setEnabled(true);
        _timer.execute();

        _toggleLiveTracker = (ToggleButton)findViewById(R.id.buttonStartLiveTracking);
        _labelLatitudeDegree = (TextView)findViewById(R.id.labelLatitudeDegree);
        _labelLatitudeMinutes = (TextView)findViewById(R.id.labelLatitudeMinutes);
        _labelLatitudeSeconds = (TextView)findViewById(R.id.labelLatitudeSeconds);
        _labelLongitudeDegree = (TextView)findViewById(R.id.labelLongitudeDegree);
        _labelLongitudeMinutes = (TextView)findViewById(R.id.labelLongitudeMinutes);
        _labelLongitudeSeconds = (TextView)findViewById(R.id.labelLongitudeSeconds);
        _labelAltitude = (TextView)findViewById(R.id.labelAltitude);
        _labelSpeed = (TextView)findViewById(R.id.labelSpeed);
        _labelDuration = (TextView)findViewById(R.id.labelDuration);
        _labelDistance = (TextView)findViewById(R.id.labelDistance);
        

        _buttonStartRecording = (ImageButton)findViewById(R.id.buttonStartRecording);
        _buttonStartRecording.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ButtonStartRecording_Clicked();				
			}
		});

        
        _buttonPauseRecording = (ImageButton)findViewById(R.id.buttonPauseRecording);
        _buttonStopRecording = (ImageButton)findViewById(R.id.buttonStopRecording);
               
        
        _toggleLiveTracker.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Environment.Instance().Settings().setLiveTrackerEnabled(isChecked);			
			}
		});
        
        
        _buttonPauseRecording.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ButtonPauseRecording_Clicked();
			}
		});
        
        _buttonStopRecording.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ButtonStopRecording_Clicked();				
			}
		});
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Environment.Instance().GPSProviderInstance().RemoveGpsStatusReceiver(this);
    }
    
    private void ButtonStartRecording_Clicked()
    {   			
    	if(Environment.Instance().getCurrentTrack() == null)
    		startActivityForResult(new Intent(this, NewTrackActivity.class), ActivityConstants.REQ_STARTRECORDING);
    	else
    		ContinueTrackRecording();
    }
    
    private void ButtonPauseRecording_Clicked()
    {
    	SetRecordingUiStyle(RecordingUiEnum.PausedRecording);
    	Environment.Instance().getCurrentTrack().getTrackTime().Pause();
    	Environment.Instance().getTrackRecorder().setEnabled(false);
    }
    
    private void ButtonStopRecording_Clicked()
    {
    	SetRecordingUiStyle(RecordingUiEnum.NotRecording);
    	Environment.Instance().getCurrentTrack().getTrackTime().Start();
    	Environment.Instance().getTrackRecorder().setEnabled(false);
    	Environment.Instance().getCurrentTrack().save();
    	Environment.Instance().registerTrack(null);
    }

    
    /**
     * Continues the recording of the current track 
     */
    private void ContinueTrackRecording()
    {
    	SetRecordingUiStyle(RecordingUiEnum.Recording);
    	Environment.Instance().getCurrentTrack().getTrackTime().Start();
    	Environment.Instance().getTrackRecorder().setEnabled(true);
    }
    
    /**
     * Just sets the user interface state of the recording buttons according to the passed value
     * @param uiState
     */
    private void SetRecordingUiStyle(RecordingUiEnum uiState)
    {
    	if(uiState == RecordingUiEnum.NotRecording)
    	{
    		_buttonPauseRecording.setVisibility(View.INVISIBLE);
    		_buttonStopRecording.setVisibility(View.INVISIBLE);
    		_buttonStartRecording.setImageResource(R.drawable.start_recording_bt);
    	}
    	else if(uiState == RecordingUiEnum.PausedRecording)
    	{
    		_buttonPauseRecording.setVisibility(View.VISIBLE);
    		_buttonStopRecording.setVisibility(View.VISIBLE);
    		_buttonStartRecording.setImageResource(R.drawable.start_recording_bt);
    	}
    	else if(uiState == RecordingUiEnum.Recording)
    	{
    		_buttonPauseRecording.setVisibility(View.VISIBLE);
    		_buttonStopRecording.setVisibility(View.VISIBLE);
    		_buttonStartRecording.setImageResource(R.drawable.recording_bt);
    	}
    }
    
    /**
     * Gets called on activity completion, if the activity was invoked using
     * startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	
    	//Start recording?
    	if(requestCode == ActivityConstants.REQ_STARTRECORDING &&
    	   resultCode  == ActivityConstants.RES_OK)
    	{
    		ContinueTrackRecording();
    	}
    	else if(requestCode == ActivityConstants.REQ_TRACKMANAGEMENT &&
    			resultCode == ActivityConstants.RES_OK)
    	{
    		ContinueTrackRecording();
    	}
    	
    }

	@Override
	public void LocationChanged(LocationIdentifier newLocation) 
	{
	
		_labelLongitudeDegree.setText(
				new Integer((int) newLocation.getLongitudeDegrees()).toString() + getResources().getString(R.string.degree_suffix));
		_labelLongitudeMinutes.setText(
				new Integer((int) newLocation.getLongitudeMinutes()).toString() + getResources().getString(R.string.minute_suffix));
		_labelLongitudeSeconds.setText(
				String.format("%.4f%s", newLocation.getLongitudeSeconds(),getResources().getString(R.string.second_suffix)));
		
		_labelLatitudeDegree.setText(
				new Integer((int) newLocation.getLatitudeDegrees()).toString() + getResources().getString(R.string.degree_suffix));
		_labelLatitudeMinutes.setText(
				new Integer((int) newLocation.getLatitudeMinutes()).toString() + getResources().getString(R.string.minute_suffix));
		_labelLatitudeSeconds.setText(
				String.format("%.4f%s", newLocation.getLatitudeSeconds(),getResources().getString(R.string.second_suffix)));
		
		_labelAltitude.setText(new Integer((int)newLocation.getLocation().getAltitude()).toString());
		
		_labelSpeed.setText(String.format("%.2f", newLocation.getSpeed()));
	}

	/** Method is called by the timer which raises every second to update the time and distance */
	@Override
	public void Callback(Object caller) 
	{
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		int distance = 0;
		if(Environment.Instance().getCurrentTrack() != null)
		{
			hours = (int) Environment.Instance().getCurrentTrack().getTrackTime().getDisplayHours();
			minutes = (int) Environment.Instance().getCurrentTrack().getTrackTime().getDisplayMinutes();
			seconds = (int) Environment.Instance().getCurrentTrack().getTrackTime().getDisplaySeconds();
			distance = (int) Environment.Instance().getCurrentTrack().getStatistics().getDistance();			
		}
		
		_labelDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		_labelDistance.setText(String.format("%06d", distance));
	}
}