package org.cw.connection.marshals;

import java.io.IOException;
import java.util.Vector;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * Serializes a Vector<T> to an SerializationEvelope, 
 * the included implementation is not compatible with .NET Webservices 
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
@SuppressWarnings("unchecked")
public class MarshalVector  extends Vector implements Marshal
{
	private static final long serialVersionUID = 427414227414645475L;
	
	private String _namespace;
	private String _tagName;
	private Marshal _subMarshal;
	
	/** XmlName of the Items in the Vector, this is not configurable
	 * with the included implementation, but .NET WebServices require
	 * a strict naming
	 */
	private String _elementName;
	
	public MarshalVector(String namespace, String tagName, String elementName, Marshal subMarshal)
	{
		_namespace = namespace;
		_tagName = tagName;
		_elementName = elementName;
		_subMarshal = subMarshal;
	}
		@Override
	public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) 
		throws IOException, XmlPullParserException 
	{
		if(parser.nextTag() != XmlPullParser.START_TAG)
			throw new XmlPullParserException("Expected START_TAG");

		Vector vec = new Vector();
		
		while(parser.getEventType() != XmlPullParser.END_TAG)
		{
			if(_subMarshal != null)
				vec.add(_subMarshal.readInstance(parser, namespace, name, expected));
			else
				vec.add(parser.nextText());
			
			parser.nextTag();
		}
		
		return vec;
	}

	@Override
	public void writeInstance(XmlSerializer writer, Object obj)
			throws IOException 
	{
		Vector vector = (Vector)obj;
		
		int cnt = vector.size();
		
		for (int i = 0; i < cnt; i++)
		{
			if(vector.elementAt(i) == null)
				continue;
			
			writer.startTag(null, _elementName);
			
			if(_subMarshal != null)
				_subMarshal.writeInstance(writer, vector.elementAt(i));
			else
				writer.text(vector.elementAt(i).toString());
			
			writer.endTag(null, _elementName);
			
		}
	}

	@Override
	public void register(SoapSerializationEnvelope ev) 
	{
		ev.addMapping(_namespace == null? ev.xsd : _namespace,
				_tagName, 
				this.getClass(), 
				this);
	}

}
