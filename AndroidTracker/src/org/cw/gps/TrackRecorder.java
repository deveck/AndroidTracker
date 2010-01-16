package org.cw.gps;

import org.cw.Environment;

/**
 * Updates the currently selected track information with the most recent 
 * gps data
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackRecorder implements IGpsStatusReceiver 
{
	/** TrackRecorder gets enabled by the TrackerApp */
	private boolean _enabled = false;

	public boolean getEnabled(){ return _enabled;}
	public void setEnabled(boolean value){_enabled = value;}
	

	@Override
	public void LocationChanged(LocationIdentifier newLocation) 
	{
		if(_enabled && Environment.Instance().getCurrentTrack() != null)
		{
			Environment.Instance().getCurrentTrack().AddLocation(newLocation);
		}
	}

}
