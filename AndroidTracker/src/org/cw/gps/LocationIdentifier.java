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
	private float _speed;
	private double _height;
	private Date _dateTime;
	
	public LocationIdentifier(Location location)
	{
		_location = location;
		
		//To be tested
		_height = location.getAltitude();
		
		_dateTime = new Date(location.getTime()); 
	}

	public void CalculateSpeed(LocationIdentifier previousLocation)
	{
		if(_location.hasSpeed())
			_speed = _location.getSpeed();
		else
		{
			double diff = (double)(getDateTime().getTime() - previousLocation.getDateTime().getTime());
			double distance = _location.distanceTo(previousLocation.getLocation());
			_speed = (float) (distance/diff * 3600.0) ;
		}
		
	}
	
	public Location getLocation(){ return _location; }
	public Date getDateTime(){ return _dateTime; }
	public float getSpeed(){ return _speed; }
	
}
