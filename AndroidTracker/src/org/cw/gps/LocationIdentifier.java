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
	private float _speed;
	private double _height;
	
	public LocationIdentifier(Location location)
	{
		_dateTime = new Date();
		_location = location;
		
		//To be tested
		_height = location.getAltitude();
	}

	public void CalculateSpeed(LocationIdentifier previousLocation)
	{
		if(_location.hasSpeed())
			_speed = _location.getSpeed();
		else
		{
			double diff = (double)(_dateTime.getTime() - previousLocation.getDateTime().getTime());
			double distance = _location.distanceTo(previousLocation.getLocation());
			_speed = (float) (distance/diff * 3600.0) ;
		}
		
	}
	
	public Location getLocation(){ return _location; }
	public Date getDateTime(){ return _dateTime; }
	public float getSpeed(){ return _speed; }
	
}
