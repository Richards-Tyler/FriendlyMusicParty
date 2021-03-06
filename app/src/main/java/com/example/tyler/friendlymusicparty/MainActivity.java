package com.example.tyler.friendlymusicparty;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    public BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private final UUID my_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");

    /***************************************************************************************************
     *
     * `                                         on create
     *
     **************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//Declare and get Adapter
        if(mBluetoothAdapter == null){//checks if bluetooth adapter is supported
            Toast.makeText(this, "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }

        else if (!mBluetoothAdapter.isEnabled()){//checks if bluetooth is off
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }


    }
    /***************************************************************************************************
     *
     * `                                         join Server
     *
     **************************************************************************************************/
    public void joinServer(View view){

        Intent intent = new Intent(this,joinBluetooth.class);
        startActivity(intent);

    }

    public void hostServer(View view){

        Intent intent = new Intent(this,hostParty.class);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
