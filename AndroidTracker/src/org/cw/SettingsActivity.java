package org.cw;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends Activity 
{
	private EditText _textUsername;
	private EditText _textPassword;
	private EditText _textCommitInterval;
	private ImageButton _buttonVerifyCredentials;
	private TextView _labelVerifyCredentials;
	private boolean _verified = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		
		_textUsername = (EditText)findViewById(R.id.textUsername);
		_textPassword = (EditText)findViewById(R.id.textPassword);
		_textCommitInterval = (EditText)findViewById(R.id.textCommitInterval);
		_buttonVerifyCredentials = (ImageButton)findViewById(R.id.buttonVerifyCredentials);
		_labelVerifyCredentials = (TextView)findViewById(R.id.labelVerifyCredentials);
		
		// Load settings
		_textUsername.setText(Environment.Instance().Settings().getUsername(""));
        _textPassword.setText(Environment.Instance().Settings().getPassword(""));      
        _textCommitInterval.setText(new Integer((Environment.Instance().Settings().getLiveTrackerCommitInterval()/1000)).toString());
        
        this.SetVerifiedState(true);
        
        _textUsername.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				SetVerifiedState(false);
				return false;
			}
		});
        
        ((Button)findViewById(R.id.buttonOk)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnSave_Clicked();				
			}
		});
        
        _buttonVerifyCredentials.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnVerifyCredentials_Clicked();				
			}
		});
        
    }
	
	/**
	 * Sets the verified state of the user credentials
	 * @param verified
	 */
	private void SetVerifiedState(boolean verified)
	{
		_verified = verified;
		if(verified)
		{
			this.
			_labelVerifyCredentials.setText(R.string.CredentialsVerified);
			_buttonVerifyCredentials.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));
		}
		else
		{
			_labelVerifyCredentials.setText(R.string.CredentialsNotVerified);
			_buttonVerifyCredentials.setImageDrawable(getResources().getDrawable(R.drawable.cross));
		}
	}
	
	/** Called on save button click,
	 * Save all settings here, if they are verified
	 */
	private void OnSave_Clicked()
	{
		if(_verified)
		{
			Environment.Instance().Settings().setUsername(_textUsername.getText().toString());
			Environment.Instance().Settings().setPassword(_textPassword.getText().toString());
		}
		try
		{
			Integer commitInterval = Integer.parseInt(_textCommitInterval.getText().toString()) * 1000;
			Environment.Instance().Settings().setLiveTrackerCommitInterval(commitInterval);
		}
		catch(NumberFormatException e)
		{
		}
		finish();
	}
	
	private void OnVerifyCredentials_Clicked()
	{
		try 
		{
			if(Environment.Instance().ConnectionInstance().VerifyCredentials(
					new UserCredentials(_textUsername.getText().toString(),
							_textPassword.getText().toString())))
			{
				this.SetVerifiedState(true);
				return;
			}
			
			this.SetVerifiedState(false);
		} catch (CWException e) {
			e.printStackTrace();
			e.ShowAlertDialog();
			this.SetVerifiedState(false);
		}
	}
}
