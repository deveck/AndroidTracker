package org.cw;


import org.cw.connection.CrossingWaysConnection;
import org.cw.utils.AlertBuilder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


/**
 * Represents the main application view.
 * The view sources are located at res/layout/main.xml
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackerApp extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertBuilder.CreateInstance(this);
        setContentView(R.layout.main);

        
        Button callSoap = (Button)findViewById(R.id.buttonOk);
        callSoap.setOnClickListener(new OnClickListener()
        	{
	    		@Override
	    		public void onClick(View v) {
	    			try
	    			{
	    				if(CrossingWaysConnection.Instance().VerifyCredentials(
	    						((EditText)findViewById(R.id.textUsername)).getText().toString(),
	    						((EditText)findViewById(R.id.textPassword)).getText().toString()))
	    					AlertBuilder.Instance().ShowInfoBox("Credential check successful", "", "OK");
	    				else
	    					AlertBuilder.Instance().ShowInfoBox("Credential check FAILED", "", "OK");
	    			}
	    			catch(CWException e)
	    			{
	    				e.ShowAlertDialog();
	    			}
	    		}
        	}
        );
    }
}