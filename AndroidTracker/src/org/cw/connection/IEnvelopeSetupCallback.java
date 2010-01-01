package org.cw.connection;

import org.ksoap2.serialization.SoapSerializationEnvelope;

/**
 * "Callback" during generation of the SoapSerializationEnvelope
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public interface IEnvelopeSetupCallback 
{
	void Setup(SoapSerializationEnvelope env);
}
