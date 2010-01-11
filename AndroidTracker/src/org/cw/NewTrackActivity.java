package org.cw;

import org.cw.dataitems.TrackInformation;
import org.cw.utils.AlertBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewTrackActivity extends Activity {
	
	private EditText _textTrackname;
	private EditText _textTracksummary;
	private Button _btnOK;
	private Button _btnCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.newtrack);
		
		_textTrackname = (EditText)findViewById(R.id.textTrackname);
		_textTracksummary= (EditText)findViewById(R.id.textTrackdescription);
		_btnOK = (Button)findViewById(R.id.buttonTrackOk);
		_btnCancel = (Button)findViewById(R.id.buttonTrackCancel);
		
		_btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CreateNewTrack();								
				finish();
			}
		});
		
		_btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
	}
	
	private void CreateNewTrack(){
		if(_textTrackname.getText().length() == 0)
			_textTrackname.setText("Default Track");
		if(_textTracksummary.getText().length() == 0)
			_textTracksummary.setText("No further information ...");
		//TODO: why does Alert lead to exception???
		//AlertBuilder lala = new AlertBuilder(this);
		//lala.ShowInfoBox("test", "title", "buttonText");
		Environment.Instance().registerTrack(this, new TrackInformation(
				_textTrackname.getText().toString(), _textTracksummary.getText().toString()));
//		if(_textTrackname.gett)
//		Environment.Instance().registerTrack(this, new Tra)
//		
//		
//		if(Environment.Instance().getCurrentTrack() != null)
//		{
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//	    	alert.setTitle("Warning possible loose of data");
//	    	alert.setMessage("Store " + Environment.Instance().getCurrentTrack().getName() + " now?");
//	    	alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					Environment.Instance().getCurrentTrack().save();
//					
//				}
//			});
//	    	alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					
//					
//				}
//			});
//	    	    	
//    	
//	    	alert.show();
//	    	try{
//	    		alert.wait();
//	    	}
//	    	catch (Exception e) {
//	    		e.toString();
//				// TODO: handle exception
//			}
//		}
//		Environment.Instance().registerTrack(this, new TrackInformation(_textTrackname.getText().toString()));
	}

}
