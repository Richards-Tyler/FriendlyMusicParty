package com.example.tyler.friendlymusicparty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class JoinBluetooth extends Activity {

    public BluetoothAdapter mBluetoothAdapter;
    private ListView newDevicesListView;
	private ArrayList<String> deviceList;
    private BluetoothSocket socket;
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

        initialize();


        newDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mBluetoothAdapter.cancelDiscovery();
                final String info = ((TextView) view).getText().toString();

                String address = info.substring(info.length() - 19);

                BluetoothDevice connect_device = mBluetoothAdapter.getRemoteDevice(address);

                try {
                    socket = connect_device.createRfcommSocketToServiceRecord(my_UUID);
                    socket.connect();
					if(socket.isConnected()){
						Toast.makeText(getApplicationContext(), "You're connected!", Toast.LENGTH_SHORT).show();
					}
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

	public void updateList() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		List<String> s = new ArrayList<String>();
		for(BluetoothDevice bt: pairedDevices)
			s.add(bt.getName());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s);
		newDevicesListView.setAdapter(adapter);

	}

	public void initialize() {
		newDevicesListView = (ListView)findViewById(R.id.new_devices);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothAdapter.startDiscovery();
		updateList();
	}

    /***************************************************************************************************
     *
     * `              Listens for Devices and populates them to a ListView
     *
     **************************************************************************************************/

    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    deviceList.add(device.getName() + "\n" + device.getAddress());
					updateList();
                }


            }
        }
    };

    /***************************************************************************************************
     *
     * `              Cancel Discoverability
     *
     **************************************************************************************************/

    protected void onDestroy(){
        super.onDestroy();
        if(mBluetoothAdapter != null)
            mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
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
