package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Runner mRunner;
    private String runnerUID;

    private Button refreshButton, exitButton;
    private TextView rLat, rLon;

    private DatabaseReference mDatabase;
    private FirebaseAuth Auth;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_maps_activitiy);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contactMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        runnerUID = intent.getStringExtra("runnerid");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        exitButton = (Button) findViewById(R.id.exitButton);
        refreshButton = (Button) findViewById(R.id.refreshLocationButton);
        rLat = (TextView) findViewById(R.id.contactLatitudeView);
        rLon = (TextView) findViewById(R.id.contactLongitudeView);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), EContactHome.class));
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRunnerInfo(runnerUID);
            }
        });
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

        getRunnerInfo(runnerUID);

/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
    }

    private void getRunnerInfo(String rUID) {
        DatabaseReference myRef = mDatabase.child("runner").child(rUID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRunner = dataSnapshot.getValue(Runner.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        updateLocationListing();
                        updateLocationView();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLocationListing() {
        rLat.setText(String.format("%.7f", mRunner.getLatitude()));
        rLon.setText(String.format("%.7f", mRunner.getLongitude()));
    }

    private void updateLocationView() {
        LatLng mLocation = new LatLng(mRunner.getLatitude(), mRunner.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(mLocation)
                .title(String.format("%s's Location", mRunner.getFirstName()))
                .draggable(false));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 19));
    }
}


