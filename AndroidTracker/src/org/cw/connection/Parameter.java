package org.cw.connection;

import java.util.Vector;

public class Parameter 
{
	/**
	 * true: no subParameters
	 * false: with subparameters
	 */
	protected boolean _isPrimitive = true;
	
	
	protected String _name;
	protected Object _value = null;
	protected Vector<Parameter> _subParameters = new Vector<Parameter>();
	
	
	public Parameter(boolean isPrimitive, String name){
		_isPrimitive = isPrimitive;
		_name = name;
	}
	
	public Parameter(boolean isPrimitive, String name, Object value)
	{
		this(isPrimitive, name);
		_value = value;
	}
	
	public String getName() { return _name;}
	public Object getValue() { return _value; }
	public void setValue(Object value) { _value = value;}
	public Vector<Parameter> getParameters() { return _subParameters;}
	
	
}
