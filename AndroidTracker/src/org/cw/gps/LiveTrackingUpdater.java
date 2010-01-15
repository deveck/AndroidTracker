package org.cw.gps;

import org.cw.CWException;
import org.cw.Environment;

/**
 * Checks if live tracking is enabled, and posts an UpdatePosition
 * request to the Connection
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class LiveTrackingUpdater implements IGpsStatusReceiver 
{

	@Override
	public void LocationChanged(LocationIdentifier newLocation) 
	{
		if(Environment.Instance().Settings().isLiveTrackerEnabled() &&
				Environment.Instance().Settings().getUsername() != null &&
				Environment.Instance().Settings().getPassword() != null)
		{
			try 
			{
				Environment.Instance().ConnectionInstance().PostCurrentPositionRequest(
						Environment.Instance().Settings().getUsername(),
						Environment.Instance().Settings().getPassword(), 
						newLocation);
			} catch (CWException e) 
			{				
				e.printStackTrace();
				Environment.Instance().AlertBuilderInstance().ShowInfoBox("Error posting live tracker update", "Eeek", "Bring me back");
			}
		}
	}

}
