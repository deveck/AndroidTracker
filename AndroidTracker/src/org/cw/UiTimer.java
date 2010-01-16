package org.cw;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cw.connection.CallbackInfo;

import android.os.AsyncTask;

/**
 * Timer which raises events at defined intervals, the special thing about this timer is that
 * it raises the event in context of the UI-Thread
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class UiTimer extends AsyncTask<Void, Void, Void>
{
	private volatile boolean _destroyMe = false;
	
	/** Synchronization context */
	private Object _syncLock = new Object();

	/** Indicaates if the timer is enabled */
	private boolean _enabled = false;
	
	/** The interval of the timer interrupts */
	private int _interval = 100;
	
	/** Callback for the timer */
	private ICallback<Object> _callback = null;
	
	/** Lock combined ui condition */
	private final Lock _uiLock = new ReentrantLock();
	
	/** Is signaled if the ui callback has been processed
	 */
	private final Condition _uiCond = _uiLock.newCondition();
	
	public boolean getEnabled()
	{
		synchronized(_syncLock)
		{
			return _enabled;
		}
	}
	
	public void setEnabled(boolean value)
	{
		synchronized(_syncLock)
		{
			_enabled = value;
		}
	}
	
	public int getInterval()
	{
		synchronized(_syncLock)
		{
			return _interval;
		}
	}
	
	public void setInterval(int value)
	{
		synchronized(_syncLock)
		{
			_interval = value;
		}
	}
	
	public UiTimer(ICallback<Object> callback)
	{
		_callback = callback;
	}
	
	
	@Override
	protected Void doInBackground(Void... arg0) 
	{
		int maxThreadSleep = 100;
		long lastCall = System.nanoTime() /1000 /1000;
		
		try 
		{
			while(!_destroyMe)
			{			
				Thread.sleep(Math.min(maxThreadSleep, _interval));
				
				if(_enabled && (System.nanoTime()/1000/1000) - lastCall >= _interval)
				{
					try
					{
						_uiLock.lock();
						publishProgress((Void[])null);
						_uiCond.await();
					}
					finally
					{
						_uiLock.unlock();
					}
				}
			}
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	
	@Override
	protected void onProgressUpdate(Void... values) 
	{
		super.onProgressUpdate(values);
		
		_callback.Callback(this);
		
		try
		{
			_uiLock.lock();
			_uiCond.signal();
		}
		finally
		{
			_uiLock.unlock();
		}
		
	}
	
}
