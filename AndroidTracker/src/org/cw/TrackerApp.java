package org.cw;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Represents the main application view.
 * The view sources are located at res/layout/main.xml
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackerApp extends Activity {
    
	private OnClickListener _onDoSOAPCallClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			HttpTransportSE transport = new HttpTransportSE("http://www.crossingways.com/services/livetracking.asmx");
			
			SoapObject verifyCredentials = new  SoapObject("http://www.crossingways.com/", "VerifyCredentials");
			verifyCredentials.addProperty("username", "test");
			verifyCredentials.addProperty("password", "pw_hash");
			
			//For some reason hard coded control-string....strange
			verifyCredentials.addProperty("control", "CWRocks2008");
			
			
			
		}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button callSoap = (Button)findViewById(R.id.buttonOk);
        callSoap.setOnClickListener(_onDoSOAPCallClick);
    }
}