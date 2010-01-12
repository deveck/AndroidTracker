package org.cw.gps;

/**
 * Implemented by classes that provide methods for saving 
 * received gps updates
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 * 
 *
 */
public interface IGpsRecorder 
{
	void AddLocation(LocationIdentifier newLocation);
} 
