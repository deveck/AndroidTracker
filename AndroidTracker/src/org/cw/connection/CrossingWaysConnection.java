package org.cw.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cw.CWException;
import org.cw.CWWithCallbackInfoException;
import org.cw.Environment;
import org.cw.UserCredentials;
import org.cw.connection.marshals.MarshalAlt;
import org.cw.connection.marshals.MarshalBoolean;
import org.cw.connection.marshals.MarshalDouble;
import org.cw.connection.marshals.MarshalHeadings;
import org.cw.connection.marshals.MarshalLat;
import org.cw.connection.marshals.MarshalLon;
import org.cw.connection.marshals.MarshalTimestamp;
import org.cw.gps.LocationIdentifier;
import org.cw.utils.HashUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

/**
 * Manages the connection to the crossingways webservice
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class CrossingWaysConnection extends AsyncTask<Void, CallbackInfo, Void>
{
	/**
	 * _baseUrl + "ServiceName" e.g. VerifyCredentials
	 */
	private String _namespace = "http://www.crossingways.com/";
	
	/**
	 * Url of the ASP Webservice
	 */
	private String _serviceUrl = "http://www.crossingways.com/services/livetracking.asmx";
	
	/**
	 * Http Transport layer
	 */
	private HttpTransportSE _transport = new HttpTransportSE(_serviceUrl);
	
	/** 
	 * Contains all Requests that are handled in the background
	 */
	private BlockingQueue<Request> _requestQueue = new LinkedBlockingQueue<Request>();

	/** Single Request that is run before any Request in _requestQueue,
	 * kind of requeue mechanism on request failure
	 */
	private Request _requeuedRequest = null;
	
	/** Lock combined with the priority Queue condition */
	private final Lock _requestLock = new ReentrantLock();
	
	/** Is signaled if an element is enqueued in the priority queue.
	 * If the worker thread is currently sleeping, it wakes up.
	 */
	private final Condition _requestCond = _requestLock.newCondition();
	
	/**
	 * Contains the priority requests, which are always handled before the normal requestQueue and 
	 * without waiting for the connection to wake up, these requests wake up the connection! Really VIP! :-) 
	 * 
	 * Another option would be to use only one Queue, a PriorityBlockingQueue but then we maybe would have to add
	 * a comparation algorithm to be sure that low priority elements are ordered the way the come in.
	 * For now, this approach flexible is enough
	 */
	private BlockingQueue<Request> _priorityQueue = new LinkedBlockingQueue<Request>();
	
	/** Requeue mechanism for priorityQueue */
	private Request _priorityRequeueRequest = null;
	
	
	/**
	 * Enqueues the given request as priority Request, and wakes up the WorkerThread for immediate processing
	 * 
	 * @param request
	 */
	private void EnqueuePriorityRequest(Request request)
	{
		try
		{
			_requestLock.lock();
			_priorityQueue.add(request);
			_requestCond.signal();
		}
		finally
		{
			_requestLock.unlock();
		}
		
	}
	
	
	private void EnqueueRequest(Request request)
	{
		try
		{
			_requestLock.lock();
			_requestQueue.add(request);
			_requestCond.signal();
		}
		finally
		{
			_requestLock.unlock();
		}
	}
	
	/**
	 * Performs a SOAP Call to the Crossingways webservice, for the specified
	 * method with the specified arguments
	 * 
	 * @param <T> Returntype of the SOAP Call, if unsure use Object
	 * @param methodName
	 * @param propertyInfos
	 * @throws CWException 
	 */
	@SuppressWarnings("unchecked")
	private <T> T PerformSOAPCall(String methodName, PropertyInfo[] propertyInfos, IEnvelopeSetupCallback envSetup) throws CWException	
	{
		synchronized(_transport)
		{
			SoapObject rpc = new SoapObject("http://www.crossingways.com/", methodName);
			
			for(PropertyInfo p : propertyInfos)
				rpc.addProperty(p);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			new MarshalBoolean(null, "boolean").register(envelope);		
			new MarshalDouble().register(envelope);
			new MarshalDate().register(envelope);
			
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			
			if(envSetup != null)
				envSetup.Setup(envelope);
			
			try 
			{
				_transport.debug = true;
				
				
				
				_transport.call(_namespace + methodName, envelope);
				
				return (T)envelope.getResponse();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new CWException(e.getMessage());
			}		
		}
		
	}
	
	
	/**
	 * Conversion from SoapPrimitive should be done with envelope mappings, but for same reason
	 * the envelope internally throws a ClassCastException, mybe this
	 * issue will be resolved later
	 * 
	 * @param p
	 * @return
	 */
	private boolean SoapPrimitiveToBoolean(SoapPrimitive p)
	{
		return new Boolean(p.toString());
	}
	
	/**
	 * Generates a default primitive Property info
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private PropertyInfo CreatePrimitivePropertyInfo(String name, Object value)
	{
		PropertyInfo propInfo = new PropertyInfo();
		propInfo.setName(name);
		propInfo.setValue(value);
		return propInfo;
	}
	
	
	/**
	 * Generates the Property Info for an array type
	 * @param <T>
	 * @param tagName
	 * @param elementName
	 * @param elements
	 * @return
	 */
	private <T> PropertyInfo CreateArrayPropertyInfo(String tagName, String elementName, Vector<T> elements)
	{
		PropertyInfo elementProp = new PropertyInfo();
		elementProp.setName(elementName);
		elementProp.setValue(-1.0);
		
		PropertyInfo propInfo = new PropertyInfo();
		propInfo.setName(tagName);
		propInfo.setValue(elements);
		propInfo.setElementType(elementProp);
		return propInfo;
	}
	
	/**
	 * Verifies the credentials
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public boolean VerifyCredentials(UserCredentials credentials)
		throws CWException
	{
		Boolean verified;
		try {
			verified = SoapPrimitiveToBoolean((SoapPrimitive) PerformSOAPCall("VerifyCredentials", 
					new PropertyInfo[]{
						CreatePrimitivePropertyInfo("username", credentials.getUsername()),
						CreatePrimitivePropertyInfo("passwordhash", HashUtils.HashPassword(credentials.getPassword())),
						CreatePrimitivePropertyInfo("control", "CWRocks2008")
					}, null
					/*new IEnvelopeSetupCallback(){
						@Override
						public void Setup(SoapSerializationEnvelope env) {
							new MarshalBoolean(_namespace, "VerifyCredentialsResponse", 1).register(env);
						}				
					}*/));
			return verified;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CWException(e.getMessage());
		}
	}
	
	/**
	 * Synchronous Position update, used by the worker thread (actually by the posted request)
	 * 
	 * @param username
	 * @param pwHash 
	 * @param trackId (??)
	 * @param message Message to be displayed on the live tracker icon
	 * @param locations Locations in sequence to send
	 * @return Parsed message from the server, indicating the success of the request
	 * @throws CWException
	 */
	@SuppressWarnings("unchecked")
	public ServerMessage UpdateCurrentPosition(String username, String pwHash,
			Integer trackId, String message, LocationIdentifier...locations) throws CWException
	{
		Vector<PropertyInfo> properties = new Vector<PropertyInfo>();
		properties.add(CreatePrimitivePropertyInfo("username", username));
		properties.add(CreatePrimitivePropertyInfo("password", pwHash));
		properties.add(CreatePrimitivePropertyInfo("trackId", trackId));
		properties.add(CreatePrimitivePropertyInfo("message", message));
		
		MarshalAlt altitudes = new MarshalAlt();
		MarshalLon longitudes = new MarshalLon();
		MarshalLat latitudes = new MarshalLat();
		MarshalHeadings headings = new MarshalHeadings();
		MarshalTimestamp dateTimes = new MarshalTimestamp();
		
		for(LocationIdentifier loc : locations)
		{
			longitudes.add(loc.getLocation().getLongitude());
			latitudes.add(loc.getLocation().getLatitude());
			altitudes.add(loc.getLocation().getAltitude());
			headings.add(0.0);
			dateTimes.add(loc.getDateTime());
		}
		
		//properties.add(CreatePrimitivePropertyInfo("lat", latitudes.get(0)));
		properties.add(CreateArrayPropertyInfo("lat", "double", latitudes));
		properties.add(CreateArrayPropertyInfo("lon", "double", longitudes));
		properties.add(CreateArrayPropertyInfo("alt", "double", altitudes));
		properties.add(CreateArrayPropertyInfo("heading", "double", headings));
		properties.add(CreateArrayPropertyInfo("timestamp", "dateTime", dateTimes));
		
		String serverMessage = PerformSOAPCall("LogPositions", 
				properties.toArray(new PropertyInfo[0]),
				new IEnvelopeSetupCallback() {
					
					@Override
					public void Setup(SoapSerializationEnvelope env) {
						new MarshalLat().register(env);
						new MarshalLon().register(env);
						new MarshalAlt().register(env);
						new MarshalHeadings().register(env);
						new MarshalTimestamp().register(env);	
					}
				}
			).toString();
		
		return new ServerMessage(serverMessage);
		
	}

	/**
	 * Synchronous upload a gpx file, used by the worker thread (actually by the posted request)
	 * @param username
	 * @param pwHash
	 * @param trackName
	 * @param gpxData
	 * @return
	 * @throws CWException 
	 */
	public ServerMessage UploadGpx(UploadGPXRequest request) throws CWException
	{
		if(request.getCallback() != null)
			this.publishProgress(new CallbackInfo(request.getCallback(), RequestProgressInfo.CreateStartingInfo(), request));

		try
		{
			Vector<PropertyInfo> properties = new Vector<PropertyInfo>();
			properties.add(CreatePrimitivePropertyInfo("username", request.getUsername()));
			properties.add(CreatePrimitivePropertyInfo("password", request.getPwHash()));
			properties.add(CreatePrimitivePropertyInfo("trackname", request.getTrackname()));
			properties.add(CreatePrimitivePropertyInfo("gpx", request.getGPXData()));
			
			String serverMessage = PerformSOAPCall("UploadGPX", 
					properties.toArray(new PropertyInfo[0]),
					null).toString();
			
			ServerMessage msg = new ServerMessage(serverMessage);
			
			if(request.getCallback() != null)
				this.publishProgress(new CallbackInfo(request.getCallback(), RequestProgressInfo.CreateCompletionInfo(), request));
			
			return msg;
		}
		catch(CWException e)
		{
			if(request.getCallback() != null)
			{
				//Post the execution callback and wait till it's executed by the ui thread
				CallbackInfo ci = new CallbackInfo(request.getCallback(), RequestProgressInfo.CreateErrorInfo(e.toString()), request);				
				try
				{
					ci.AcquireExecutionLock();
					this.publishProgress(ci);
					ci.WaitForExecution();
				}
				finally
				{
					ci.ReleaseExecutionLock();
				}
				
				throw new CWWithCallbackInfoException(e.getMessage(), ci);
				
			}
			
			throw e;
		}
	}
	
	
	/**
	 * Posts an asynchronous LogPosition Request
	 * @param username
	 * @param password
	 * @param location
	 * @throws CWException 
	 */
	public void PostCurrentPositionRequest(
		String username,
		String password,
		LocationIdentifier location) 
		throws CWException
	{
		LogPositionRequest request;
		try 
		{
			request = new LogPositionRequest(
					username, 
					HashUtils.HashPassword(password), 
					0, 
					"AndroidTracker",
					location);
		
			EnqueueRequest(request);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CWException(e.toString());
		}
	}

	/**
	 * Posts an asynchronous UploadGpx request.
	 * 
	 * This posted request is not handled the same way a CurrentPosition(CP) request is handled.  
	 * CP-Requests are collected and once every few seconds or even minutes they are transmitted at once.
	 * The UI does not get immediate information about the status of the request.
	 * This is not the way a GPX Request should be processed. The request should be handled immediatly 
	 * (also if other queued requests are waiting), the user should get an immediate status response and 
	 * should be notified on completion. So this more handled like a synchronous request, but without
	 * blocking the calling thread. This kind of request is called "priority request", they are handled 
	 * by a second request queue which is prioritized by the worker thread
	 */
	public void PostGPXUploadRequest(String username, String password, String trackName, String gpxData)
		throws CWException
	{
		try 
		{
			Request gpxRequest = new UploadGPXRequest(
					username, 
					HashUtils.HashPassword(password),
					trackName,
					gpxData
					);
			
			EnqueuePriorityRequest(gpxRequest);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CWException(e.toString());
		}
		
	}

	/**
	 * Tries to merge as many requests from the queue as possible
	 * 
	 * There are 2 reasons for the merge process to stop
	 * 1.) The queue is empty
	 * 2.) The next request is not compatible with the base request
	 * @param baseRequest
	 */
	private void MergeRequest(Request baseRequest)
	{
		Request requestToMerge = null;
		
		while((requestToMerge = _requestQueue.peek()) != null)
		{
			if(baseRequest.TryMerge(requestToMerge))
				_requestQueue.remove();
		}
	}
	
	@Override
	protected Void doInBackground(Void... arg0) 
	{
		try
		{
			long lastLowPriorityExecution = System.nanoTime();
			
			while(true)
			{
				/** The thread is sleeping while not active and wakes up (achieved by a condition variable)
				 *  on new priority items or if the timeout for low priority items is met
				 */				
				
				long millisSinceLastLowPriorityExecution = (System.nanoTime() - lastLowPriorityExecution) / 1000;
				long millisLowPriorityCommitInterval =  Environment.Instance().Settings().getLiveTrackerCommitInterval() * 1000;
				try
				{
					_requestLock.lock();
					
					
					
					// if no priority elements are available and it's not time to execute low priority requests,
					// we fall asleep till a priority item comes in or the timeout elapses and low priority requests are processed
					if(_priorityQueue.isEmpty() && _priorityRequeueRequest == null &&
						_requestQueue == null && _requestQueue.isEmpty())
					{
						_requestCond.await();
					}
					else if(_priorityQueue.isEmpty() && _priorityRequeueRequest == null &&		
						millisSinceLastLowPriorityExecution < millisLowPriorityCommitInterval) 
					{				
						_requestCond.await(millisLowPriorityCommitInterval - millisSinceLastLowPriorityExecution, TimeUnit.MILLISECONDS);	
					}
				}
				finally
				{
					_requestLock.unlock();
				}
				
				boolean isLowPriorityRequest = false;
				Request myRequest = null;
				
				if(_priorityRequeueRequest != null)
					myRequest = _priorityRequeueRequest;
				else if(_priorityQueue.isEmpty() == false)
					myRequest = _priorityQueue.take();				
				else if(_requeuedRequest != null && millisSinceLastLowPriorityExecution >= millisLowPriorityCommitInterval)
				{
					isLowPriorityRequest = true;
					myRequest = _requeuedRequest;
				}
				else if(_requestQueue.isEmpty() == false && millisSinceLastLowPriorityExecution >= millisLowPriorityCommitInterval)
				{
					isLowPriorityRequest = true;
					myRequest =_requestQueue.take();
				}
				
				if(myRequest != null)
				{
					MergeRequest(myRequest);
					
					//On Request failure, requeue the request and run later
					if(myRequest.Execute(this) == false)
					{
						if(isLowPriorityRequest)
							_requeuedRequest = myRequest;
						else
							_priorityRequeueRequest = myRequest;
					}
					else
					{
						if(isLowPriorityRequest)
							_requeuedRequest = null;
						else
							_priorityRequeueRequest = null;
						
					}
					
					if(isLowPriorityRequest)
						lastLowPriorityExecution = System.nanoTime();
				}
			}
		}
		catch(InterruptedException e)
		{
			
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(CallbackInfo... values) 
	{
		values[0].getUiCallback().StatusUpdate(values[0].getProgressInfo());
		
		try
		{
			values[0].AcquireExecutionLock();
			values[0].SignalExecution();
		}
		finally
		{
			values[0].ReleaseExecutionLock();
		}
	}
}
