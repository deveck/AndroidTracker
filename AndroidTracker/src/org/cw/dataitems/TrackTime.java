package org.cw.dataitems;

/**
 * Manages the whole tracktime, including pause and start calls
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackTime
{
	/** Initial duration without pause or something */
	private long _startDurationMs;
	
	/** Local Timestamp when started in ms */
	private long _startingTime;
	
	/** Indicates if the timer is sarted */
	private boolean _isStarted = false;
	
	public TrackTime(long startDurationMs)
	{
		_startDurationMs = startDurationMs;
		_startingTime = System.currentTimeMillis();
	}

	/** Gets the complete elapsed time in ms */
	public long getCurrentDuration()
	{
		if(_isStarted)
			return _startDurationMs + (System.currentTimeMillis() - _startingTime);
		else
			return _startDurationMs;
	}
	
	public double getDisplayHours()
	{
		return getCurrentDuration() / 1000.0 /60.0 / 60.0;
	}
	
	public double getDisplayMinutes()
	{
		double hours = getDisplayHours();
		
		return ((double)(hours - (int)hours)) * 60;
	}
	
	public double getDisplaySeconds()
	{
		double minutes = getDisplayMinutes();
		
		return ((double)(minutes - (int)minutes)) * 60;
	}
	
	/** Pauses the timer */
	public void Pause()
	{
		_startDurationMs = getCurrentDuration();
		_isStarted = false;
	}
	
	/** Wakes up the timer from a paused state */
	public void Start()
	{
		_startingTime = System.currentTimeMillis();
		_isStarted = true;
	}
	
	/** Starts the time after resetting to the initial duration */
	public void Start(long predefinedDuration)
	{
		_startDurationMs = predefinedDuration;
		Start();
	}
	
}
