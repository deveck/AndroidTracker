package org.cw;


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
		Environment.Instance().AlertBuilderInstance().ShowInfoBox("Error: " + getMessage(), "Error", "OK");
	}
	
}
