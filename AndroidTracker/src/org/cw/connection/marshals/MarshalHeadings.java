package org.cw.connection.marshals;

public class MarshalHeadings extends MarshalVector 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8839126039436776443L;

	public MarshalHeadings()
	{
		super(null, "heading", "double", new MarshalDouble());
	}
	
}
