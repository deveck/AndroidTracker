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
	public static final int RES_OK  = 1;
	
	/** Signals that the Activity has been canceled */
	public static final int RES_CANCEL = -1;
	
	public static final int BUTTON_RETURN_PRESSED = 0;
	
	/** Signals that the activity has completed its operation without an error,
	 *  but no action by the caller is required
	 */
	public static final int RES_NOTHINGTODO = -2;
	
	/** Request code which signals that a track should be recorded */
	public static final int REQ_STARTRECORDING = 1;

	/** Request code which signals that the track management screen was started */
	public static final int REQ_TRACKMANAGEMENT = 2;
	
	public static final int START_MAINSCREEN = 10;
	
	public static final int START_TRACKSCREEN = 11;
	
	public static final int START_SETTINGSCREEN = 12;
	
	public static final int ACTIVITY_CHOOSE = 100;

	
}
