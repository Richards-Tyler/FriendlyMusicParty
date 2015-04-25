package com.example.tyler.friendlymusicparty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class JoinBluetooth extends Activity {

    public BluetoothAdapter mBluetoothAdapter;
    private ListView newDevicesListView;
	private ArrayList<String> deviceList;
    private List<String> discoverableDevicesList;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothServerSocket mBluetoothServerSocket;
    private BroadcastReceiver mReceiver;
    private final UUID my_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");

    /***************************************************************************************************
     *
     * `              onCreate joinBT
     *
     **************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_bluetooth);
        discoverableDevicesList = new ArrayList<String>();

        initialize();


        newDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "item click" , Toast.LENGTH_SHORT).show();

                mBluetoothAdapter.cancelDiscovery();
                final String info = ((TextView) view).getText().toString();

                String address = info.substring(info.length() - 19);

                BluetoothDevice connect_device = mBluetoothAdapter.getRemoteDevice("0C:71:5D:FA:20:CC");

                try {
                    mBluetoothSocket= connect_device.createRfcommSocketToServiceRecord(my_UUID);
                   // socket.connect();
					//if(socket.isConnected()){
					//	Toast.makeText(getApplicationContext(), "You're connected!", Toast.LENGTH_SHORT).show();
				//	}
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

	public void updateList() {
		//Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //List<String> discoverableDevicesList

        System.out.println("in Update");
		List<String> s = new ArrayList<String>();
		for(String bt: discoverableDevicesList) {
            System.out.println("in device list");
            s.add(bt);
            System.out.println("Device " + bt);
        }


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s);
		newDevicesListView.setAdapter(adapter);

	}

	public void initialize() {
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        //final List<String> discoverableDevicesList = new ArrayList<String>();
        Toast.makeText(getApplicationContext(), "initialize! address is " +  mBluetoothAdapter.getAddress(), Toast.LENGTH_SHORT).show();

        /***************************************************************************************************
         *
         * `              Listens for Devices and populates them to a ListView
         *
         **************************************************************************************************/

        mReceiver = new BroadcastReceiver() {

                public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Toast.makeText(getApplicationContext(), "in receiver!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "intent " + action, Toast.LENGTH_LONG).show();
                // When discovery finds a device
               // if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                    // Add the name and address to an array adapter to show in a ListView
                    //System.out.println(device.getName());
                    Toast.makeText(getApplicationContext(), "device found! name is " + device.getName(), Toast.LENGTH_SHORT).show();
                    System.out.println("Device address is " + device.getAddress());
                    discoverableDevicesList.add(device.getName() + "\n" + device.getAddress() + "\n" + rssi);
                    for(String s : discoverableDevicesList)
                        System.out.println("Device list address is " + s);


               // }
            }

        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        JoinBluetooth.this.getApplicationContext().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discoverableDevicesList);
        newDevicesListView.setAdapter(adapter);



        System.out.print("set adapter");
    }
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        //filter.addAction(ClientParty.BROADCAST_ACTION);
        registerReceiver(mReceiver, filter);
        //mBluetoothAdapter.startDiscovery();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        //mBluetoothAdapter.cancelDiscovery();
        super.onPause();
    }
    /***************************************************************************************************
     *
     * `              Cancel Discoverability
     *
     **************************************************************************************************/

    protected void onDestroy(){
        super.onDestroy();
        if(mBluetoothAdapter != null)
            mBluetoothAdapter.cancelDiscovery();
        //unregisterReceiver(mReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
