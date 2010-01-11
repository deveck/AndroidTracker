package org.cw.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertBuilder 
{
	private Context _ctx;
	
	public AlertBuilder(Context ctx)
	{		
		_ctx = ctx;
	}
	
	public void ShowInfoBox(String message, String title, String buttonText)
	{
		new AlertDialog.Builder(_ctx)
			.setMessage(message)
			.setTitle(title)
			.setPositiveButton(buttonText, null)
			.show();
	}
	
//	public void ShowYesNoBox(String message, String title)
//	{
//		new AlertDialog.Builder(_ctx)
//			.setMessage(message)
//			.setTitle(title)
//			.setPositiveButton("YES", null)
//			.setNegativeButton("NO", null)
//			.show();
//	}
	
}
