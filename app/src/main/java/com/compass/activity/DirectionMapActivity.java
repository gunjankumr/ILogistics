package com.compass.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.compass.ilogistics.R;
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
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionMapActivity extends FragmentActivity implements
	                                    OnMapClickListener,
	                                    OnMapLongClickListener,
	                                    OnInfoWindowClickListener {
	
	private ProgressDialog dialog;
	SupportMapFragment supportMapFragment;
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;
	View convertView;
	HashMap<String,String> hashmap = new HashMap<String, String>(); 
	Bundle bundle;
	
	private LocationManager locationManager;
	private LocationListener locationListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.direction_map_activity);
		bundle = getIntent().getExtras();
		/*
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		*/    
		        //android.os.Process.killProcess(android.os.Process.myPid());
		        //System.exit(0);
		    	/*
		        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		    	intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(intent);
		    	android.os.Process.killProcess(android.os.Process.myPid());
		    	System.exit(0);
		    	*/
		    	/*
		    	PendingIntent myActivity = PendingIntent.getActivity(getApplicationContext(), 192837,  new Intent(getApplicationContext(), DirectionMapActivity.class), PendingIntent.FLAG_ONE_SHOT);
		        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 15000, myActivity );
		        System.exit(2);
		        */
		/*
		    }
		});
		*/
		supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		supportMapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap map) {
				googleMap = map;
			}
		});
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		dialog = ProgressDialog.show(DirectionMapActivity.this, "", getString(R.string.findLocation));
		dialog.setCancelable(false);
		
		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
		
		
		if (gps_enabled) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
		
		if (network_enabled) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
		

	}// end onCreate
	
	
	public void GenDirection(final LatLng fromLatLng, final LatLng toLatLng){           
        final DirectionGen direction = new DirectionGen();
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Document doc = direction.getDocument(fromLatLng, toLatLng, DirectionGen.MODE_DRIVING);
		        
		        if (doc != null) {
		        	ArrayList<LatLng> directionPoint = direction.getDirection(doc);
		            PolylineOptions rectLine = new PolylineOptions().width(2).color(Color.BLUE);
		             
		            for(int i = 0 ; i < directionPoint.size() ; i++){           
		                rectLine.add(directionPoint.get(i));
		            }          
		            googleMap.addPolyline(rectLine);
		            
				} else {
					Toast.makeText(DirectionMapActivity.this, "Ooops! Doc is null", Toast.LENGTH_LONG).show();
				}
			}
		});
	}//end GenDirection 

	
	class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locationManager.removeUpdates(locationListener);
//				String latitude = "Latitude: " + location.getLatitude();
//				String londitude = "Londitude: " + location.getLongitude();
				
				 String latitude = ""+ILogisticsApplication.currentLatitude;
		    	 String londitude = ""+ILogisticsApplication.currentLongitude;
//		    		latitude = Double.valueOf(currentLat).doubleValue();
//		    		longitude = Double.valueOf(currentLongitude).doubleValue();
				dialog.dismiss();

				googleMap.addMarker(new MarkerOptions()
		        .position(new LatLng(location.getLatitude(),location.getLongitude()))
		        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		        .title("Start location")
		        .snippet(""));
				
				googleMap.addMarker(new MarkerOptions()
		        .position(new LatLng(bundle.getDouble("destinationLat"),bundle.getDouble("destinationLon")))
		        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		        .title("Destination location")
		        .snippet(bundle.getString("title")));
				
			    LatLng begin = new LatLng(location.getLatitude(), location.getLongitude());// ????????????
			    //if(bundle.getString("destination")!= null)
		        //{
			    	Double lat = bundle.getDouble("destinationLat");
			    	Double lon = bundle.getDouble("destinationLon");
			    	LatLng destination1 = new LatLng(lat, lon);// ???????? 1 ???? ???????????? ???
				    GenDirection(begin, destination1);
		        //}
			    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),12));
			    
			    /*
			    Intent streetView = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("google.streetview:cbll="+ location.getLatitude()+","+location.getLongitude()+"&cbp=1,99.56,,1,-5.27&mz=21"));
			    startActivity(streetView);
			    */
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

