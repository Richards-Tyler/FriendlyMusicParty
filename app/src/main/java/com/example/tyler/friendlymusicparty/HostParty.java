package com.example.tyler.friendlymusicparty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by zissou on 4/23/15.
 */
public class HostParty extends Activity  implements MediaPlayer.OnCompletionListener{

    private ArrayList<String> priorities;
    private ArrayList<HashMap<String, String>> songsList;
    private MediaPlayer mp;
    private Button start, stop, vetoSong;
    private ProgressBar progress;
    public BluetoothAdapter mBluetoothAdapter;
    private MusicLibrary library;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView newDevicesListView;
    private TextView songTitleLabel;
    private Handler mHandler = new Handler();
    private Utilities utils;
    private int currentSongIndex = 0;
    private int nextSongIndex = 0;
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

        ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();


        library = new MusicLibrary();
        mp = new MediaPlayer();
        utils = new Utilities();

        mp.setOnCompletionListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter.startDiscovery();

        // get all songs from sdcard
        this.songsList = library.getPlayList(getApplicationContext());


        songTitleLabel = (TextView) findViewById(R.id.currentSongTextView);

        for(HashMap<String, String> map : songsList) {
            HashMap<String, String> song = map;

            songsListData.add(song);
        }

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.playlist_item, new String[] { "songTitle" }, new int[] {
                R.id.songTitle });

        ListView mList = (ListView) findViewById(R.id.hostMusicListView);

        mList.setAdapter(adapter);

        // selecting single ListView item

        // listening to single listitem click
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting listitem index
                int songIndex = position;
                if(!mp.isPlaying()) {
                    currentSongIndex = 0;
                    nextSongIndex = position;

                }else {
                    nextSongIndex = position;
                }

                String songTitle = songsList.get(songIndex).get("songTitle");
                Toast.makeText(getApplicationContext(),
                        "next song: " + songTitle, Toast.LENGTH_LONG).show();
            }
        });

        initializeComponents(savedInstanceState);
        addButtonHandlers();


    }



    public void initializeComponents(Bundle savedInstanceState) {

        //priorities = new ArrayList<>(items);


        progress = (ProgressBar) findViewById(R.id.hostPartyProgressBar);
        start = (Button) findViewById(R.id.hostPlayButton);
        stop = (Button) findViewById(R.id.stopButton);
        vetoSong = (Button) findViewById(R.id.vetoSongButton);


        progress.setMax(100); //change to song length
        progress.setProgress(0);



    }
    public void addButtonHandlers() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == start) {
                    if(mp.isPlaying()){
                        if(mp!=null){

                            mp.pause();
                            start.setText("Play");

                        }else{
                            playSong(currentSongIndex);
                        }
                    }else{
                        // Resume song
                        if(mp!=null){

                            mp.start();
                            start.setText("Pause");

                        }else {
                            playSong(currentSongIndex);
                        }
                    }



                } else if(v == stop) {

                    mp.stop();
                    start.setText("Play");

                } else if (v == vetoSong){

                    Toast.makeText(getApplicationContext(),
                            "veto", Toast.LENGTH_LONG).show();

                    if(currentSongIndex != nextSongIndex) {
                        Random rand = new Random();
                        mp.stop();
                        playSong(nextSongIndex);

                        currentSongIndex = nextSongIndex;
                        nextSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;

                    }else {
                        if (currentSongIndex < (songsList.size() - 1)) {
                            playSong(currentSongIndex + 1);
                            currentSongIndex = currentSongIndex + 1;
                        } else {
                            // play first song
                            playSong(0);
                            currentSongIndex = 0;
                        }

                    }
                }



            }
        };
        start.setOnClickListener(listener);
        stop.setOnClickListener(listener);
        vetoSong.setOnClickListener(listener);




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

    public void  playSong(int songIndex){

        try {
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();

            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            progress.setProgress(0);
            progress.setMax(100);

            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(nextSongIndex != currentSongIndex) {
            currentSongIndex = nextSongIndex;
            playSong(nextSongIndex);
        }else {
             // Implement get audio from player two bluetooth
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else {
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mp.release();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            //songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            //songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            int pr = (int)(utils.getProgressPercentage(currentDuration, totalDuration));

            progress.setProgress(pr);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
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

