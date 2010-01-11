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
public class TrackerApp extends Activity implements IGpsStatusReceiver  {
	
	private Button _startRecordingButton;
	private Button _manageTracks;
	private ToggleButton _toggleLiveTracker;
	private TextView _labelLatitudeDegree;
	private TextView _labelLatitudeMinutes;
	private TextView _labelLatitudeSeconds;
	private TextView _labelLongitudeDegree;
	private TextView _labelLongitudeMinutes;
	private TextView _labelLongitudeSeconds;
	private TextView _labelAltitude;
	

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
        _startRecordingButton = (Button)findViewById(R.id.buttonStartRecording);
        _manageTracks = (Button)findViewById(R.id.buttonTracks);
        
        _manageTracks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonTracks_Clicked();
			}
		});
        
        ((Button)findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
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
    	startActivity(new Intent(this, NewTrackActivity.class));
    }
    //	startActivity(new Intent(this, TrackListActivity.class));
//    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
//    	alert.setTitle("lala");
//    	final EditText edit = new EditText(this);
//    	alert.setView(edit);
//    	alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//    	
//    	alert.show();
			
//			@Override
//			public void dismiss() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void cancel() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
    	
  //}

	@Override
	public void LocationChanged(LocationIdentifier newLocation) {
		/*Toast info = Toast.makeText(this, String.format("Location changed to long=%f lat=%f", 
				newLocation.getLocation().getLongitude(), 
				newLocation.getLocation().getLatitude()), Toast.LENGTH_SHORT);
		
		try
		{
			info.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		
		
		
		
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
	}
}