package org.cw.gps;

import java.util.Vector;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Uses the android location manager
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class DefaultGPSProvider implements IGpsProvider, IGpsRecorder
{	
	private Context _ctx;

	/** The associated location manager */
	private LocationManager _locman;

	/** Contains all recorded GPS points */
	private Vector<LocationIdentifier> _gpsPoints = new Vector<LocationIdentifier>();
	
	/** Contains all registered status receivers */
	private Vector<IGpsStatusReceiver> _statusReceivers = new Vector<IGpsStatusReceiver>();
	
	private LocationListener _locationListener;
	
	public DefaultGPSProvider(Context ctx)
	{
		_ctx = ctx;
		
		_locman = (LocationManager)_ctx.getSystemService(Context.LOCATION_SERVICE);
		_locationListener = new MyLocationManager(this);
		_locman.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				0,
				0,
				_locationListener);
	}
	

	public LocationIdentifier GetLastKnownLocation()
	{
		if(_gpsPoints.size() > 0)
			return _gpsPoints.get(_gpsPoints.size() - 1);
		else
			return null;
	}
	
	public void AddGpsStatusReceiver(IGpsStatusReceiver statusReceiver)
	{
		_statusReceivers.add(statusReceiver);
	}
	
	@Override
	public void AddLocation(LocationIdentifier newLocation) 
	{
		_gpsPoints.add(newLocation);
		
		for(IGpsStatusReceiver recv : _statusReceivers)
			recv.LocationChanged(newLocation);
	}  
	
	private class MyLocationManager implements LocationListener 
    {
		private IGpsRecorder _gpsRecorder;
		
		public MyLocationManager(IGpsRecorder gpsRecorder)
		{
			_gpsRecorder = gpsRecorder;
		}
		
        @Override
        public void onLocationChanged(Location loc) {
        	_gpsRecorder.AddLocation(new LocationIdentifier(loc));        	
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

	
}
