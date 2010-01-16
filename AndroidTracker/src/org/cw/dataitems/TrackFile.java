package org.cw.dataitems;

import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.Context;

/**
 * Represents a saved track ON DISK!
 * This is just for speedup, so not all tracks need to be loaded to view them,
 * filename inspection is enough.
 * 
 * Valid track files have the following scheme:
 * gpxtrack_name.xml
 * 
 * This of course has the disadvantage that the name can only contain characters that are supported
 * by the filesystem. See Nameify for name to valid name translation
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class TrackFile 
{
	
	public static final boolean fileExists(Context ctx, String filename){
		try {
			ctx.openFileInput(filename);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the specified file is a valid track file
	 * @param filename
	 * @return
	 */
	public static final boolean IsTrackFile(String filename)
	{
		if(filename.startsWith("gpxtrack_") &&
		   filename.endsWith(".xml"))
			return true;
		
		return false;
	}
	
	
	private Context _ctx;
	
	/**
	 * The filename (without any path information) of the track file
	 */
	private String _trackfilename;
	
	
	public TrackFile(Context ctx, String trackfilename)
	{
		_ctx = ctx;
		_trackfilename = trackfilename;
	}
	

	/**
	 * Returns the raw track filename with extension but without path information
	 * @return
	 */
	public String getFilename()
	{
		return _trackfilename;
	}
	
	/**
	 * Returns the status filename of this track
	 * @return
	 */
	public String getStatFilename()
	{
		return _trackfilename.substring(0, _trackfilename.length() - (".xml").length()) + ".stat";
	}
	
	/**
	 * Deletes the file from disk
	 */
	public void DeleteMe()
	{
		_ctx.deleteFile(_trackfilename);
		_ctx.deleteFile(getStatFilename());
	}
	
	@Override
	public String toString() 
	{
		int prefixLength = ("gpxtrack_").length();
		int suffixLength = (".xml").length();
		return _trackfilename.substring(prefixLength, _trackfilename.length() - prefixLength - suffixLength);
	}
	
}
