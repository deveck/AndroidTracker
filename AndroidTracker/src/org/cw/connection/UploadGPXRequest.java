package org.cw.connection;

import org.cw.CWException;
import org.cw.CWWithCallbackInfoException;

/**
 * Represents the request to upload gpx data
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class UploadGPXRequest extends Request 
{
	/** Cleartext username */
	protected String _username;
	
	/** Hashed password */
	protected String _pwHash;
	
	/**	Trackname on CW */
	protected String _trackname;
	
	/** XML Gpx data, as string because correct conversion (including encoding selection ) should be done outside */
	protected String _gpxData;
	
	public String getUsername(){return _username;}
	public String getPwHash(){return _pwHash;}
	public String getTrackname(){ return _trackname;}
	public String getGPXData(){ return _gpxData;}
	
	
	public UploadGPXRequest(
			String username,
			String pwHash,
			String trackname,
			String gpxData,
			IUiCallback callback
	)
	{
		_username = username;
		_pwHash = pwHash;
		_trackname = trackname;
		_gpxData = gpxData;
		_callback = callback;
	}
	
	@Override
	public boolean Execute(CrossingWaysConnection conn) 
	{
		try
		{ 
			if(conn.UploadGpx(this).IsError())
				return false;
		}
		catch(CWWithCallbackInfoException e)
		{
			if(e.getCallbackInfo().getProgressInfo().getRequeue() == false)
				return true;
			return false;
		}
		catch(CWException e)
		{
			return false;
		}
		
		return true;
	}

	/**
	 * GPX Request is not mergeable, always returns false
	 */
	@Override
	public boolean TryMerge(Request otherRequest) 
	{
		return false;
	}

}
