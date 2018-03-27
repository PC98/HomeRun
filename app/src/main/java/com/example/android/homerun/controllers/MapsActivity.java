package com.example.android.homerun.controllers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homerun.R;
import com.example.android.homerun.model.Shelter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Shelter> shelterList = DashboardActivity.shelterAdapter.getShelters();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.753746, -84.386330), 11));
        if (shelterList.size() == 0) {
            Toast.makeText(this, "No Shelters to Show",
                    Toast.LENGTH_SHORT).show();
        } else {
            for (Shelter shelter : shelterList) {
                LatLng shelCoord = new LatLng(shelter.getLatitude(), shelter.getLongitude());
                Marker shelMark = mMap.addMarker(new MarkerOptions().position(shelCoord).title("Marker in " + shelter.getName()).snippet("Phone: " + shelter.getPhoneNumber() + "\n" + "Capacity: " + shelter.getCapacityString()));
                shelMark.setTag(shelter.getId());
            }
        }
    }
}
