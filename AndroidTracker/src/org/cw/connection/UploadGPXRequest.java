package org.cw.connection;

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
			String gpxData
	)
	{
		_username = username;
		_pwHash = pwHash;
		_trackname = trackname;
		_gpxData = gpxData;
	}
	
	@Override
	public boolean Execute(CrossingWaysConnection conn) 
	{
		return false;
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
