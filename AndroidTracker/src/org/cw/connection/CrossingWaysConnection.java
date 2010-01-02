package org.cw.connection;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.cw.CWException;
import org.cw.UserCredentials;
import org.cw.connection.marshals.MarshalBoolean;
import org.cw.utils.HashUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class CrossingWaysConnection 
{
	private static CrossingWaysConnection _instance = null;
	
	public static final CrossingWaysConnection Instance()
	{
		if(_instance == null)
			_instance = new CrossingWaysConnection();
		
		return _instance;
	}
	
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
		SoapObject rpc = new SoapObject("http://www.crossingways.com/", methodName);
		
		for(PropertyInfo p : propertyInfos)
			rpc.addProperty(p);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		new MarshalBoolean(null, "boolean").register(envelope);		
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
	
	public void UpdateCurrentPosition()
	{
		
	}
}
