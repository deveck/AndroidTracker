package org.cw.dataitems;

import org.cw.gps.IGpsRecorder;

import org.cw.gps.LocationIdentifier;

public class TrackStatistics implements IGpsRecorder {
	
	private double _averagespeed = 0;
	private double _distance = 0;
	private double _maxalt = 0;
	private double _minalt = 0;
	private double _minlat = 0;
	private double _maxlat = 0;
	private double _minlon = 0;
	private double _maxlon = 0;
	private long _timems = 0;
	private int _numlocations = 0;
	private LocationIdentifier _lastlocation;
	private String _name;
	
	@Override
	public String toString(){ return "Statistics for tour: " + _name; }
	public double getMinAlt(){ return _minalt; }
	public double getMaxAlt(){ return _maxalt; }
	public double getMinLon(){ return _minlon; }
	public double getMaxLon(){ return _maxlon; }
	public double getMinLat(){ return _minlat; }
	public double getMaxLat(){ return _maxlat; }
	public double getDistance(){ return _distance; }
	public double getTimems(){ return _timems; }
	public double getAverageSpeed() { return _averagespeed; }
	
	private TrackStatistics(){}
	public TrackStatistics(String name){
		this();
		_name = name;
	}

	@Override
	public void AddLocation(LocationIdentifier newLocation) {
		if(_numlocations == 0){
			_maxalt = newLocation.getLocation().getAltitude();
			_minalt = newLocation.getLocation().getAltitude();
			_maxlon = newLocation.getLocation().getLongitude();
			_minlon = newLocation.getLocation().getLongitude();
			_maxlat = newLocation.getLocation().getLatitude();
			_minlat = newLocation.getLocation().getLatitude();
		}
		else
		{
			_maxalt = Math.max(_maxalt, newLocation.getLocation().getAltitude());
			_minalt = Math.min(_minalt, newLocation.getLocation().getAltitude());
			_maxlon = Math.max(_maxlon, newLocation.getLocation().getLongitude());
			_minlon = Math.min(_minlon, newLocation.getLocation().getLongitude());
			_maxlat = Math.max(_maxlat, newLocation.getLocation().getLatitude());
			_minlat = Math.min(_minlat, newLocation.getLocation().getLatitude());
			
			_distance += newLocation.distanceTo(_lastlocation);
			_timems += newLocation.getTime() - _lastlocation.getTime();
			_averagespeed = _distance / _timems * 3600.0;
		}
		_numlocations++;
		_lastlocation = newLocation;
	}
}
