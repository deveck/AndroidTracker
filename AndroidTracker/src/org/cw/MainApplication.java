package org.cw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainApplication extends Activity {
	
	private LinearLayout _mainView;
	private View _mainScreen;
	private View _trackScreen;
	private View _settingsScreen;
	//private 
	private ImageButton _btnSettings;
	private ImageButton _btnTracks;
	private ImageButton _btnRecord;
	
	private LayoutInflater _inflater;
	private Intent _currentActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent _currentActivity = new Intent(this, TrackerApp.class);
		startActivityForResult(_currentActivity, ActivityConstants.ACTIVITY_CHOOSE);
		
      Environment.Instance().CreateSettings(this);
      Environment.Instance().CreateAlertBuilderInstance(this);
      Environment.Instance().CreateDefaultGPSProvider(this);
   //   Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(this);
   //   Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(new LiveTrackingUpdater());
   //   Environment.Instance().GPSProviderInstance().AddGpsStatusReceiver(_trackRecorder);
		//setContentView(R.layout.background);
		
		//_inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated method stub
		//TextView ll = new TextView(this);
		//ll.setText("hallo");
		//_mainView = (LinearLayout) findViewById(R.id.mainLayout);
		//_inflater = ;
		//_mainScreen = _inflater.inflate(R.layout.main, null);
		//_trackScreen = _inflater.inflate(R.layout.tracklist, null);
		//_settingsScreen = _inflater.inflate(R.layout.settings, null);
		
//		_btnSettings = (ImageButton)findViewById(R.id.buttonSettings);
//		_btnSettings.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				onButtonSettings_clicked();
//			}
//
//		
//		});
		
//		_btnRecord = (ImageButton)findViewById(R.id.buttonTracks);
//		_btnRecord.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				_mainView.removeAllViews();
//				_mainView.addView(_inflater.inflate(R.layout.tracklist, null));				
//			}
//		});
//		
//		_btnTracks = (ImageButton)findViewById(R.id.buttonRecord);
//		_btnTracks.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				_mainView.removeAllViews();
//				_mainView.addView(_inflater.inflate(R.layout.main, null));
//			}
//		});
//		
//	//	View currentView = R.layout. (R.layout.test);
//		
//		//_mainView.addView(_inflater.inflate(R.layout.main, null));
//		//_mainView.addView(TrackerApp.class);
//		_currentActivity = new Intent(_mainView.getContext(), TrackerApp.class);
//		//_currentActivity.
//		//Activity ac = new Activity();
//		//ac.start
//	//	ac.setContentView(findViewById(R.id.mainLayout));
//		//ac.startActivity(_currentActivity);
//		//_currentActivity.get
//		//startActivity(_currentActivity);
		
		
	}
		private void onButtonSettings_clicked() {
			_mainView.removeAllViews();
			_mainView.addView(_inflater.inflate(R.layout.settings, null));
		//	startActivity(new Intent(this,SettingsActivity.class));
			//_inflater.inflate(R.layout.settings, _mainView);
				
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
