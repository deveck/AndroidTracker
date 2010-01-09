package org.cw;

import org.cw.gps.IGpsStatusReceiver;
import org.cw.gps.LiveTrackingUpdater;
import org.cw.gps.LocationIdentifier;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Represents the main application view.
 * The view sources are located at res/layout/main.xml
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackerApp extends Activity implements IGpsStatusReceiver  {
	
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
        
        ((Button)findViewById(R.id.SettingsButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ButtonSettings_Clicked();
			}
		});
    }
        
    private void ButtonSettings_Clicked()
    {
    	startActivity(new Intent(this, SettingsActivity.class));
    }
        

	@Override
	public void LocationChanged(LocationIdentifier newLocation) {
		Toast info = Toast.makeText(this, String.format("Location changed to long=%f lat=%f", 
				newLocation.getLocation().getLongitude(), 
				newLocation.getLocation().getLatitude()), Toast.LENGTH_SHORT);
		info.show();

		try
		{
		info.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
}