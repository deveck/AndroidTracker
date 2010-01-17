package org.cw.connection;

/**
 * Progress info handed over to the ui thread by the CrossingWaysConnection
 * on status update
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class RequestProgressInfo 
{
	public static final RequestProgressInfo CreateStartingInfo()
	{
		RequestProgressInfo pInfo = new RequestProgressInfo();
		pInfo._status = StatusTypeEnum.STARTING;
		return pInfo;
	}

	public static final RequestProgressInfo CreateStatusInfo(String statusMessage)
	{
		RequestProgressInfo pInfo = new RequestProgressInfo();
		pInfo._status = StatusTypeEnum.STATUS;
		pInfo._statusMessage = statusMessage;
		return pInfo;
	}
	
	public static final RequestProgressInfo CreateErrorInfo(String errorMessage)
	{
		RequestProgressInfo pInfo = new RequestProgressInfo();
		pInfo._status = StatusTypeEnum.ERROR;
		pInfo._statusMessage = errorMessage;
		return pInfo;
	}
	
	public static final RequestProgressInfo CreateCompletionInfo()
	{
		RequestProgressInfo pInfo = new RequestProgressInfo();
		pInfo._status = StatusTypeEnum.COMPLETED;
		return pInfo;
	}
	
	public enum StatusTypeEnum
	{
		/** Indicates that the request gets started */
		STARTING,
		
		/** Indicates that this is a status update, no action is required */
		STATUS,
		
		/** Indicates that the Request produced an error */
		ERROR,
		
		/** Indicates that the Request completed successful */
		COMPLETED
	}
	
	protected StatusTypeEnum _status;
	
	/** In case of an error this contains the error message */
	protected String _statusMessage = null;
	
	/** In error case this indicates if the request should be requeued or not */
	protected Boolean _requeue = true;
	
	public Boolean getRequeue(){ return _requeue; }
	public void setRequeue(Boolean value){ _requeue = value; }
	
	public StatusTypeEnum getStatus(){return _status;}
	
	protected RequestProgressInfo()
	{
		
	}
	
}
