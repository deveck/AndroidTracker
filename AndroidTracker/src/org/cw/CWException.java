package org.cw;

import org.cw.utils.AlertBuilder;

public class CWException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2731962264992590333L;

	public CWException(String message)
	{
		super(message);	
	}
	
	public void ShowAlertDialog()
	{
		AlertBuilder.Instance().ShowInfoBox("Error: " + getMessage(), "Error", "OK");
	}
	
}
