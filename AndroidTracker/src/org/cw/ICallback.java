package org.cw;

/**
 * Generic Callback where a method with one PArameter is defined 
 *  
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public interface ICallback<T> 
{
	void Callback(T param);
}
