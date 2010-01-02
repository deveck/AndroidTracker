package org.cw;

/**
 * Combines the user credentials, for easyer management
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class UserCredentials 
{
	private String _username;
	private String _password;

	public UserCredentials(String username, String password)
	{
		_username = username;
		_password = password;
	}
	
	public String getUsername()
	{
		return _username;
	}
	
	public void setUsername(String value)
	{
		_username = value;
	}
	
	public String getPassword()
	{
		return _password;
	}
	
	public void setPassword(String value)
	{
		_password = value;
	}
}
