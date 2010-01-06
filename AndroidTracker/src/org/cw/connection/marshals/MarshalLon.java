package org.cw.connection.marshals;

public class MarshalLon extends MarshalVector 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8228533275865405364L;

	public MarshalLon()
	{
		super(null, "lon", "double", new MarshalDouble());
	}
	
}
