package org.cw.gps;

/**
 * Implemented by classes that provide GPS data to the application
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public interface IGpsProvider 
{
	LocationIdentifier GetLastKnownLocation();
	
	void AddGpsStatusReceiver(IGpsStatusReceiver statusReceiver);
}
