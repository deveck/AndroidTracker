package org.cw.connection.marshals;

public class MarshalAlt extends MarshalVector 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5875222201551948591L;

	public MarshalAlt()
	{
		super(null, "alt", "double", new MarshalDouble());
	}
	
}
