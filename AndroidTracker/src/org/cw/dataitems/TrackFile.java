package org.cw.dataitems;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.CharBuffer;

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
	
	public static final TrackFile CreateFromName(Context ctx, String trackName)
	{
		return new TrackFile(ctx, "gpxtrack_" + trackName + ".xml");
	}
	
	public static final boolean fileExists(Context ctx, String trackname){
		return CreateFromName(ctx, trackname).Exists();
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
	
	public OutputStream openOuputTrackFile() throws FileNotFoundException, UnsupportedEncodingException
	{
		FileOutputStream output = _ctx.openFileOutput(_trackfilename, Context.MODE_PRIVATE);
		return output;
	}
	
	public InputStream openInputTrackFile() throws FileNotFoundException
	{
		FileInputStream input = _ctx.openFileInput(_trackfilename);
		return input;
	}
	
	/**
	 * Deletes the file from disk
	 */
	public void DeleteMe()
	{
		_ctx.deleteFile(_trackfilename);
		_ctx.deleteFile(getStatFilename());
	}
	
	/**
	 * Checks if the Track file already exists on disk
	 * @return
	 */
	public boolean Exists()
	{
		try {
			_ctx.openFileInput(_trackfilename);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() 
	{
		int prefixLength = ("gpxtrack_").length();
		int suffixLength = (".xml").length();
		return _trackfilename.substring(prefixLength, _trackfilename.length() - suffixLength);
	}

	/** Currently the file is directly saved as gpx file, 
	 *  so we only need to read the file data
	 * @return
	 * @throws IOException 
	 */
	public String getGPXData() throws IOException 
	{
		InputStreamReader rdr = new InputStreamReader(openInputTrackFile());
		
		StringBuffer strBuffer = new StringBuffer();
		
		char[] buffer = new char[4096];
		int read = -1;
		do
		{
			read = rdr.read(buffer, 0, buffer.length);
			
			if(read > 0)
				strBuffer.append(buffer, 0, read);
			
		}while(read > -1);
		
		return strBuffer.toString();
	}
	
}
