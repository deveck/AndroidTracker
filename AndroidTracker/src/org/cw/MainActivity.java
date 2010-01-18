package org.cw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public abstract class MainActivity extends Activity {
	
	protected LinearLayout _mainView;
	private ImageButton _btnSettings;
	private ImageButton _btnTracks;
	private ImageButton _btnRecord;
	private LayoutInflater _inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background);
		_inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_mainView = (LinearLayout) findViewById(R.id.mainLayout);
		_btnSettings = (ImageButton)findViewById(R.id.buttonSettings);
		_btnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onButtonSettings_clicked();
			}

		
		});
		
		_btnRecord = (ImageButton)findViewById(R.id.buttonTracks);
		_btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onButtonTracks_clicked();			
			}
		});
		
		_btnTracks = (ImageButton)findViewById(R.id.buttonRecord);
		_btnTracks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onButtonRecord_clicked();
			}
		});
	}
	
	protected void onButtonSettings_clicked() {
		if(Environment.Instance().getCurrentActivity() == ActivityConstants.START_SETTINGSCREEN);
		else{
			setResult(ActivityConstants.START_SETTINGSCREEN);
			finish();
		}
	}
	protected void onButtonTracks_clicked() {
		if(Environment.Instance().getCurrentActivity() == ActivityConstants.START_TRACKSCREEN);
		else{
			setResult(ActivityConstants.START_TRACKSCREEN);
			finish();
		}
		//_mainView.removeAllViews();
		//_mainView.addView(_inflater.inflate(R.layout.settings, null));
	//	startActivity(new Intent(this,SettingsActivity.class));
		//_inflater.inflate(R.layout.settings, _mainView);
			
		}
	protected void onButtonRecord_clicked() {
		if(Environment.Instance().getCurrentActivity() == ActivityConstants.START_MAINSCREEN);
		else{
			setResult(ActivityConstants.START_MAINSCREEN);
			finish();
		}
		//_mainView.removeAllViews();
		//_mainView.addView(_inflater.inflate(R.layout.settings, null));
	//	startActivity(new Intent(this,SettingsActivity.class));
		//_inflater.inflate(R.layout.settings, _mainView);
			
		}
	
	protected final void setViewContent(int activityChoose) {
		//_mainView = (LinearLayout) findViewById(R.id.mainLayout);
		Environment.Instance().setCurrentActivity(activityChoose);
		_mainView.removeAllViews();
		switch(activityChoose){
		case ActivityConstants.START_MAINSCREEN:
			_mainView.addView(_inflater.inflate(R.layout.main, null));
			break;
		case ActivityConstants.START_SETTINGSCREEN:
			_mainView.addView(_inflater.inflate(R.layout.settings, null));
			break;
		case ActivityConstants.START_TRACKSCREEN:
			_mainView.addView(_inflater.inflate(R.layout.tracklist, null));
			break;
		}
	}
	
/*	protected final void setCurrentActivity(int currentActivity) {
		_thisActivity = currentActivity;
		Environment.Instance().setCurrentActivity(currentActivity); 
	}*/
	
	
}
