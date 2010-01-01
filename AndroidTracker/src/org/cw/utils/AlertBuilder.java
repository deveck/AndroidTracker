package org.cw.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertBuilder 
{
	private static AlertBuilder _instance = null;
	
	
	public static final AlertBuilder Instance()
	{
		return _instance;
	}
	
	public static final void CreateInstance(Context ctx)
	{
		_instance = new AlertBuilder(ctx);
	}
	
	private Context _ctx;
	
	private AlertBuilder(Context ctx)
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
	
}
