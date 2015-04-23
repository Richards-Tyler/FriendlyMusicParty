package com.example.tyler.friendlymusicparty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class joinBluetooth extends Activity {

    public BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView newDevicesListView;

    /***************************************************************************************************
     *
     * `              onCreate joinBT
     *
     **************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_bluetooth);
        newDevicesListView = (ListView)findViewById(R.id.new_devices);

        mBluetoothAdapter.startDiscovery();

        mNewDevicesArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.activity_list_item);//populate listview soon

        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);

        mNewDevicesArrayAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


            }
        });

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
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                }


            }
        }
    };


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
