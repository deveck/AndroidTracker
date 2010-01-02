package org.cw;


import org.cw.connection.CrossingWaysConnection;
import org.cw.gps.DefaultGPSProvider;
import org.cw.gps.IGpsStatusReceiver;
import org.cw.gps.LocationIdentifier;
import org.cw.utils.AlertBuilder;
import android.app.Activity;
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
        
        Environment.Instance().CreateAlertBuilderInstance(this);
        Environment.Instance().CreateDefaultGPSProvider(this);
        Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(this);
        
        setContentView(R.layout.main);
        
        Button callSoap = (Button)findViewById(R.id.buttonOk);
        callSoap.setOnClickListener(new OnClickListener()
        	{
	    		@Override
	    		public void onClick(View v) {
	    			try
	    			{
	    				if(CrossingWaysConnection.Instance().VerifyCredentials(
	    						new UserCredentials(
	    							((EditText)findViewById(R.id.textUsername)).getText().toString(),
	    							((EditText)findViewById(R.id.textPassword)).getText().toString())
	    						)
	    					)
	    					Environment.Instance().AlertBuilderInstance().ShowInfoBox("Credential check successful", "", "OK");
	    				else
	    					Environment.Instance().AlertBuilderInstance().ShowInfoBox("Credential check FAILED", "", "OK");
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