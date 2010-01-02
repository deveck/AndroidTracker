package org.cw.gps;

/**
 * Implemented by classes to register at the IGpsProvider to receive
 * location updates
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public interface IGpsStatusReceiver 
{
	void LocationChanged(LocationIdentifier newLocation);
}
