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
	private Date _dateTime;
	
	public LocationIdentifier(Location location)
	{
		_location = location;
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
	
	public double getLongitudeDegrees()
	{
		return _location.getLongitude();
	}
	
	public double getLongitudeMinutes()
	{
		double degree = getLongitudeDegrees();
		
		return (double)((degree - (int)degree)) * 60.0;
	}
	
	public double getLongitudeSeconds()
	{
		double minutes = getLongitudeMinutes();
		
		return (double)((minutes - (int)minutes)) * 60.0;
	}
	
	public double getLatitudeDegrees()
	{
		return _location.getLatitude();
	}
	
	public double getLatitudeMinutes()
	{
		double degree = getLatitudeDegrees();
		
		return (double)((degree - (int)degree)) * 60.0;
	}
	
	public double getLatitudeSeconds()
	{
		double minutes = getLatitudeMinutes();
		
		return (double)((minutes - (int)minutes)) * 60.0;
	}
	
	public Location getLocation(){ return _location; }
	public Date getDateTime(){ return _dateTime; }
	public float getSpeed(){ return _speed; }
	public long getTime(){ return _dateTime.getTime(); }
	public double distanceTo(LocationIdentifier dest){ return _location.distanceTo(dest.getLocation()); }
}
