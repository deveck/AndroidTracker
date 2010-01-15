package org.cw.connection;

/**
 * Encapsulates an asynchronous webservice request
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public abstract class Request 
{
	/** 
	 * Called if a status update is available
	 */
	protected IUiCallback _callback = null;
	
	public IUiCallback getCallback(){ return _callback; }
	
	/**
	 * Executes the request, if execution was not successful the
	 * request needs to be requeued
	 * @param conn
	 * @return
	 */
	public abstract boolean Execute(CrossingWaysConnection conn); 
	
	/**
	 * Some Webservice Commands can process multiple date at once, e.g.
	 * LogPosition: only send credentials once but send various of coordinates
	 * @param otherRequest
	 * @return
	 */
	public abstract boolean TryMerge(Request otherRequest);
	
	
}
