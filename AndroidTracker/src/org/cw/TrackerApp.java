package org.cw;

import org.cw.gps.IGpsStatusReceiver;
import org.cw.gps.LocationIdentifier;
import android.app.Activity;
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
        
        setContentView(R.layout.main);
        
        
        ((EditText)findViewById(R.id.textUsername)).setText(Environment.Instance().Settings().getUsername(""));
        ((EditText)findViewById(R.id.textPassword)).setText(Environment.Instance().Settings().getPassword(""));
        
        Button callSoap = (Button)findViewById(R.id.buttonOk);
        callSoap.setOnClickListener(new OnClickListener()
        	{
	    		@Override
	    		public void onClick(View v) {
	    			try
	    			{
	    				if(Environment.Instance().ConnectionInstance().VerifyCredentials(
	    						new UserCredentials(
	    							((EditText)findViewById(R.id.textUsername)).getText().toString(),
	    							((EditText)findViewById(R.id.textPassword)).getText().toString())
	    						)
	    					)
	    				{
	    					Environment.Instance().AlertBuilderInstance().ShowInfoBox("Credential check successful", "", "OK");
	    					Environment.Instance().Settings().setUsername(((EditText)findViewById(R.id.textUsername)).getText().toString());
	    					Environment.Instance().Settings().setPassword(((EditText)findViewById(R.id.textPassword)).getText().toString());
	    				}
	    				else
	    					Environment.Instance().AlertBuilderInstance().ShowInfoBox("Credential check FAILED", "", "OK");
	    				
	    				String username = ((EditText)findViewById(R.id.textUsername)).getText().toString();
	    				String password = ((EditText)findViewById(R.id.textPassword)).getText().toString();
	    				
	    				
	    				// Test Test, just send some coordinates 
	    				for(int i = 0; i<3; i++)
	    				{
	    					Location loc = new Location("gps");
		    				loc.setAltitude(1);
		    				loc.setLatitude(2);
	    					loc.setLongitude(i);
	    					Environment.Instance().ConnectionInstance().PostCurrentPositionRequest(username, password, 
		    						new LocationIdentifier(loc));
	    				}
	    				
	    				
	    			}
	    			catch(CWException e)
	    			{
	    				e.ShowAlertDialog();
	    			}
	    		}
        	}
        	
        );
        
        
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