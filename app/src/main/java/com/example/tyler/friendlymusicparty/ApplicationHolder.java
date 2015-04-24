package com.example.tyler.friendlymusicparty;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by tyler on 4/24/15.
 */
public class ApplicationHolder extends Application {
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket socket;
    public ApplicationHolder data = new ApplicationHolder();

    /***************************************************************************************************
     *
     * `              onCreate Application BluetoothSocket
     *
     **************************************************************************************************/

    public BluetoothSocket onCreate(Bundle savedInstanceState) {
       AcceptThread acceptThread = new AcceptThread();
       socket = acceptThread.getBTContext();
       //ApplicationHolder = acceptThread.getBTContext();
       return socket;
    }

    /***************************************************************************************************
     *
     * `              Our AcceptThread that creates the socket
     *
     **************************************************************************************************/

    public class AcceptThread extends Thread {

        public BluetoothAdapter mBluetoothAdapter;
        private BluetoothServerSocket mmServerSocket;
        private final UUID my_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public BluetoothSocket socket;

        public AcceptThread() {
            BluetoothServerSocket socket = null;
            try {

                socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Friendly Music Party", my_UUID);
            } catch (IOException e){}
            mmServerSocket = socket;
        }

        public void run(){
            socket = null;

            while(true){
                try{
                    socket = mmServerSocket.accept();
                }catch (IOException e) {
                    break;
                }
                if(socket != null){
                    //hostParty host =new hostParty(); this is where you call the method and pass the connection. I.e. useConnection(Socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        public void cancel(){
            try {
                mmServerSocket.close();
            } catch(IOException e){}
        }

        public BluetoothSocket getBTContext() {
            run();
            return socket;
        }
    }



}
