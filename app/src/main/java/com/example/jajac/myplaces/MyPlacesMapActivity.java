package com.example.jajac.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 2;

    private int state = 0;
    private boolean selCoordsEnabled = false;
    private LatLng placeLoc;

    private GoogleMap map;
    private HashMap<Marker, Integer> markerPlaceIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // this will call "onMapReady" when the map is ready
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (state == SELECT_COORDINATES && !selCoordsEnabled) {
            getMenuInflater().inflate(R.menu.menu_select_location, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_loc_item:
                this.selCoordsEnabled = true;
                Toast.makeText(this, "Tap a location", Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_loc_cancel_item:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Intent mapIntent = getIntent();
        Bundle mapBundle = mapIntent.getExtras();
        if (mapBundle != null) {
            this.state = mapBundle.getInt("state");
            if (this.state == CENTER_PLACE_ON_MAP) {
                String placeLat = mapBundle.getString("lat");
                String placeLon = mapBundle.getString("lon");
                this.placeLoc = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLon));
            }
        }

        if (state == SHOW_MAP) {
            // must either try and catch SecurityException or ask the user for permission
            try {
                this.map.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                Log.e("MyPlacesMapActivity", e.getMessage());
            }
        } else if (state == SELECT_COORDINATES) {
            this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                if (state == SELECT_COORDINATES && selCoordsEnabled) {
                    String lon = Double.toString(latLng.longitude);
                    String lat = Double.toString(latLng.latitude);
                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("lon", lon);
                    locationIntent.putExtra("lat", lat);
                    setResult(Activity.RESULT_OK, locationIntent);
                    finish();
                }
                }
            });
        } else {
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
        }

        this.addMyPlacesMarkers();
        this.map.setOnMarkerClickListener(this);
    }

    private void addMyPlacesMarkers() {
        ArrayList<MyPlace> places = MyPlacesData.getInstance().getMyPlaces();
        markerPlaceIdMap = new HashMap<Marker, Integer>((int)((double)places.size()*1.2));
        for (int i = 0; i < places.size(); i++) {
            MyPlace place = places.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitude();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place));
            markerOptions.title(place.getName());
            Marker marker = map.addMarker(markerOptions);
            markerPlaceIdMap.put(marker, i);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(this, ViewMyPlaceActivity.class);
        int hashPos = markerPlaceIdMap.get(marker);
        i.putExtra("position", hashPos);
        startActivity(i);
        return true;
    }
}
