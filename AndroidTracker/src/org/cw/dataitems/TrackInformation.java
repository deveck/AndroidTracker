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
	private File _gpx;
	private File _stat;
	private Date _creationdate;
	private TrackStatistics _statistics;
	
	private TrackInformation(){
		_locations = new Vector<LocationIdentifier>();
		_creationdate = new Date();
	}
	public TrackInformation(String name, String summary){
		this();
		_summary = summary;
		_name = name;
		_statistics = new TrackStatistics(_name);
	}
	
	public String getName(){ return _name; }
	public String getSummary(){	return _summary; }
	public Date getCreationDate(){ return _creationdate; }
	public Vector<LocationIdentifier> getLocations(){ return _locations; }
	public TrackStatistics getStatistics(){ return _statistics; }
	
	@Override
	public void AddLocation(LocationIdentifier newLocation) {
		_locations.add(newLocation);
		_statistics.AddLocation(newLocation);	
	}
	
	//public void loadFile(){
//		
//	}
	
	public void save(){
		//TODO: Store Track into File		
	}
	
	public static void loadTrack(String name){
		//TODO: Load Track from file
	}
}
