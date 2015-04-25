package com.example.tyler.friendlymusicparty;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
/**
 * Created by zissou on 4/24/15.
 */
public class ApplicationHolder extends Application {
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket socket;
    public ApplicationHolder data;
    public BluetoothServerSocket serverSocket;
    private AcceptThread acceptThread;

    /***************************************************************************************************
     *
     * `              onCreate Application BluetoothSocket
     *
     **************************************************************************************************/

    public void onCreate(Bundle savedInstanceState) {

        acceptThread = new AcceptThread();
        data = new ApplicationHolder();
        socket = acceptThread.getBTContext();
        serverSocket = acceptThread.getMmServerSocket();

    }

    /***************************************************************************************************
     *
     *              Our AcceptThread that creates the socket
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
                Toast.makeText(getApplicationContext(),
                        "UUID: " + my_UUID, Toast.LENGTH_LONG).show();
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
        public BluetoothServerSocket getMmServerSocket(){
            run();
            return mmServerSocket;
        }

        public BluetoothSocket getBtSocket() {
            return socket;
        }

        public BluetoothServerSocket getServSock() {
            return mmServerSocket;
        }
    }

    public AcceptThread getAcceptThread() {
        return this.acceptThread;
    }

}
