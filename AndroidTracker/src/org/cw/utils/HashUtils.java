package org.cw.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Provides simple hashing functionality mainly for password hashing
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 */
public class HashUtils {

	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('A' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
 
	/**
	 * Calculates the hash value of the given password 
	 * 
	 * @param password
	 * @return
	 * @throws Exception 
	 */
    public static String HashPassword(String text) 
    	throws NoSuchAlgorithmException, UnsupportedEncodingException  
    {
	    MessageDigest md = MessageDigest.getInstance("SHA-1");
	    
	    byte[] sha1hash = new byte[40];
	    byte[] data = text.getBytes("utf-8");
	    md.update(data, 0, data.length);
	    sha1hash = md.digest();
	    return convertToHex(sha1hash);
    }

}
