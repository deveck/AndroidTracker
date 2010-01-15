package org.cw.connection.marshals;

public class MarshalLat extends MarshalVector 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6392788967038817919L;

	public MarshalLat()
	{
		super(null, "lat", "double", new MarshalDouble());
	}
	
}
