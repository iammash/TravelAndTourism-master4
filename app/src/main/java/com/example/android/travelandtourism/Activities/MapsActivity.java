package com.example.android.travelandtourism.Activities;
//Created By Dania
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.travelandtourism.Activities.ResponseValue;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

import io.realm.internal.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double Latitude = 0.0;
    double Longitude = 0.0;
    String locationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Dania
        Intent intent = this.getIntent();
        final int hotelId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        Latitude =Double.parseDouble(intent.getStringExtra("Latitude")) ;
        Longitude =Double.parseDouble(intent.getStringExtra("Longitude")) ;
        locationName = intent.getStringExtra("hotelName");

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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker and move the camera
        LatLng Marker = new LatLng(Latitude,Longitude);

        mMap.addMarker(new MarkerOptions().position(Marker).title(locationName)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Marker,16));
    }
}
