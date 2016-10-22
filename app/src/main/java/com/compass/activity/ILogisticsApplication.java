package com.compass.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.util.Log;


public class ILogisticsApplication extends Application implements LocationListener {

	LocationManager locationManager = null;
	public static double currentLatitude = 0.0;
	public static double currentLongitude = 0.0;
	public static ArrayList<HashMap<String, String>> problemListArr = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initGPSService();
	}
	
	/**
	 * Purpose: Start GPS service on application launch, so that accurate location will be available while using
	 */
	public void initGPSService() {
		try {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			locationUpdate();
		} catch(Exception e) {
			
		}
	}

	public void locationUpdate() {
		Log.d("GPS","Location Services Started");
	    CellLocation.requestLocationUpdate();
	}
	
	public void stopLocationServices() {
		 Log.d("GPS","Location Services Stopped");
		 if(locationManager != null) {
			 locationManager.removeUpdates(this);
		 }
	 }
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			Log.d("Device GPS Position", "Latitude ===>> "+location.getLatitude()+" Longitude ===>> "+location.getLongitude());
			currentLatitude = location.getLatitude();
			currentLongitude = location.getLongitude();
//			currentLatitude = 13.75;
//			currentLongitude = 100.4667; 
	    }
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}