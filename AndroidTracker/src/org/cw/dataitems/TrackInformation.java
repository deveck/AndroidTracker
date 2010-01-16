package org.cw.dataitems;

import org.cw.CWException;
import org.cw.gps.IGpsRecorder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Vector;

import org.cw.gps.LocationIdentifier;
import org.kobjects.isodate.IsoDate;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class TrackInformation implements IGpsRecorder {
	
	public static TrackInformation CreateFromTrackFile(String name)
	{
		return null;
	}
	
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
	
	public TrackInformation(TrackFile file, String summary){
		this();
		_summary = summary;
		_name = file.toString();
		_statistics = new TrackStatistics(_name);
		_trackTime = new TrackTime(0);
		_data = file;
	}
	
	public String getName(){ return _name; }
	public String getSummary(){	return _summary; }
	public Date getCreationDate(){ return _creationdate; }
	public Vector<LocationIdentifier> getLocations(){ return _locations; }
	public TrackStatistics getStatistics(){ return _statistics; }
	public TrackTime getTrackTime(){ return _trackTime;}
	public TrackFile getFile(){ return _data; }
	
	@Override
	public void AddLocation(LocationIdentifier newLocation) {
		_locations.add(newLocation);
		_statistics.AddLocation(newLocation);	
	}
	
	/**
	 * Every Track has 2 files,
	 * .xml for track information and
	 * .stat for statistics
	 * @throws CWException 
	 */
	public void save() throws CWException
	{
		XmlSerializer trackXmlOutput = Xml.newSerializer();
		StringWriter trackOutput = new StringWriter();
		
		try
		{
			trackXmlOutput.setOutput(trackOutput);
			trackXmlOutput.startDocument("UTF-8", true);
			trackXmlOutput.startTag("", "gpx");
			trackXmlOutput.attribute("", "xmlns", "http://www.topografix.com/GPX/1/1");
			trackXmlOutput.attribute("", "creator", "AndroidTracker");
			trackXmlOutput.attribute("", "version", "1.1");
			trackXmlOutput.attribute("xsi", "schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
			trackXmlOutput.attribute("xmlns", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
			
			SerializeMetadata(trackXmlOutput);
			SerializeTrackPoints(trackXmlOutput);
			
			trackXmlOutput.endTag("", "gpx");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CWException(e.getMessage());
		}
		 
	}
	
	private void SerializeMetadata(XmlSerializer xmlOutput) throws 
		IllegalArgumentException, 
		IllegalStateException, 
		IOException
	{
		//Insert <metadata> tag
		xmlOutput.startTag("", "metadata");
		
		//In our case name of the gpx file is always the same than name of the track
		//because there is always only one track in the file
		xmlOutput.startTag("", "name");
		xmlOutput.text(_name);
		xmlOutput.endTag("", "name");
		
		xmlOutput.startTag("", "desc");
		xmlOutput.text(_summary);
		xmlOutput.endTag("", "desc");
		
		xmlOutput.startTag("", "author");
		xmlOutput.text("Android Tracker");
		xmlOutput.endTag("", "author");
		
		//Insert time
		xmlOutput.startTag("", "time");
		xmlOutput.text(IsoDate.dateToString(_creationdate, IsoDate.DATE_TIME));
		xmlOutput.endTag("", "time");
		
		//Insert bounds
		xmlOutput.startTag("", "bounds");
		xmlOutput.attribute("", "maxlat", String.format("%.9f", _statistics.getMaxLat()));
		xmlOutput.attribute("", "minlat", String.format("%.9f", _statistics.getMinLat()));
		xmlOutput.attribute("", "maxlon", String.format("%.9f", _statistics.getMaxLon()));
		xmlOutput.attribute("", "minlon", String.format("%.9f", _statistics.getMinLon()));
		xmlOutput.endTag("", "bounds"); 
		
		//Insert </metadata> tag
		xmlOutput.endTag("", "metadata");
	}
	
	private void SerializeTrackPoints(XmlSerializer xmlOutput) throws 
		IllegalArgumentException, 
		IllegalStateException, 
		IOException
	{
		//Insert <trk> tag
		xmlOutput.startTag("", "trk");
		
		xmlOutput.startTag("", "name");
		xmlOutput.text(_name);
		xmlOutput.endTag("", "name");
		
		
		
		//Insert <trkseg> which holds the track point informations
		xmlOutput.startTag("", "trkseg");
		
		for(LocationIdentifier loc : _locations)
			SerializeTrackPoint(xmlOutput, loc);
		
		//Insert </trkseg>
		xmlOutput.endTag("", "trkseg");
		
		//Insert </trk> tag
		xmlOutput.endTag("", "trk");
	}
	
	private void SerializeTrackPoint(XmlSerializer xmlOutput, LocationIdentifier loc) throws 
		IllegalArgumentException, 
		IllegalStateException, 
		IOException
	{
		xmlOutput.startTag("", "trkpt");
		xmlOutput.attribute("", "lat", String.format("%.9f", loc.getLatitudeDegrees()));
		xmlOutput.attribute("", "lon", String.format("%.9f", loc.getLongitudeDegrees()));
		
		xmlOutput.startTag("", "ele");
		xmlOutput.text(String.format("%.2f", loc.getLocation().getAltitude()));
		xmlOutput.endTag("", "ele");
		
		xmlOutput.startTag("", "time");
		xmlOutput.text(IsoDate.dateToString(loc.getDateTime(), IsoDate.DATE_TIME));
		xmlOutput.endTag("", "time");
		xmlOutput.endTag("", "trkpt");
	}
	
	
}
