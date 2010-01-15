package org.cw;

import org.cw.gps.IGpsStatusReceiver;
import org.cw.gps.LiveTrackingUpdater;
import org.cw.gps.LocationIdentifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 * Represents the main application view.
 * The view sources are located at res/layout/main.xml
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackerApp extends Activity implements IGpsStatusReceiver  {
	
	
	private Button _startRecordingButton;
	private ImageButton _manageTracks;
	private ToggleButton _toggleLiveTracker;
	private TextView _labelLatitudeDegree;
	private TextView _labelLatitudeMinutes;
	private TextView _labelLatitudeSeconds;
	private TextView _labelLongitudeDegree;
	private TextView _labelLongitudeMinutes;
	private TextView _labelLongitudeSeconds;
	private TextView _labelAltitude;
	private TextView _labelSpeed;
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Environment.Instance().CreateSettings(this);
        Environment.Instance().CreateAlertBuilderInstance(this);
        Environment.Instance().CreateDefaultGPSProvider(this);
        Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(this);
        Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(new LiveTrackingUpdater());
        
        setContentView(R.layout.screen);
        
        _toggleLiveTracker = (ToggleButton)findViewById(R.id.buttonStartLiveTracking);
        _labelLatitudeDegree = (TextView)findViewById(R.id.labelLatitudeDegree);
        _labelLatitudeMinutes = (TextView)findViewById(R.id.labelLatitudeMinutes);
        _labelLatitudeSeconds = (TextView)findViewById(R.id.labelLatitudeSeconds);
        _labelLongitudeDegree = (TextView)findViewById(R.id.labelLongitudeDegree);
        _labelLongitudeMinutes = (TextView)findViewById(R.id.labelLongitudeMinutes);
        _labelLongitudeSeconds = (TextView)findViewById(R.id.labelLongitudeSeconds);
        _labelAltitude = (TextView)findViewById(R.id.labelAltitude);
        _labelSpeed = (TextView)findViewById(R.id.labelSpeed);
        
        _startRecordingButton = (Button)findViewById(R.id.buttonStartRecording);
        _manageTracks = (ImageButton)findViewById(R.id.buttonTracks);
        
        _manageTracks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonTracks_Clicked();
			}
		});
        
        ((ImageButton)findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonSettings_Clicked();
			}
		});
        
        _toggleLiveTracker.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Environment.Instance().Settings().setLiveTrackerEnabled(isChecked);			
			}
		});
        
        _startRecordingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ButtonStartRecording_Clicked();				
			}
		});
    }
    
    private void ButtonTracks_Clicked()
    {
    	startActivity(new Intent(this, TrackListActivity.class));
	}
        
    private void ButtonSettings_Clicked()
    {
    	startActivity(new Intent(this, SettingsActivity.class));
    }
       
   
    private void ButtonStartRecording_Clicked()
    {
    	startActivityForResult(new Intent(this, NewTrackActivity.class), ActivityConstants.REQ_STARTRECORDING);
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
}