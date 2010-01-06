package org.cw.connection;

import java.util.Vector;

import org.cw.CWException;
import org.cw.gps.LocationIdentifier;

/**
 * Encapsulates an asynchronous LogPosition Request
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class LogPositionRequest extends Request 
{
	/** Cleartext username */
	protected String _username;
	
	/** Hashed password */
	protected String _pwHash;
	
	/**	 */
	protected Integer _trackId;
	
	/** Log Message for crossing ways */
	protected String _message;
	
	/** Contains all locations to be transmitted to the other side
	 *  before the first call of TryMerge, this only contains one coordinate
	 */
	protected Vector<LocationIdentifier> _locations = new Vector<LocationIdentifier>();

	/** Contains the last Execution exception */
	protected Exception _lastError = null;
	
	public Exception getLastError(){ return _lastError; }
	
	public LogPositionRequest(
			String username, 
			String pwHash,
			Integer trackId, 
			String message, 
			LocationIdentifier... initialLocations)
	{
		_username = username;
		_pwHash = pwHash;
		_trackId = trackId;
		_message = message;
		
		for(LocationIdentifier loc:initialLocations)
			_locations.add(loc);		
		
	}
	
	@Override
	public boolean Execute(CrossingWaysConnection conn)
	{
		try 
		{
			conn.UpdateCurrentPosition(_username, 
					_pwHash,
					_trackId, 
					_message, 
					_locations.toArray(new LocationIdentifier[0])
					);
		} 
		catch (CWException e) 
		{
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
	
	/**
	 * Tries to integrate the locations from otherRequest into this Request
	 */
	@Override
	public boolean TryMerge(Request otherRequest) 
	{
		if(otherRequest.getClass() == this.getClass())
		{
			LogPositionRequest otherLogRequest = (LogPositionRequest)otherRequest;
			if(otherLogRequest._username.equals(_username) &&
			   otherLogRequest._pwHash.equals(_pwHash) &&
			   otherLogRequest._trackId.equals(_trackId) &&
			   otherLogRequest._message.equals(_message))
			{
				for(LocationIdentifier loc:otherLogRequest._locations)
					_locations.add(loc);
				
				return true;
			}
			   
		}
		
		return false;
	}

}
