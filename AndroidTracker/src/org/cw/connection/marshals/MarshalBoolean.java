package org.cw.connection.marshals;

import java.io.IOException;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * Converts boolean xml type to java boolean and vice versa
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class MarshalBoolean implements Marshal
{

	private String _namespace;
	private String _tagName;
	
	/**
	 * The number of tags to open before the boolean value is read.
	 * 
	 * A sample response looks as follows:
	 * 
	 * <VerifyCredentialsResponse xmlns="http://www.crossingways.com/">
     *   <VerifyCredentialsResult>boolean</VerifyCredentialsResult>
     * </VerifyCredentialsResponse>
     * 
     * the readinstance event is calles vor VerifyCredentialsResponse tag.
     * so one further tag must be read before the boolean value can be read
     * 
     * this avoids dummy classes for all return values
	 */
	private Integer _subTagsToRead = 0;
	
	public MarshalBoolean(String namespace, String tagName)
	{
		_namespace = namespace;
		_tagName = tagName;
	}
	
	public MarshalBoolean(String namespace, String tagName, Integer subTagsToRead)
	{
		this(namespace, tagName);
		
		_subTagsToRead = subTagsToRead;
	}
	
	@Override
	public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) 
		throws IOException, XmlPullParserException 
	{
		
		for(int i = 0; i<_subTagsToRead; i++)
			parser.nextTag();		
		
		String myText = parser.nextText();
		
		for(int i = 0; i<_subTagsToRead; i++)
			parser.nextTag();		
		
		return Boolean.parseBoolean(myText);		
	}

	@Override
	public void writeInstance(XmlSerializer writer, Object obj)
			throws IOException 
	{
		writer.text(Boolean.toString((Boolean)obj));
	}

	@Override
	public void register(SoapSerializationEnvelope ev) 
	{
		ev.addMapping(_namespace == null? ev.xsd : _namespace, 
				_tagName, 
				new Boolean(false).getClass(), 
				this);
	}

}
