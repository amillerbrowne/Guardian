package com.example.nikig.logintest;

import android.Manifest;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SettingsAlertDialog.settingsDialogListener,
        EmergencyAlertDialog.emergencyDialogListener
{

    /** map objects **/
    GoogleMap googleMap;
    SupportMapFragment mapFrag;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location lastLocation, startLocation, endLocation;
    Marker currLocationMarker;


    /** bluetooth objects **/
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public ImageView connectionStatus;
    //SPP UUID. Look for it
    static final UUID sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /** generic views/objects **/
    Button cameraLockButton, startPointButton, endPointButton;
    TextView latitudeTV, longitudeTV, distanceTV, durationTV;

    /** primitive member variables **/
    public boolean cameraFollow;
    public boolean settingMapSat, settingZoomOut, autoTextContact;
    public int zoomLevel, startMarkerCount, endMarkerCount;
    public String deviceAddress = null;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private long startTimeMillis, endTimeMillis, timeRun;
    private String startTimeFormatted, endTimeFormatted, runTimeFormatted, statsMessage;
    private String selectedContactPhoneNo, emergencyTriggerKey;
    private float distanceRun;
    public int selectedContactIndex, updateCounter;

    /** database variables **/
    private Runner runner;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    /** TESTING PURPOSE VARS **/
    String contacts[] = {"Inna", "Alex"};
    final String contactNums[] = {"16179357165", "17818660040"};
    // Phone nums for respective contacts[] entries
    // also only for testing, will be substituted with
    // database calls when ready


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Get the bluetooth device address from the passed intent
        Intent intent = getIntent();
        deviceAddress = intent.getStringExtra(Home.BLUETOOTH_DEVICE_ADDRESS);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        connectionStatus = (ImageView) findViewById(R.id.connectionStatusBar);

        // execute the bluetooth connection
        new ConnectBT().execute();
        // if success, set connection bar to green
        bluetoothConnectSuccess(connectionStatus);

        // set up database connection
        Auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = Auth.getCurrentUser();

        if(firebaseUser == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // default emergency contact number to 911 for now (NOT REAL 911)
        selectedContactPhoneNo = "119";

        // default emergencyTriggerKey to false;
        emergencyTriggerKey = "false";

        // initialize updateCounter
        updateCounter = 8; // start high to send update soon after launch

        // make sure run times are set to zero
        startTimeMillis = 0;
        endTimeMillis = 0;

        autoTextContact = true;
        settingMapSat = false;
        settingZoomOut = false;
    }

    protected void onResume() {
        super.onResume();

        // resume location updates
        if (googleApiClient != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        }
    }

    protected void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
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
        // set up map object to manipulate
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // use normal map type for navigation
        zoomLevel = 19; // standard zoom level for fixed follow mode
        cameraFollow = true; // set to true by default on startup
        googleMap.clear(); // clear the map
        startMarkerCount = 0;
        endMarkerCount = 0;

        // Initialize Google Play services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000); // 3 seconds
        locationRequest.setFastestInterval(1000); // 1 second
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location Services Suspended, Please Reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        // update counter and send data if we reach 7; ~21 seconds per update to device
        updateCounter++;

        // update database every 15 seconds
        if(updateCounter == 3 || updateCounter == 8) {
            updateLocationInDatabase(lastLocation);
        }

        // update arduino every 30 seconds
        if(updateCounter == 10){
            sendFormattedStringByBluetooth(getEContactCallParameters());
            updateCounter = 0;
        }

        updateLocationListing(lastLocation); // always update location values

        if (cameraFollow) {                       // follow location with camera if option enabled
            updateCameraLocation(lastLocation);
        }
    }

    private void updateLocationListing(Location location) {
        latitudeTV = (TextView) findViewById(R.id.latitude);
        longitudeTV = (TextView) findViewById(R.id.longitude);
        latitudeTV.setText(String.valueOf(location.getLatitude()));
        longitudeTV.setText(String.valueOf(location.getLongitude()));
    }

    private void updateLocationInDatabase(Location location) {
        DatabaseReference reference = databaseReference.child("runner").child(firebaseUser.getUid());
        Log.d(TAG, firebaseUser.getUid());

        databaseReference.child(firebaseUser.getUid()).child("latitude").setValue(lastLocation.getLatitude());
        databaseReference.child(firebaseUser.getUid()).child("longitude").setValue(lastLocation.getLongitude());
    }

    private void updateCameraLocation(Location location) {
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = googleMap.addMarker(markerOptions);
        //move map camera
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        currLocationMarker.remove();
    }

    // Permission Constants
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_CALL_911 = 88;
    public static final int MY_PERMISSIONS_CALL_CONTACT = 77;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void check911CallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Make Phone Call Permission Needed")
                        .setMessage("This app needs permission to make a call for you.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_CALL_911);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL_911);
            }
        }
    }

    private void checkContactCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Make Phone Call Permission Needed")
                        .setMessage("This app needs permission to make a call for you.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_CALL_CONTACT);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL_CONTACT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_CALL_911: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, make the call
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        make911Call();
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
            case MY_PERMISSIONS_CALL_CONTACT: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, make the call
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        makeEContactCall();
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void selectEmergencyContact(View view) {
        // String contacts[] = TODO: Database call for Emergency Contacts then phone numbers

        // build a dialog to display the list of contacts
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        // inflate and set views
        LayoutInflater inflater = getLayoutInflater();
        View v = (View) inflater.inflate(R.layout.list_items, null);
        builder.setView(v);

        // set dialog title
        builder.setTitle("Select Emergency Contact");

        // set up a list view
        ListView lv = (ListView) v.findViewById(R.id.listview);

        // set up arrayadapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContactIndex = position;
                selectedContactPhoneNo = contactNums[selectedContactIndex];
            }
        });

        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void triggerEmergency(View view) {
        DialogFragment dialog = new EmergencyAlertDialog();
        dialog.show(getFragmentManager(), "Emergency Alert Dialog");
    }

    private String get911CallParameters() {
        return String.format("%s:%f:%f:%f:119;", // DO NOT USE REAL 911
                emergencyTriggerKey,
                lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                lastLocation.getAccuracy());
    }

    private String getEContactCallParameters() {
        return String.format("%s:%f:%f:%f:%s;",
                emergencyTriggerKey,
                lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                lastLocation.getAccuracy(),
                selectedContactPhoneNo);
    }

    private void sendFormattedStringByBluetooth(String message) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                bluetoothConnectFailure(connectionStatus);
                msg("Error sending data.");
                playAlertSound();
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Error Connecting to Device");
                builder.setMessage("You have been disconnected from your Guardian device.\n" +
                        "Please return to the device menu and reconnect to maintain functionality.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void playAlertSound() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    private void make911Call() {

        String number = "tel:119"; // DO NOT use real 911 for testing lol
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(number));

        // check permission and call or request
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            check911CallPermission();
        }
    }

    private void makeEContactCall() {
        String number = "tel:" + selectedContactPhoneNo;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            checkContactCallPermission();
        }
    }



    public void onEmergencyDialog911Click(DialogFragment dialog){
        // update emergencyKey to true
        emergencyTriggerKey = "true";
        updateCounter = 0;  // prevent an update for at least ~20 seconds so this isn't interrupted

        // get formatted string to send by bluetooth
        sendFormattedStringByBluetooth(get911CallParameters());

        // also send message to emergency contact
        if(autoTextContact) {
            sendFormattedStringByBluetooth(getEContactCallParameters());
        }

        // forward call?
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Dial 911?");
        builder.setMessage("An emergency text message has been sent.\n" +
                "Do you also want to place a call to emergency services?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                make911Call();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // return key to false
        emergencyTriggerKey = "false";
    }

    public void onEmergencyDialogPersonalContactClick(DialogFragment dialog){
        // key set to true
        emergencyTriggerKey = "true";
        updateCounter = 0;  // prevent an update for at least ~20 seconds so this isn't interrupted

        // send formatted message
        sendFormattedStringByBluetooth(getEContactCallParameters());

        // ask to call
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Dial Emergency Contact?");
        builder.setMessage("An emergency text message has been sent.\n" +
                "Do you also want to place a call to your contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeEContactCall();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // reset key to false
        emergencyTriggerKey = "false";
    }
    public void onEmergencyDialogCancelClick(DialogFragment dialog){
        // do nothing. handled in dialog class
    }

    public void cameraFollowToggle(View view) {

        cameraLockButton = (Button) findViewById(R.id.cameraLockButton);

        if(cameraFollow) {
            cameraFollow = false;
            //cameraLockButton.setBackgroundColor(getResources().getColor(R.color.colorBGgrey));
            cameraLockButton.setBackground(getDrawable(R.drawable.button_dark));
            cameraLockButton.setText("Lock \nCamera");
        } else {
            cameraFollow = true;
            //cameraLockButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
            cameraLockButton.setBackground(getDrawable(R.drawable.button_blue));
            cameraLockButton.setText("Camera \nLocked");
        }
    }

    public void openSettings(View view) {
        DialogFragment dialog = SettingsAlertDialog.newInstance(settingMapSat, settingZoomOut, autoTextContact);
        dialog.show(getFragmentManager(), "SettingsAlertDialog");
    }


    // settingsAlertDialog.settingsDialogListener interface implementation
    @Override
    public void onSettingsDialogPositiveClick(DialogFragment dialog, boolean isSat, boolean zoomOut, boolean autoText) {
        // User touched the dialog's positive button

        // map type setting
        if(isSat) {
            settingMapSat = true;
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            settingMapSat = false;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        // zoom setting
        if(zoomOut) {
            settingZoomOut = true;
            zoomLevel = 16;
        } else {
            settingZoomOut = false;
            zoomLevel = 19;
        }

        // autoTextContact setting
        if(autoText) {
            autoTextContact = true;
        } else autoTextContact = false;


    }
    @Override
    public void onSettingsDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // do nothing lol
    }

    public String returnCurrentTimeString() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        String stringHour, stringMinute, stringSecond, halfDay;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if(hour > 12) {
            halfDay = "PM";
            hour -= 12;
        } else {
            halfDay = "AM";
        }

        if(hour < 10) {
            stringHour = String.format("0%d", hour);
        } else stringHour = Integer.toString(hour);

        if(minute < 10)
        {
            stringMinute = String.format("0%d", minute);
        } else stringMinute = Integer.toString(minute);

        if(second < 10) {
            stringSecond = String.format("0%d", second);
        } else stringSecond = Integer.toString(second);


        String dateFormatted = String.format("%s:%s:%s %s", stringHour, stringMinute, stringSecond, halfDay);
        return dateFormatted;
    }

    public long getCurrentTimeMilliseconds() {
        return System.currentTimeMillis();
    }

    public void startPoint(final View view) {
        if(startMarkerCount != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("Reset Starting Point?");
            builder.setMessage("Looks like you have already placed a starting location" +
                    "marker. Would you like to reset it?");
            builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    googleMap.clear();
                    startMarkerCount = 0;
                    endMarkerCount = 0;
                    startLocation = null;
                    endLocation = null;
                    startTimeMillis = 0;
                    endTimeMillis = 0;
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        if(lastLocation != null) {

            startLocation = lastLocation;

            // get current time for calculation later
            startTimeMillis = getCurrentTimeMilliseconds();
            startTimeFormatted = returnCurrentTimeString();

            // set up a marker to drop on map
            LatLng startPos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            final Marker startMkr = googleMap.addMarker(new MarkerOptions()
                    .position(startPos)
                    .draggable(false)
                    .title("Start Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            startMkr.setTag("StartMarker");
            startMarkerCount++;
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTag().equals(startMkr.getTag())) {
                        Snackbar sb = Snackbar.make(view,
                                String.format("The Start Time was: %s", startTimeFormatted),
                                Snackbar.LENGTH_LONG);
                        sb.show();
                        return true;
                    } else return false;
                }
            });
        }
    }

    public void endPoint(final View view) {
        if(startMarkerCount == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("It looks like you haven't set a starting position marker" +
                    "yet. You should do this before placing an end marker so that the " +
                    "application can calculate your run statistics when you finish your exercise.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if(endMarkerCount != 0) {
            if(endMarkerCount != 0) {
                // TODO: handle the situation where the marker has already been placed
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Reset Ending Point?");
                builder.setMessage("Looks like you have already placed a finishing location" +
                        "marker. Would you like to reset it?");
                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        googleMap.clear();
                        startMarkerCount = 0;
                        endMarkerCount = 0;
                        startLocation = null;
                        endLocation = null;
                        startTimeMillis = 0;
                        endTimeMillis = 0;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
        }


        if(lastLocation != null) {

            endLocation = lastLocation;

            // get time placed for calculations later
            endTimeMillis = getCurrentTimeMilliseconds();
            endTimeFormatted = returnCurrentTimeString();

            LatLng endPos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            final Marker endMkr = googleMap.addMarker(new MarkerOptions()
                    .position(endPos)
                    .draggable(false)
                    .title("End Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            endMkr.setTag("EndMarker");
            endMarkerCount++;
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(marker.getTag().equals(endMkr.getTag())) {
                        Snackbar sb = Snackbar.make(view,
                                String.format("The End Time was: %s", endTimeFormatted),
                                Snackbar.LENGTH_LONG);
                        sb.show();
                        return true;
                    } else return false;
                }
            });
        }
    }

    public void zoomOutButtonClick(View view) {
        if(!cameraFollow) {
            googleMap.animateCamera(CameraUpdateFactory.zoomOut());
        } else if(zoomLevel < 5) {
            Snackbar.make(view, "Zoom is at minimum.", Snackbar.LENGTH_SHORT).show();
        } else zoomLevel--;

    }

    public void bluetoothConnectSuccess(ImageView view) {
        view.setBackground(getDrawable(R.drawable.button_green));
    }

    public void bluetoothConnectFailure(ImageView view) {
        view.setBackground(getDrawable(R.drawable.button_red));
    }

    public void zoomInButtonClick(View view) {
        if(!cameraFollow) {
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        } else if(zoomLevel == 20) {
            Snackbar.make(view, "Zoom is at maximum.", Snackbar.LENGTH_SHORT).show();
        } else zoomLevel++;
    }

    public long calculateTimeRun(long startTime, long stopTime) {
        return stopTime - startTime; // assumes stopTime will always be later than start time
        // should be protected by the way times are saved
    }

    public float calculateDistanceRun(Location startLocation, Location endLocation) {
        return startLocation.distanceTo(endLocation);
    }

    public String formatTimeRun(long timeRunMillis) {
        String formatted = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(timeRunMillis),
                TimeUnit.MILLISECONDS.toSeconds(timeRunMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRunMillis))
        );
        return formatted;
    }

    public void EndRun(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setTitle("Confirm End Run");
        builder.setMessage("Warning: This will disconnect you from the device " +
                "and you will need to reconnect for the application & device to interact properly. " +
                "You will still be able to trigger an alert from the device's panic button.");
        builder.setPositiveButton("End Run", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked to confirm end-run
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        /** Run Statistics Calculation, TODO: Fix displaying by choosing another interface
         builder.setNeutralButton("Run Statistics", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        if(startTimeMillis != 0 && endTimeMillis != 0) {
        // calculate time run
        timeRun = calculateTimeRun(startTimeMillis, endTimeMillis);
        runTimeFormatted = formatTimeRun(timeRun);
        }
        if(startLocation != null && endLocation != null) {
        // calculate distance run in meters
        distanceRun = calculateDistanceRun(startLocation, endLocation);
        }

        if(runTimeFormatted != null && distanceRun != 0) {
        String statsMessage  = String.format("You ran for %s\n" +
        "and a distance of %02f", runTimeFormatted, distanceRun);
        }
        if(statsMessage != null) {
        Snackbar.make(getCurrentFocus(), statsMessage, Snackbar.LENGTH_INDEFINITE).show();
        } else Snackbar.make(getCurrentFocus(), "Run Not Complete", Snackbar.LENGTH_SHORT).show();
        }
        }); **/

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {

        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MapsActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(deviceAddress);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(sppUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    } // end ConnectBT class definition

    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
