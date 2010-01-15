package org.cw.connection;

/**
 * Server result code
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class ServerMessage 
{
	private int _statusCode;
	private String _message;
	
	public ServerMessage(String message)
	{
		String[] splittedMsg = message.split(" - ", 2);
		
		if(splittedMsg.length == 2)
		{
			try
			{
				_statusCode = Integer.parseInt(splittedMsg[0].trim());
			}
			catch(NumberFormatException e)
			{
				_statusCode = 0;
			}
		}
		else
		{
			_statusCode = 0;
			_message = message;
		}
	}
	
	public int getStatusCode(){ return _statusCode;}
	public String getMessage(){return _message;}
	
	public boolean IsError(){ return false; }
}
