package com.compass.activity;


import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.utils.GPSTracker;
import com.compass.utils.ValueHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurrentMapActivity extends FragmentActivity implements
	                                    OnMapClickListener,
	                                    OnMapLongClickListener,
	                                    OnInfoWindowClickListener {
	
	private ValueHolder valueHolder;
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;
	View convertView;
	HashMap<String,String> hashmap = new HashMap<String, String>(); 
	Bundle bundle;
	//private LocationManager locationManager;
	//private LocationListener locationListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_activity);
		valueHolder = ValueHolder.getSingletonObject();
		bundle = getIntent().getExtras();
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
		getSupportFragmentManager().findFragmentById(R.id.map) ;
				 supportMapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap map) {
				googleMap = map;
			}
		});
		
		gps = new GPSTracker(CurrentMapActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation()) {
            double latitude; //= gps.getLatitude();
            double longitude; // = gps.getLongitude();
            
            String currentLat = ""+ILogisticsApplication.currentLatitude;
    		String currentLongitude = ""+ILogisticsApplication.currentLongitude;
    		latitude = Double.valueOf(currentLat).doubleValue();
    		longitude = Double.valueOf(currentLongitude).doubleValue();
            // \n is for new line
            Toast.makeText(getApplicationContext(), gps.getProviderType() + "  :   Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            
            googleMap.addMarker(new MarkerOptions()
	        .position(new LatLng(latitude,longitude))
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
	        .title("Start location")
	        .snippet(""));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),12));
            
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        
        
	}// end onCreate
	
	@Override
	public void onMapLongClick(LatLng point) {// LongClick ??? Map
		// ????? add Marker
		googleMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
		Toast.makeText(getApplicationContext(), "????  Marker ?????", Toast.LENGTH_LONG).show();
	}	

	@Override
	public void onMapClick(LatLng point) {	// Click ??? map
		// ???  overley ?? map
		//myMap.clear();
		// ????????????? click ??????
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	@Override
	public void onInfoWindowClick(Marker marker) {// ?????? infowindow ?? marker ??????		
		marker.hideInfoWindow();
		Toast.makeText(getApplicationContext(), marker.getTitle()+"\n"+marker.getPosition().toString(), Toast.LENGTH_LONG).show();		
	}

}//end MainClass

