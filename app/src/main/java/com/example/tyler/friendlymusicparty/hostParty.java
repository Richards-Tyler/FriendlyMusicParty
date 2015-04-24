package com.example.tyler.friendlymusicparty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by zissou on 4/23/15.
 */
public class hostParty extends Activity {

    private ArrayList<String> priorities;
    private Button start, stop, addMusic;
    private ProgressBar progress;
    public BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView newDevicesListView;
    private final UUID my_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");

    /***************************************************************************************************
     *
     * `              onCreate hostBT
     *
     **************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_bluetooth);


        Toast.makeText(getApplicationContext(),
                " host Button is clicked", Toast.LENGTH_LONG).show();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter.startDiscovery();

        initializeComponents(savedInstanceState);
        addButtonHandlers();


    }
    public void initializeComponents(Bundle savedInstanceState) {

        //priorities = new ArrayList<>(items);


        progress = (ProgressBar) findViewById(R.id.hostPartyProgressBar);
        start = (Button) findViewById(R.id.hostPlayButton);
        stop = (Button) findViewById(R.id.stopButton);
        addMusic = (Button) findViewById(R.id.addMusicButton);

        progress.setMax(100); //change to song length
        progress.setProgress(0);



    }
    public void addButtonHandlers() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == start) {
                    Toast.makeText(getApplicationContext(),
                            "start is clicked", Toast.LENGTH_LONG).show();

                    progress.incrementProgressBy(1);
                } else if(v == stop) {

                    Toast.makeText(getApplicationContext(),
                            "stop Button is clicked", Toast.LENGTH_LONG).show();

                } else if (v == addMusic){
                    Toast.makeText(getApplicationContext(),
                            "add music Button is clicked", Toast.LENGTH_LONG).show();
                }



            }
        };
        start.setOnClickListener(listener);
        stop.setOnClickListener(listener);
        addMusic.setOnClickListener(listener);




    }


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
class AcceptThread extends Thread {

    public BluetoothAdapter mBluetoothAdapter;
    private BluetoothServerSocket mmServerSocket;
    private final UUID my_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");

    public AcceptThread() {
        BluetoothServerSocket socket = null;
        try {

            socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Friendly Music Party", my_UUID);
        } catch (IOException e){}
        mmServerSocket = socket;
    }

    public void run(){
        BluetoothSocket socket = null;

        while(true){
            try{
                socket = mmServerSocket.accept();
            }catch (IOException e) {
                break;
            }
            if(socket != null){
                //hostParty host =new hostParty(); this is where you call the method and pass the connection. I.e. useConnection(Socket);
                //mmServerSocket.close(); This closes the serversocket
                break;
            }
        }
    }

    public void cancel(){
        try {
            mmServerSocket.close();
        } catch(IOException e){}
    }
}

