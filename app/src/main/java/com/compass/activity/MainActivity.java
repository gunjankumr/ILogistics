package com.compass.activity;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
	
	Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
	SessionManager session;
	
	AlertDialogManager alert = new AlertDialogManager();
	//EditText phoneNumber;

	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter username and password", false);
		
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String device_id = tm.getDeviceId();
		//Toast.makeText(getApplicationContext(), device_id,Toast.LENGTH_SHORT).show();	
		
		// creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());       
        isInternetPresent = cd.isConnectingToInternet();        
        // check for Internet status
        if (isInternetPresent) {
            showAlertDialog(MainActivity.this, "Internet Connection", "You have internet connection", true);
        } else {
            showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
        
        // Session Manager
        session = new SessionManager(getApplicationContext());  
        
        
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
				getSupportFragmentManager().findFragmentById(R.id.map);
	
		supportMapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap map) {
				googleMap = map;
			}
		});

        Button btn_find = (Button) findViewById(R.id.btn_find);			
		btn_find.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				EditText etLocation = (EditText) findViewById(R.id.et_location);				
				String location = etLocation.getText().toString();
				
				if(location!=null && !location.equals("")){
					new GeocoderTask().execute(location);
				}
			}
		});		
		
		
	}// end onCreate

	
	
	// AsyncTask GeocodeTask
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

		@Override
		protected List<Address> doInBackground(String... locationName) {
				
				Geocoder geocoder = new Geocoder(getBaseContext());
				List<Address> addresses = null;
				
				try {

					addresses = geocoder.getFromLocationName(locationName[0], 5);
				} catch (IOException e) {
					e.printStackTrace();
				}			
				return addresses;
		}			
			
		@Override
		protected void onPostExecute(List<Address> addresses) {			
		        
		    if(addresses==null || addresses.size()==0){
					Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
			}
		        
		        // Clear  Marker
		    googleMap.clear();
				
		        // Add Marker
		    for(int i=0;i<addresses.size();i++){				
					
				Address address = (Address) addresses.get(i);
					

			     latLng = new LatLng(address.getLatitude(), address.getLongitude());
			        
			     String addressText = String.format("%s, %s",
	                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                        address.getCountryName());

			        markerOptions = new MarkerOptions();
			        
			        markerOptions.position(latLng);
			        markerOptions.title(addressText);
			        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
			        		BitmapDescriptorFactory.HUE_BLUE));
			        googleMap.addMarker(markerOptions);			        
			        
			        if(i==0)			        	
						googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); 	
				}			
			}		
	}//end task
		
	
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create(); 
        // Setting Dialog Title
        alertDialog.setTitle(title); 
        // Setting Dialog Message
        alertDialog.setMessage(message); 
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail); 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
	
	
}//end MainClass

