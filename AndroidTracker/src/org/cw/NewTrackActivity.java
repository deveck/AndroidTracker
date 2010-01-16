package org.cw;

import org.cw.dataitems.TrackFile;
import org.cw.dataitems.TrackInformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class NewTrackActivity extends Activity {

	private EditText _textTrackname;
	private EditText _textTracksummary;
	private Button _btnOK;
	private Button _btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newtrack);

		_textTrackname = (EditText) findViewById(R.id.textTrackname);
		_textTracksummary = (EditText) findViewById(R.id.textTrackdescription);
		_btnOK = (Button) findViewById(R.id.buttonTrackOk);
		_btnCancel = (Button) findViewById(R.id.buttonTrackCancel);

		_textTrackname.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO maybe key must be forwarded?
				// I do not understand that?
				// different things happens if keyboard, touch-keybord, num-pad is used!
				
				if(_textTrackname.getText().length() > 0){
					CheckForOverride();
					_btnOK.setEnabled(true);
				}
				else
					_btnOK.setEnabled(false);
				return true;
			}
		});
		
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
	}

	private void CreateNewTrack() 
	{
		if (_textTrackname.getText().length() == 0)
			return;
		if (_textTracksummary.getText().length() == 0)
			_textTracksummary.setText("No further information ...");
		Environment.Instance().registerTrack(
				new TrackInformation(new TrackFile(this.getBaseContext(), _textTrackname.getText().toString()), _textTrackname.getText().toString(),
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
