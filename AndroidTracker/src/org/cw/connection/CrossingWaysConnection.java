package org.cw.connection;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.cw.CWException;
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


public class CrossingWaysConnection extends AsyncTask<Void, AsyncTaskProgressInfo, Void>
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
	
	private BlockingQueue<Request> _requestQueue = new LinkedBlockingQueue<Request>();

	/** Single Request that is run before any Request in _requestQueue,
	 * kind of requeue mechanism on request failure
	 */
	private Request _requeuedRequest = null;
	
	/**
	 * Performs a SOAP Call to the Crossingways webservice, wo the specified
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
		
		_requestQueue.add(request);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CWException(e.toString());
		}
	}

	@Override
	protected Void doInBackground(Void... arg0) 
	{
		try
		{
			while(true)
			{
				/** Currently the Thread is sleeping while not active because the only requests
				 * that are handled by this thread are LogPositions requests. 
				 * Once there are more requests available (Upload gpx,...)
				 * Some other mechanism must be implemented...
				 */
				Thread.sleep(Environment.Instance().Settings().getLiveTrackerCommitInterval());
				Request myRequest = null;
				
				if(_requeuedRequest != null)
					myRequest = _requeuedRequest;
				else
					myRequest =_requestQueue.take();
				
				MergeRequest(myRequest);
				
				//On Request failure, requeue the request and run later
				if(myRequest.Execute(this) == false)
					_requeuedRequest = myRequest;
				else
					_requeuedRequest = null;
			}
		}
		catch(InterruptedException e)
		{
			
		}
		return null;
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
}
