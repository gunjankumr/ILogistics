package com.compass.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.utils.ValueHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

public class RouteMapActivity extends FragmentActivity implements
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.route_map_activity);
		valueHolder = ValueHolder.getSingletonObject();
		bundle = getIntent().getExtras();
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
		getSupportFragmentManager().findFragmentById(R.id.map);
		supportMapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap map) {
				googleMap = map;
			}
		});
		/*
		googleMap.setOnMapClickListener(this);
		googleMap.setOnMapLongClickListener(this);
		googleMap.setOnInfoWindowClickListener(this);
		*/
		//googleMap.setTrafficEnabled(true);
		//googleMap.setMyLocationEnabled(true);
		//Toast.makeText(getApplicationContext(), googleMap.getMyLocation().getLatitude()+"",Toast.LENGTH_SHORT).show();
		//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.7164567,100.5848037),15));
		
		
		/*
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.7164567,100.5848037),12));
	    LatLng begin = new LatLng(13.7164567, 100.5848037);// �ԡѴ�ش�������
	    LatLng destination1 = new LatLng(13.734841, 100.550534);// �ԡѴ���·ҧ 1 ���� ������ͻ����� �ӡѴ
	    //LatLng destination2 = new LatLng(13.734841, 100.550534);//�ԡѴ���·ҧ 2		          
	    //��鹷ҧ��� 1
	    GenDirection(begin, destination1);
	    //��鹷ҧ��� 2 ��ͨҡ��鹷ҧ��� 1
	    //GenDirection(destination1,  new LatLng(19.683660045847258, 99.0900808095932));
		*/
		
		
		googleMap.setInfoWindowAdapter(new MapPopupAdapter(getLayoutInflater()));
		
		String latTemp = "";
		String lonTemp = "";
		if(bundle.getString("map").equalsIgnoreCase("route_map")) {
			
			for(int i=0;i<valueHolder.getCustomerList().size();i++) {
	        	CustomerModel customer  = valueHolder.getCustomerList().get(i);
				for(int j=0;j<customer.getInvoiceList().size();j++) {
					if((customer.getInvoiceList().get(j).getINV_LATITUDE().length() > 0) && (customer.getInvoiceList().get(j).getINV_LONGITUDE().length() > 0)) {
						latTemp = customer.getInvoiceList().get(j).getINV_LATITUDE();
						lonTemp = customer.getInvoiceList().get(j).getINV_LONGITUDE();
						if(!hashmap.containsKey(latTemp+","+lonTemp)) {
							hashmap.put(latTemp+","+lonTemp , latTemp+","+lonTemp);						
							
							if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("Y")) {
								googleMap.addMarker(new MarkerOptions()
						        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
						        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
						        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
						        .snippet(""));
							}else if (customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {	
								googleMap.addMarker(new MarkerOptions()
						        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
						        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
						        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
						        .snippet(""));
							}else {
								googleMap.addMarker(new MarkerOptions()
						        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
						        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
						        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
						        .snippet(""));
							}
						}
					}
					
				}
	        }
			
		} else if(bundle.getString("map").equalsIgnoreCase("view_map")) {
			
			CustomerModel customer  = valueHolder.getCustomerList().get(valueHolder.getCustomerSelected());
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				if((customer.getInvoiceList().get(j).getINV_LATITUDE().length() > 0) && (customer.getInvoiceList().get(j).getINV_LONGITUDE().length() > 0)) {
					latTemp = customer.getInvoiceList().get(j).getINV_LATITUDE();
					lonTemp = customer.getInvoiceList().get(j).getINV_LONGITUDE();
					if(!hashmap.containsKey(latTemp+","+lonTemp)) {
						hashmap.put(latTemp+","+lonTemp , latTemp+","+lonTemp);
						
						if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("Y")) {
							googleMap.addMarker(new MarkerOptions()
					        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
					        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
					        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
					        .snippet(""));
						}else if (customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {	
							googleMap.addMarker(new MarkerOptions()
					        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
					        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
					        .snippet(""));
						}else {
							googleMap.addMarker(new MarkerOptions()
					        .position(new LatLng(Double.parseDouble(latTemp), Double.parseDouble(lonTemp)))
					        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
					        .title(customer.getCUST_CODE()+" - "+customer.getCUST_NAME())
					        .snippet(""));
						}
					}
				}
				
			}
			
		}
		
		
		
		if((latTemp.length()) > 0 && (lonTemp.length() > 0)) {
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latTemp),Double.parseDouble(lonTemp)),14));
		}
		
		
		/*
		googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(13.7164567,100.5848037))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        .title("Jagota Brother Trading Co.,Ltd")
        .snippet("42 Tower 17 floor room 1704"));
		*/
	      
		/*
		googleMap.addMarker(new MarkerOptions()
	    .position(new LatLng(15.55234,100))
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
	    .title("Custom Marker")
	    .snippet("content")); 
		*/
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
		

		googleMap.setOnInfoWindowClickListener(
			  new OnInfoWindowClickListener(){
			    public void onInfoWindowClick(Marker marker){
			    	Intent nextScreen = new Intent(RouteMapActivity.this,DirectionMapActivity.class);
		            nextScreen.putExtra("destinationLat", marker.getPosition().latitude);
		            nextScreen.putExtra("destinationLon", marker.getPosition().longitude);
		            nextScreen.putExtra("title", marker.getTitle());
		            startActivityForResult(nextScreen, 0);
			    	//Toast.makeText(getApplicationContext(), marker.getTitle()+"\n"+marker.getPosition().toString(), Toast.LENGTH_LONG).show();
			    }
			  }
			);
				
	}// end onCreate
	
	 public void GenDirection(LatLng fromLatLng,LatLng toLatLng){           
        DirectionGen direction = new DirectionGen();
        Document doc = direction.getDocument(fromLatLng,toLatLng, DirectionGen.MODE_DRIVING);
      
        ArrayList<LatLng> directionPoint = direction.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(2).color(Color.BLUE);
         
        for(int i = 0 ; i < directionPoint.size() ; i++){           
            rectLine.add(directionPoint.get(i));
        }          
        googleMap.addPolyline(rectLine);
	             
	}//end GenDirection 

	// AsyncTask �Ѻ��ҷ������ҡ  GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
		@Override
		protected List<Address> doInBackground(String... locationName) {				
				Geocoder geocoder = new Geocoder(getBaseContext());
				List<Address> addresses = null;			
				try {
					// �Ѻ��ҼšҤ����٧�ش  5 ���˹�
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
		    //googleMap.clear();
		        // Add Marker
		    for(int i=0;i<addresses.size();i++) {
				Address address = (Address) addresses.get(i);					
			        // �Ѻ��� LatLng
			     latLng = new LatLng(address.getLatitude(), address.getLongitude());			        
			     String addressText = String.format("%s, %s",
	                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                        address.getCountryName());

			        markerOptions = new MarkerOptions();
			        markerOptions.position(latLng);
			        markerOptions.title(addressText);
			        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			        googleMap.addMarker(markerOptions);			        			        
			        if(i==0) {
						googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			        }
				}
			}
	}//end task
	
	
	
	
	@Override
	public void onMapLongClick(LatLng point) {// LongClick ��� Map
		// ����� add Marker
		googleMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
		Toast.makeText(getApplicationContext(), "����  Marker �����", Toast.LENGTH_LONG).show();
	}	

	@Override
	public void onMapClick(LatLng point) {	// Click ��� map
		// ��ҧ  overley �� map
		//myMap.clear();
		// ����͹���˹觷��١ click �ҵç��ҧ
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	@Override
	public void onInfoWindowClick(Marker marker) {// ��ԡ��� infowindow �ͧ marker ���е��		
		marker.hideInfoWindow();
		Toast.makeText(getApplicationContext(), marker.getTitle()+"\n"+marker.getPosition().toString(), Toast.LENGTH_LONG).show();		
	}
	
	
	
	@Override
    protected void onResume() {    
        super.onResume();     
        // ત��� �� googleplay service �����������
        final int RQS_GooglePlayServices = 1;
        int resultCode =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
         
        if (resultCode == ConnectionResult.SUCCESS){
            //Toast.makeText(getApplicationContext(),"GooglePlayServices is Available",Toast.LENGTH_LONG).show();
        }else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }
         
    } // end onResume

}//end MainClass

