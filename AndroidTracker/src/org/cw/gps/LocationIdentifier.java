package org.cw.gps;

import java.util.Date;

import android.location.Location;

/**
 * Contains information from a single location
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class LocationIdentifier 
{
	private Location _location;
	private Date _dateTime;
	
	public LocationIdentifier(Location location)
	{
		_dateTime = new Date();
		_location = location;
	}
	
	public Location getLocation(){ return _location; }
	public Date getDateTime(){ return _dateTime; }
	
}
