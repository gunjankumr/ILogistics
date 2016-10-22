package com.compass.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor; 
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
 
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
 
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    
   
    public void logOut(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		/*
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		// Staring Login Activity
		_context.startActivity(i);
		*/
	}
    
    public void setPreferenceVal(String key, String value) {
    	// Storing email in pref
		editor.putString(key, value);
		// commit changes
		editor.commit();
	}
    
    public String getPreferenceVal(String key) {
		if(pref.contains(key)) {
			return pref.getString(key, null);
		} else {
			return "";
		}	
	}
    
    public void deletePreferenceVal(String key) {
		if(pref.contains(key)) {
			editor.remove(key);
			editor.commit(); 
		}
	}
    
    public void deleteAllPreference(){
		// clear all store
		editor.clear();
		// commit changes
		editor.commit();
	}	
    
}    
