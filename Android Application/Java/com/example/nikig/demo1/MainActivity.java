package com.example.nikig.demo1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> listAdapter;
    Button connectNew;
    ListView listView;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(btAdapter== null){
            Toast.makeText(getApplicationContext(), "No Bluetooth Detected", 0).show();
            //Toast.makeText(getApplicationContext(),"No Bluetooth Detected", 0).show();

            finish();
        }
        else{
            if(!btAdapter.isEnabled()){
                Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
            getPairedDevices();
        }

    }

    private void getPairedDevices()
    {
        devicesArray= btAdapter.getBondedDevices();
        if(devicesArray.size()>0){
            for(BluetoothDevice device:devicesArray){
                listAdapter.add(device.getName()+ "\n"+device.getAddress());
            }
        }
    }

    private void init() {
        connectNew=(Button)findViewById(R.id.bConnectionNew);
        listView=(ListView)findViewById(R.id.listView);
        listAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,0);
        listView.setAdapter(listAdapter);
        btAdapter= BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"Bluetooth must be Enabled to continue", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

