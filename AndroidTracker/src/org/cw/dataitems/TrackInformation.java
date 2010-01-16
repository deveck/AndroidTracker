package org.cw.dataitems;

import org.cw.gps.IGpsRecorder;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import org.cw.gps.LocationIdentifier;

public class TrackInformation implements IGpsRecorder {
	
	private String _name;
	private String _summary;
	private Vector<LocationIdentifier> _locations;
	private TrackFile _data;
	private Date _creationdate;
	private TrackStatistics _statistics;
	private TrackTime _trackTime;
	
	private TrackInformation(){
		_locations = new Vector<LocationIdentifier>();
		_creationdate = new Date();
		_trackTime = new TrackTime(0);
		
	}
	public TrackInformation(TrackFile data, String name, String summary){
		this();
		_summary = summary;
		_name = name;
		_statistics = new TrackStatistics(_name);
		_trackTime = new TrackTime(0);
		_data = data;
	}
	
	public String getName(){ return _name; }
	public String getSummary(){	return _summary; }
	public Date getCreationDate(){ return _creationdate; }
	public Vector<LocationIdentifier> getLocations(){ return _locations; }
	public TrackStatistics getStatistics(){ return _statistics; }
	public TrackTime getTrackTime(){ return _trackTime;}
	
	@Override
	public void AddLocation(LocationIdentifier newLocation) {
		_locations.add(newLocation);
		_statistics.AddLocation(newLocation);	
	}
	
	public void save()
	{
				
	}
	
	public static TrackInformation CreateFromTrackFile(String name)
	{
		return null;
	}
}
