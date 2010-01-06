package org.cw.utils;

public class UnitConversion {

	/**
	 * Converts km to land mile, length of mile is given by 1609,344m
	 * @param kmh
	 * @return
	 */
	static double KmToLm(double kmh){
		return kmh / 1.609344;
	}
	
	/**
	 * Converts land mile to km, length of mile is given by 1609,344m
	 * @param lm
	 * @return
	 */
	static double LmToKm(double lm){
		return lm * 1.609344;
	}
	
	/**
	 * Converts km to sea mile, length of mile is given by 1852.216m
	 * @param kmh
	 * @return
	 */
	static double KmtoSm(double km){
		return km / 1.852216;
	}
	
	/**
	 * Converts sea mile to km, length of mile is given by 1852.216m
	 * @param sm
	 * @return
	 */
	static double SmToKm(double sm){
		return sm * 1.852216;
	}
	
	/**
	 * Converts m to feet, length of feet is given by 0.3048m
	 * @param m
	 * @return
	 */
	static double MToFeet(double m){
		return m / 0.3048;
	}
	
	/**
	 * Converts feet to m, length of feet is given by 0.3048m
	 * @param feet
	 * @return
	 */
	static double FeetToM(double feet){
		return feet * 0.3048;
	}
}
