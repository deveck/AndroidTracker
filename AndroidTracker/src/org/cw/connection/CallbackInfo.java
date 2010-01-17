package org.cw.connection;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Collects the callback and the data, to be executed in context of the ui thread
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class CallbackInfo 
{
	private final Lock _executionLock = new ReentrantLock();
	private final Condition _executedCond = _executionLock.newCondition();
	
	private IUiCallback _callback;
	private RequestProgressInfo _progressInfo;
	private Request _request;
	
	public IUiCallback getUiCallback(){ return _callback; }
	public RequestProgressInfo getProgressInfo(){ return _progressInfo; }
	public Request getRequest(){ return _request; }
	
	public CallbackInfo(IUiCallback callback, RequestProgressInfo progressInfo, Request request)
	{
		_callback = callback;
		_progressInfo = progressInfo;
		_request = request;
	}
	
	public void AcquireExecutionLock()
	{
		_executionLock.lock();
	}
	
	
	public void ReleaseExecutionLock()
	{
		_executionLock.unlock();
	}
	
	/**
	 * Waits till the callback was executed by the ui thread and then returns
	 */
	public void WaitForExecution()
	{
		try 
		{
			_executedCond.await();
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Signals that the execution is completed, the waiting thread will wake up
	 */
	public void SignalExecution()
	{
		_executedCond.signal();
	}
}
