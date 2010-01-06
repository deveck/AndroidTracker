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
public class MarshalDouble implements Marshal
{

	private String _namespace;
	private String _tagName;
	
	public MarshalDouble()
	{
		this(null, "double");
	}
	
	public MarshalDouble(String namespace, String tagName)
	{
		_namespace = namespace;
		_tagName = tagName;
	}
	
	
	@Override
	public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) 
		throws IOException, XmlPullParserException 
	{
		
		if(parser.nextTag() != XmlPullParser.START_TAG)
			throw new XmlPullParserException("Start tag expected");
		
		String myText = parser.nextText();
		
		if(parser.nextTag() != XmlPullParser.END_TAG)
			throw new XmlPullParserException("End tag expected");
		
		return Double.parseDouble(myText);		
	}

	@Override
	public void writeInstance(XmlSerializer writer, Object obj)
			throws IOException 
	{
		writer.text(Double.toString((Double)obj));
	}

	@Override
	public void register(SoapSerializationEnvelope ev) 
	{
		ev.addMapping(_namespace == null? ev.xsd : _namespace, 
				_tagName, 
				Double.class, 
				this);
	}

}
