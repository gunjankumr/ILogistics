package com.compass.activity.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.DeliveryData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Lists;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private GoogleMap googleMap;
    private Toolbar topToolbar;
    private SupportMapFragment mapFragment;
    private RecyclerView recyclerView;
    private TextView textViewNumberOfShopLocations;
    private TextView textViewCode;

    private DeliveryListAdapter deliveryListAdapter;
    private List<DeliveryData> deliveryList = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        topToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);

        TextView textViewLogOut = findViewById(R.id.logout);
        TextView textViewSave = findViewById(R.id.save);

        textViewLogOut.setOnClickListener(this);
        textViewSave.setOnClickListener(this);

        SegmentedGroup segmentedGroup = findViewById(R.id.segments);
        segmentedGroup.setOnCheckedChangeListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        recyclerView = findViewById(R.id.recyclerView);

        textViewNumberOfShopLocations = findViewById(R.id.text_view_number_of_shop);
        textViewCode = findViewById(R.id.text_view_code);

        textViewNumberOfShopLocations.setText(getString(R.string.number_of_shop_locations, 6));
        textViewCode.setText("BC11F");

        deliveryListAdapter = new DeliveryListAdapter(deliveryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MarginItemDecoration((int) getResources().getDimension(R.dimen.component_margin_8dp)));
        recyclerView.setAdapter(deliveryListAdapter);

        prepareMockData();
        deliveryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(13.723290, 100.585910);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Sukhumvit"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.map_segment) {
            recyclerView.setVisibility(GONE);
            showHideMapView(true);
        } else if (checkedId == R.id.list_segment) {
            recyclerView.setVisibility(VISIBLE);
            showHideMapView(false);
        }
    }

    private void showHideMapView(final boolean isVisible) {
        if (mapFragment.getView() != null) {
            if (isVisible) {
                mapFragment.getView().setVisibility(VISIBLE);
            } else {
                mapFragment.getView().setVisibility(INVISIBLE);
            }
        }
    }

    private void prepareMockData() {
        DeliveryData deliveryData = new DeliveryData();
        deliveryData.setShopLocation("Central world");
        deliveryData.setStartLocation("Ware house");
        deliveryData.setDropLocation("Sukhumvit");
        deliveryData.setDistance("10 km");
        deliveryData.setArrivesBefore("-");

        deliveryList.add(deliveryData);
        deliveryList.add(deliveryData);
        deliveryList.add(deliveryData);
        deliveryList.add(deliveryData);
        deliveryList.add(deliveryData);
    }
}
