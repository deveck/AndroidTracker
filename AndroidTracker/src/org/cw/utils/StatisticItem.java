package org.cw.utils;

import java.security.acl.LastOwnerException;
import java.util.*;

import org.cw.gps.LocationIdentifier;

import android.text.format.DateFormat;

public class StatisticItem {
	
	double _average_speed = 0;
	double _distance = 0;
	Date _time = null;
	int _recorded_locs = 0;
	LocationIdentifier _last_loc = null;

	String _name = "Empty Object";
	
	//LocationIdentifier _lastlocation = null;
	//LocationIdentifier _num
	
	@Override
	public String toString(){return _name;}
	
	private StatisticItem(){}
	
	public StatisticItem(String tourname, Vector<LocationIdentifier> locs){
		
		if(locs == null || locs.isEmpty())
			throw new IllegalArgumentException("Got no locations");
		
		if(tourname == null)
			throw new IllegalArgumentException("Tourname must be given");
		
		Calendar cal = new GregorianCalendar();
		_name = "Statistics for " + tourname + " @" + cal.getTime().toString();
		_recorded_locs = locs.size();
		cal.setTimeInMillis(locs.lastElement().getDateTime().getTime() - locs.firstElement().getDateTime().getTime());
		_time = cal.getTime();
		
		for(LocationIdentifier loc : locs){
			if(_last_loc == null){
				_last_loc = loc;
				continue;}
			_distance += loc.getLocation().distanceTo(_last_loc.getLocation());			
		}
		
		_average_speed = _distance / _time.getTime() * 3600.0;
		}
	
	public StatisticItem(String tourname){
		if(tourname == null)
			throw new IllegalArgumentException("Tourname must be given");
		
		Calendar cal = new GregorianCalendar();
		_name = "Statistics for " + tourname + " @" + cal.getTime().toString();
	}
	
	public void AddNext(LocationIdentifier loc){
		if(loc == null)
			throw new IllegalArgumentException("Got no location");
		
		_distance += loc.getLocation().distanceTo(_last_loc.getLocation());
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(_time.getTime() +  (loc.getDateTime().getTime() - _last_loc.getDateTime().getTime()));
		_time = cal.getTime();
		
		_average_speed = _distance / _time.getTime() * 3600.0; 
	}
	
	//TODO: Getter for distance and speed (km, miles)
	
	
//	public void updateStatistic(LocationIdentifier loc){
//		if(!_run)
//			return;
//		
//		if(_lastlocation){
//			
//		}
//		_lastlocation = loc;
//	}
//	
//	public void Start(){_run=true}
//	public void Stop(){_run=false}
//	public boolean isActive(){return _run}
//	
}
