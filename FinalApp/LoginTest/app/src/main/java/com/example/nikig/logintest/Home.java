package com.example.nikig.logintest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Set;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends AppCompatActivity implements CreateEContactDialog.contactDialogListener {

    private Button buttonLogout;
    private Button startRunButton;

    private boolean isContact;

    private TextView msg_welcome;
    private Button viewContactInfoButton;
    private Button updateContactInfo;
    private TextView UserID;
    //create a runner instance
    private Runner mRunner;
    private Emergency possibleContact;

    String address;

    private Toast toast = null;

    // Declare app widgets
    Button btnPaired, mapsButton;
    ListView devicelist;

    // Declare bluetooth objects
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String BLUETOOTH_DEVICE_ADDRESS = "device_address";
    public ArrayList<String> contactNames, contactNums;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebase;
    private DatabaseReference mDatabase;

    //added this for troubleshooting
    private static String TAG = Home.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        isContact = false;

        //session check
        Auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = Auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

//        // check if user is an emergency contact
//        String userId = Auth.getCurrentUser().getUid();
//        checkIfContact(userId);


        startRunButton = (Button) findViewById(R.id.startRunButton);
        buttonLogout = (Button) findViewById(R.id.log_out);
        viewContactInfoButton = (Button) findViewById(R.id.userIdDisplay);
        msg_welcome = (TextView) findViewById(R.id.welcome);
        msg_welcome.setText("Welcome " + user.getDisplayName());
        updateContactInfo = (Button) findViewById(R.id.addContactButton);

        //created user and get current UID for their info
        String UID = user.getUid();
        //print to make sure accuracy
        Log.d(TAG, UID);
        //pass it into function to get user info with unique ID
        getUserInfo(UID);

        // logout button handler
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure you want to logout of Guardian?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Auth.signOut();
                        finish();
                        Intent intent = new Intent(Home.this, MainActivity.class);
                        startActivity(intent);
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
            }
        });


        viewContactInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, EmergencyCard.class);
                intent.putExtra("emergencyid", mRunner.getEmergencyid());
                startActivity(intent);
            }
        });


        //Calling widgets
        btnPaired = (Button) findViewById(R.id.findDevicesButton);
        devicelist = (ListView) findViewById(R.id.listView);

        // TODO: Pass array to maps function
        contactNames = new ArrayList<>();
        contactNums = new ArrayList<>();

        // set up bluetooth code
        // if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Show a message that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });


        updateContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, UpdateContactEmergency.class);
                intent.putExtra("emergencyid", mRunner.getEmergencyid());
                intent.putExtra("runnerid", user.getUid());
                startActivity(intent);

            }
        });

        startRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make an intent to start next activity.
                /** Jump to grabPhoneNo activity first **/

                Intent i = new Intent(Home.this, GrabPhoneNo.class);

                if(address != null) {
                    //Change the activity.
                    i.putExtra("address", address); //this will be received at receiving Activity
                    Log.d(TAG, mRunner.getEmergencyid());
                    i.putExtra("emergencyid", mRunner.getEmergencyid());
                    i.putExtra("runnerid", user.getUid());
                    startActivity(i);
                }
            }
        });
    }



//    private void checkIfContact(final String id) {
//        Log.d("Entering:",  "checkIfContact()");
//        DatabaseReference ref = mDatabase.child("emergency");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("Entering:", "onDataChange()");
//                if (dataSnapshot.hasChild(id)) {
//                    Log.d("Entered:", "dataSnapshot.hasChild(id)");
//                    // go to contact homepage instead
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            startActivity(new Intent(getApplicationContext(), EContactHome.class));
//                            finish();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("Canceled:", "checkIfContact() Canceled");
//            }
//        });
//    }

    /** TODO: ADD A CONNECTION STATUS BAR TO SHOW WHICH BLUETOOTH DEVICE IS SELECTED FROM THE LIST */

    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);

            /*
            // Make an intent to start next activity.
            Intent i = new Intent(Home.this, MapsActivity.class);

            //Change the activity.
            i.putExtra(BLUETOOTH_DEVICE_ADDRESS, address); //this will be received at receiving Activity
            startActivity(i);
            */
        }
    };

    // button callback for create emergency contact button
    public void addEmergencyContact(View view) {
        DialogFragment dialog = new CreateEContactDialog();
        dialog.show(getFragmentManager(), "Create Emergency Contact");
    }

    public void addEmergencyContactToList(String name, String number) {
        contactNames.add(name);
        contactNums.add(number);
    }

    private void getUserInfo(final String UID) {
        if(isContact) {
            return;
        }

        //referencing/ checking if that uid has a child
        DatabaseReference myRef = mDatabase.child("runner").child(UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG,dataSnapshot.getChildren(""));
                mRunner= dataSnapshot.getValue(Runner.class);

//                Log.d(TAG,"Runner= "+ mRunner.getEmergencyid());
//                Log.d(TAG,"Runner= "+ mRunner.getAge());
//                Log.d(TAG,"Runner= "+ mRunner.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    // CreateEContactDialog.contactDialogListener interface implementation
    @Override
    public void onContactDialogPositiveClick(DialogFragment dialog, String name, String number) {
        addEmergencyContactToList(name, number);
    }

    @Override
    public void onContactDialogNegativeClick(DialogFragment dialog){
        // do nothing
    }
}


//    private void logoutUser() {
//
//        Auth.signOut();
//        if (Auth.getCurrentUser() == null) {
//            startActivity(new Intent(Home.this, MainActivity.class));
//            finish();
//            toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_LONG);
//            toast.show();

// this listener will be called when there is change in firebase user session
//        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth Auth) {
//                FirebaseUser user = Auth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(Home.this, MainActivity.class));
//                    finish();
//                }
//            }





