package org.cw;

/**
 * Just defines some constants used to call activities and return from activities
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public final class ActivityConstants 
{
	/** Signals that the Activity finished successful, or that data can be loaded */
	public static final int RES_OK  = 0;
	
	/** Signals that the Activity has been canceled */
	public static final int RES_CANCEL = -1;
	
	/** Request code which signals that a track should be recorded */
	public static final int REQ_STARTRECORDING = 1;
}
