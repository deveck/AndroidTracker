package org.cw;

//import java.text.SimpleDateFormat;
//import java.util.Date;

import java.util.Date;

import org.cw.dataitems.TrackFile;
import org.cw.dataitems.TrackInformation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class NewTrackActivity extends Activity {

	private EditText _textTrackname;
	private EditText _textTracksummary;
	private Button _btnOK;
	private Button _btnCancel;
	private boolean _fresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newtrack);
		
		

		_textTrackname = (EditText) findViewById(R.id.textTrackname);
		_textTracksummary = (EditText) findViewById(R.id.textTrackdescription);
		_btnOK = (Button) findViewById(R.id.buttonTrackOk);
		_btnCancel = (Button) findViewById(R.id.buttonTrackCancel);

//		_textTrackname.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO maybe key must be forwarded?
//				// I do not understand that?
//				// different things happens if keyboard, touch-keybord, num-pad is used!
//				
//				if(_textTrackname.getText().length() > 0){
//					CheckForOverride();
//					_btnOK.setEnabled(true);
//				}
//				else
//					_btnOK.setEnabled(false);
//				return false;
//			}
//		});
		
		_btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CreateNewTrack();

			}
		});

		_btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				setResult(ActivityConstants.RES_CANCEL);
				finish();
			}
		});
		
		
		// Always set a default name, so that it is easy to start a new track, 
		// especially if you do not want to type...
		_fresh = true;
		//_textTrackname.setTextColor(android.R.color.darker_gray);
		_textTrackname.setText("My track on " + new Date().toLocaleString());
		_textTrackname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(_fresh == false)
					return;
				_fresh = false;
				_textTrackname.setText("");
			//	_textTrackname.setTextColor(android.R.color.black);
			}
		});
		_btnOK.setEnabled(true);
	}

	private void CreateNewTrack() 
	{
		if (_textTrackname.getText().length() == 0)
			return;
		if (_textTracksummary.getText().length() == 0)
			_textTracksummary.setText("No further information ...");
		
		TrackFile trackFile = TrackFile.CreateFromName(this, _textTrackname.getText().toString());
		
		if(trackFile.Exists())
			trackFile.DeleteMe();
		
		Environment.Instance().registerTrack(
				new TrackInformation(trackFile,
						_textTracksummary.getText().toString()));
		
		setResult(ActivityConstants.RES_OK);
		finish();
	}
	
	private void CheckForOverride() {
		if(TrackFile.fileExists(this.getBaseContext(), _textTrackname.getText().toString())){
			_btnOK.setText(R.string.textOverride);
		}
		else
			_btnOK.setText(R.string.textCreate);
	}

}
