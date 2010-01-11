package org.cw.settings;

import org.cw.dataitems.TrackInformation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Provides access to the application wide Settings
 * 
 * @author Andreas Reiter <andreas.reiter@student.tugraz.at>
 *
 */
public class SettingsEnvironment 
{
	private Context _ctx;
	private SharedPreferences _programSettings;
	
	private static final String PREF_PROGRAMSETTINGS = "android_tracker";
	
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_LIVETRACKER_ENABLED = "lt_enabled";
	public static final String KEY_LIVETRACKER_COMMIT_INTERVAL = "lt_commit_interval";
	
	public SettingsEnvironment(Context ctx) 
	{
		_ctx = ctx;
		_programSettings = _ctx.getSharedPreferences(PREF_PROGRAMSETTINGS, Context.MODE_PRIVATE); 
	}
	
	public String getUsername(){ return getUsername(null); }
	public String getUsername(String defaultVal){ return _programSettings.getString(KEY_USERNAME, defaultVal); }
	public void setUsername(String username){ 
		_programSettings.edit()
			.putString(KEY_USERNAME, username)
			.commit();
		}
	
	public String getPassword(){ return getPassword(null); }
	public String getPassword(String defaultVal){ return _programSettings.getString(KEY_PASSWORD, defaultVal); }
	public void setPassword(String password){ 
		_programSettings.edit()
			.putString(KEY_PASSWORD, password)
			.commit();
		}
	
	public Boolean isLiveTrackerEnabled(){ return _programSettings.getBoolean(KEY_LIVETRACKER_ENABLED, false); }
	public void setLiveTrackerEnabled(Boolean enabled){ 
		_programSettings.edit()
			.putBoolean(KEY_LIVETRACKER_ENABLED, enabled)
			.commit();
		}
	
	public Integer getLiveTrackerCommitInterval(){ return _programSettings.getInt(KEY_LIVETRACKER_COMMIT_INTERVAL, 60000);}
	public void setLiveTrackerCommitInterval(Integer interval){ 
		_programSettings.edit()
			.putInt(KEY_LIVETRACKER_COMMIT_INTERVAL, interval)
			.commit();
		}
}
