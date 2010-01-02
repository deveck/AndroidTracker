package org.cw;

import org.cw.connection.CrossingWaysConnection;
import org.cw.gps.DefaultGPSProvider;
import org.cw.gps.IGpsProvider;
import org.cw.utils.AlertBuilder;

import android.content.Context;

/**
 * Defines the environment for the tracker application 
 * which is available to all parts of the program 
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class Environment 
{
	private static Environment _environment = null;	
	public static final Environment Instance()
	{
		if(_environment == null)
			_environment = new Environment();
		
		return _environment;
	}
	
	
	private AlertBuilder _alertBuilder = null;	
	public void CreateAlertBuilderInstance(Context ctx){
		_alertBuilder = new AlertBuilder(ctx);
	}	
	public AlertBuilder AlertBuilderInstance(){
		return _alertBuilder;
	}
	
	private CrossingWaysConnection _connectionInstance = null;	
	public CrossingWaysConnection ConnectionInstance()
	{
		if(_connectionInstance == null)
			_connectionInstance = new CrossingWaysConnection();
		
		return _connectionInstance;
	}
	
	private IGpsProvider _gpsProvider = null;
	public  void CreateDefaultGPSProvider(Context ctx){
		_gpsProvider = new DefaultGPSProvider(ctx);
		
	}
	public IGpsProvider GPSProviderInstance(){
		return _gpsProvider;
	}
	
}
