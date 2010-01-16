package org.cw.dataitems;

import org.cw.CWException;
import org.cw.gps.IGpsRecorder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Vector;

import org.cw.gps.LocationIdentifier;
import org.kobjects.isodate.IsoDate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.location.Location;
import android.util.Xml;

public class TrackInformation implements IGpsRecorder {
	
	private static final int MAX_UNSAVED_TRACKPOINTS = 1;
	
	public static TrackInformation CreateFromTrackFile(TrackFile file)
	{
		return new TrackInformation(file, "");
	}
	
	private String _name;
	private String _summary;
	private Vector<LocationIdentifier> _locations;
	private TrackFile _data;
	private Date _creationdate;
	private TrackStatistics _statistics;
	private TrackTime _trackTime;
	
	private int _unsavedTrackPoints = 0;
	
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
		
		if(file.Exists())
		{
			//Load the existing data
			load();
		}
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
		
		_unsavedTrackPoints ++;
		
		if(_unsavedTrackPoints >= MAX_UNSAVED_TRACKPOINTS)
			save();
	}
	
	/**
	 * Every Track has 2 files,
	 * .xml for track information and
	 * .stat for statistics
	 * @throws CWException 
	 */
	public void save() 
	{
		XmlSerializer trackXmlOutput = Xml.newSerializer();
			
		
		try
		{
			OutputStream output = _data.openOuputTrackFile();
			Writer trackOutput = new OutputStreamWriter(output, "UTF-8");
			
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
			
			trackXmlOutput.flush();
			trackOutput.flush();
			trackOutput.close();
			output.flush();			
			output.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			new CWException(e.getMessage()).ShowAlertDialog();
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
	

	/**
	 * Loads the track points saved in the xml file
	 */
	public void load()
	{ 
		try 
		{
			XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
			xmlParser.setInput(_data.openInputTrackFile(), "UTF-8");
			
			//Parse gpx start Tag
			int xmlLevel = 0;
			if(xmlParser.nextTag() != XmlPullParser.START_TAG)
				throw new XmlPullParserException("Unexpected tag");
			xmlLevel++;
			
			//Once the Root tag is closed, nothing more can be in the xml file
			while(xmlLevel > 0)
			{
				int tagResult = xmlParser.nextTag();
				if(tagResult == XmlPullParser.START_TAG)
					xmlLevel++;
				else// if(tagResult == XmlPullParser.END_TAG)
					xmlLevel--;
				
				if(xmlLevel == 2 && xmlParser.getName().equals("metadata"))
				{
					ParseMetadata(xmlParser);
					//metadata end tag is consumed inside
					xmlLevel--;
				}
				else if(xmlLevel == 2 && xmlParser.getName().equals("trk"))
				{
					ParseTrackPoints(xmlParser);
					//trk end tag is consumed inside
					xmlLevel--;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			new CWException(e.getMessage()).ShowAlertDialog();
		}
		
	}
	
	private void ParseMetadata(XmlPullParser xmlParser) throws XmlPullParserException, IOException
	{
		//Start Tag is parsed outside
		
		while(xmlParser.nextTag() == XmlPullParser.START_TAG)
		{
			
			
			if(xmlParser.getName().equals("desc"))
				_summary = xmlParser.nextText();
			else if(xmlParser.getName().equals("name"))
				_name = xmlParser.nextText();	
			else if(xmlParser.getName().equals("time"))
				_creationdate = IsoDate.stringToDate(xmlParser.nextText(), IsoDate.DATE_TIME);
			else
				xmlParser.nextText();
			//Bounds tag is ignored because the max and min values get reconstructed from the stat file
			
			//Parse end tags
			//if(xmlParser.nextTag() != XmlPullParser.END_TAG)
			//	throw new XmlPullParserException("Unexpected tag");
		}
	}	
	
	private void ParseTrackPoints(XmlPullParser xmlParser) throws XmlPullParserException, IOException
	{
		//Start Tag is parsed outside
		
		while(xmlParser.nextTag() == XmlPullParser.START_TAG)
		{
			if(xmlParser.getName().equals("name"))
				_name = xmlParser.nextText();		
			else if(xmlParser.getName().equals("trkpt"))
				ParseTrackPoint(xmlParser);
			else if(xmlParser.getName().equals("trkseg"))
			{
				System.out.println("huhu");
				//Do Nothing!
			}
			else
				xmlParser.nextText();
			
			//Parse end tags
			//if(xmlParser.nextTag() != XmlPullParser.END_TAG)
			//	throw new XmlPullParserException("Unexpected tag");
		}
	}
	
	private void ParseTrackPoint(XmlPullParser xmlParser) throws XmlPullParserException, IOException
	{
		//Start Tag is parsed outside
		double latitude = Double.parseDouble(xmlParser.getAttributeValue("", "lat"));
		double longitude = Double.parseDouble(xmlParser.getAttributeValue("", "lon"));
		double altitude = 0;
		Date time = new Date();
		
		while(xmlParser.nextTag() == XmlPullParser.START_TAG)
		{
			if(xmlParser.getName().equals("ele"))
				altitude = Double.parseDouble(xmlParser.nextText());		
			else if(xmlParser.getName().equals("time"))
				time = IsoDate.stringToDate(xmlParser.nextText(), IsoDate.DATE_TIME);
			else
				xmlParser.nextTag();
			//Parse end tags
			//if(xmlParser.nextTag() != XmlPullParser.END_TAG)
			//	throw new XmlPullParserException("Unexpected tag");
		}
		
		Location newLocation = new Location("gps");
		newLocation.setAltitude(altitude);
		newLocation.setLatitude(latitude);
		newLocation.setLongitude(longitude);
		newLocation.setTime(time.getTime());
		_locations.add(new LocationIdentifier(newLocation));
		
	}
}
