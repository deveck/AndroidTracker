package org.cw.connection;

/**
 * Provides methods that are called for UI Status methodsand Request completion 
 * by the Worker thread of the CWConnection.
 * The methods are already called in context of the ui thread, so no
 * invoke on the ui thread is required
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public interface IUiCallback 
{
	void StatusUpdate(RequestProgressInfo progressInfo);
}
