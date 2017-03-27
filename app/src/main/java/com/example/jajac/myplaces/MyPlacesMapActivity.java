package com.example.jajac.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MyPlacesMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        // either try/catch SecurityException or ask the user for permission
        try {
            this.map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Toast.makeText(this, "Needs permission", Toast.LENGTH_LONG).show();
        }

        this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String lon = Double.toString(latLng.longitude);
                String lat = Double.toString(latLng.latitude);
                Intent locationIntent = new Intent();
                locationIntent.putExtra("lon", lon);
                locationIntent.putExtra("lat", lat);
                setResult(Activity.RESULT_OK, locationIntent);
                finish();
            }
        });
    }
}
