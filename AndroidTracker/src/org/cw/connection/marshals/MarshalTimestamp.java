package org.cw.connection.marshals;

import org.ksoap2.serialization.MarshalDate;

public class MarshalTimestamp extends MarshalVector 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 543318070869184610L;

	public MarshalTimestamp()
	{
		super(null, "timestamp", "dateTime", new MarshalDate());
	}
	
}
