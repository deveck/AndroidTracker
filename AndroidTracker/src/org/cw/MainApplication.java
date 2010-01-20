package org.cw;

import org.cw.gps.LiveTrackingUpdater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainApplication extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent _currentActivity = new Intent(this, TrackerApp.class);
		startActivityForResult(_currentActivity, ActivityConstants.ACTIVITY_CHOOSE);
		
		Environment.Instance().CreateTrackRecorder();		
		Environment.Instance().CreateSettings(this);
		Environment.Instance().CreateAlertBuilderInstance(this);
		Environment.Instance().CreateDefaultGPSProvider(this);
		Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(new LiveTrackingUpdater());
        Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(Environment.Instance().getTrackRecorder());
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == ActivityConstants.ACTIVITY_CHOOSE){
			if(resultCode == ActivityConstants.START_MAINSCREEN){
				Intent _currentActivity = new Intent(this, TrackerApp.class);
				startActivityForResult(_currentActivity, ActivityConstants.ACTIVITY_CHOOSE);
			}
			else if(resultCode == ActivityConstants.START_SETTINGSCREEN){
				Intent _currentActivity = new Intent(this, SettingsActivity.class);
				startActivityForResult(_currentActivity, ActivityConstants.ACTIVITY_CHOOSE);
			}
			else if(resultCode == ActivityConstants.START_TRACKSCREEN){
				Intent _currentActivity = new Intent(this, TrackListActivity.class);
				startActivityForResult(_currentActivity, ActivityConstants.ACTIVITY_CHOOSE);
			}			
		}
	}

}
