package com.example.nicolas.smartride2.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.DataSensor;
import com.example.nicolas.smartride2.BDD.Run;
import com.example.nicolas.smartride2.BDD.Time;
import com.example.nicolas.smartride2.SessionManager;
import com.example.nicolas.smartride2.SettingsManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Valentin on 17/03/2017.
 */

public class BluetoothService {
    // Debugging
    private static final String TAG = "BluetoothService";

    // Name for the SDP record when creating server socket
    private static final String NAME = "Bluetooth";

    // Unique UUID for this application
    private static final UUID MY_UUID =
            UUID.fromString("f47bce9b-834b-487a-a8ac-477a494b605f");
    private static final UUID MY_SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private int mNewState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device


    String rxBuffer="";

    BDD bdd;
    static SessionManager session;
    static SettingsManager settings;
    DataSensor dataSensorAcc;
    DataSensor dataSensorGyro;
    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
        bdd = new BDD(context);
        session = new SessionManager(context);
        //init DataSensor Object
        dataSensorAcc= new DataSensor(null,session.getLoginPref(),null,null,null,null);
        dataSensorGyro= new DataSensor(null,session.getLoginPref(),null,null,null,null);
        settings = new SettingsManager(context);
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        mState = STATE_NONE;
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_NONE,STATE_NONE).sendToTarget();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_NONE,STATE_NONE).sendToTarget();
        // Start the service over to restart listening mode
        //BluetoothService.this.start();
        Log.i(TAG, "connectionFailed");

    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_NONE,STATE_NONE).sendToTarget();

        // Start the service over to restart listening mode
        //BluetoothService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME,
                            MY_UUID);
            } catch (IOException e) {
                Log.e(TAG,"listen() failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
            mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_LISTEN,STATE_LISTEN).sendToTarget();
            Log.d("acceptThread","acceptTread");
        }

        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG,"accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread");

        }

        public void cancel() {
            Log.d(TAG, "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG,"close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
           // try {
                    //tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                try {
                    tmp =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,Integer.valueOf(1));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                //tmp = mmDevice.getClass().getMethod("createRfcommSocket",new Class[] {int.class}).invoke()
                //Log.i(TAG,"create() sucesss");

            //} catch (IOException e) {
                //Log.i(TAG,"create() failed");
            //}
            mmSocket = tmp;

            if(mmSocket==null){
                Log.i(TAG,"mmSocket null !");
            }
            mState = STATE_CONNECTING;
            mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_CONNECTING,STATE_CONNECTING).sendToTarget();
            Log.d("ConnectThread","ConnectThread");
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();
            Log.i(TAG, "BEGIN mConnectThread 2");
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                //Log.i(TAG,Boolean.toString(mmSocket.isConnected()));
                //mmSocket.close();
                mmSocket.connect();
                Log.i(TAG, "BEGIN mConnectThread 3");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.i(TAG, "socket closed() ");
                } catch (IOException e2) {
                    Log.i(TAG, "unable to close() ");
                }
                Log.e(TAG,"failed to connect",e);
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
            Log.i(TAG, "BEGIN mConnectThread4");
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.i(TAG, "close() of connect socket failed");
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread: ");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
            mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,STATE_CONNECTED,STATE_CONNECTED).sendToTarget();
            Log.d("ConnectedThread","ConnectedThread");
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    if(settings.getStartManualRunPref()||settings.getStartMotionRunPref()) {
                        bytes = mmInStream.read(buffer);
                        String readMessage = new String(buffer);
                        readMessage = rxBuffer + readMessage;
                        rxBuffer = extractData(readMessage);
                        //Log.d("message recu","message recu");
                    }
                    //Log.d("message recu","rxBuffer="+rxBuffer+" size="+rxBuffer.length());
                   /* bdd.open();
                    List<DataSensor> dataSensorsA = new ArrayList<DataSensor>();
                    dataSensorsA=bdd.getAllDataAccWithRunAndProfil("Run#1",session.getLoginPref());
                    List<DataSensor> dataSensorsG = new ArrayList<DataSensor>();
                    dataSensorsG=bdd.getAllDataGyroWithRunAndProfil("Run#1",session.getLoginPref());
                    //Log.d("bdd","length Acc ="+dataSensorsA.size());
                    //Log.d("bdd","length Gyro ="+dataSensorsG.size());
                    bdd.close();*/
                   /* mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();*/
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                Log.d(TAG, "message send");
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

        private String extractData(String data) {
            String[] splitedString = data.split("");
            Log.d("ExtractData","lenght="+splitedString.length);
            int indexOfLastLetter=0;
            String lastString="";
            for (int i=0;i<splitedString.length;i++){
                //Log.d("ExtractData",Integer.toString(i));
                if(i>11) {
                    if (i + 1 < splitedString.length) {
                        if (splitedString[i].compareTo("A") == 0) {
                            if (splitedString[i + 1].compareTo("X") == 0) {
                                String dataAX = data.substring(i - 11, i - 1);
                                if(!dataAX.contains("A")&&!dataAX.contains("G")&&!dataAX.contains("X")&&!dataAX.contains("Y")&&!dataAX.contains("Z")) {
                                    if (dataAX.replace('\0','0').replace("0","")!="") {
                                        dataSensorAcc.setDataX(dataAX);
                                        Log.d("ExtractData", "dataAX=" + dataAX);
                                    }
                                }
                                indexOfLastLetter=i+1;
                            }
                            if (splitedString[i + 1].compareTo("Y") == 0) {
                                String dataAY = data.substring(i - 11, i - 1);
                                if(!dataAY.contains("A")&&!dataAY.contains("G")&&!dataAY.contains("X")&&!dataAY.contains("Y")&&!dataAY.contains("Z")) {
                                    if (dataAY.replace('\0','0').replace("0","")!="") {
                                        dataSensorAcc.setDataY(dataAY);
                                        Log.d("ExtractData", "dataAY=" + dataAY);
                                    }

                                }
                                indexOfLastLetter=i+1;
                            }
                            if (splitedString[i + 1].compareTo("Z") == 0) {
                                String dataAZ = data.substring(i - 11, i - 1);
                                if(!dataAZ.contains("A")&&!dataAZ.contains("G")&&!dataAZ.contains("X")&&!dataAZ.contains("Y")&&!dataAZ.contains("Z")) {
                                    //Log.d("ExtractData", "dataAZ=" + dataAZ);
                                    if (dataAZ.replace('\0','0').replace("0","")!="") {
                                        dataSensorAcc.setDataZ(dataAZ);
                                        Log.d("ExtractData", "dataAZ=" + dataAZ);
                                    }

                                }
                                indexOfLastLetter=i+1;
                            }
                        }
                        if (splitedString[i].compareTo("G") == 0) {
                            if (splitedString[i + 1].compareTo("X") == 0) {
                                String dataGX = data.substring(i - 11, i - 1);
                                if(!dataGX.contains("A")&&!dataGX.contains("G")&&!dataGX.contains("X")&&!dataGX.contains("Y")&&!dataGX.contains("Z")) {
                                    if (dataGX.replace('\0','0').replace("0","")!="") {
                                        dataSensorGyro.setDataX(dataGX);
                                        Log.d("ExtractData", "dataGX=" + dataGX);
                                    }
                                }

                                indexOfLastLetter=i+1;
                            }
                            if (splitedString[i + 1].compareTo("Y") == 0) {
                                String dataGY = data.substring(i - 11, i - 1);
                                if(!dataGY.contains("A")&&!dataGY.contains("G")&&!dataGY.contains("X")&&!dataGY.contains("Y")&&!dataGY.contains("Z")) {
                                    if (dataGY.replace('\0','0').replace("0","")!="") {
                                        dataSensorGyro.setDataY(dataGY);
                                        Log.d("ExtractData", "dataGY=" + dataGY);
                                    }
                                }

                                indexOfLastLetter=i+1;
                            }
                            if (splitedString[i + 1].compareTo("Z") == 0) {
                                String dataGZ = data.substring(i - 11, i - 1);
                                if(!dataGZ.contains("A")&&!dataGZ.contains("G")&&!dataGZ.contains("X")&&!dataGZ.contains("Y")&&!dataGZ.contains("Z")) {
                                    Log.d("ExtractData", "dataGZ=" + dataGZ);
                                    if (dataGZ.replace('\0','0').replace("0","")!="") {
                                        dataSensorGyro.setDataZ(dataGZ);
                                    }

                                }

                                indexOfLastLetter=i+1;
                            }
                        }
                    }
                }
                if(dataSensorAcc.getDataX()!=null && dataSensorAcc.getDataY()!=null && dataSensorAcc.getDataZ()!=null ){
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH) + 1;
                    int day = c.get(Calendar.DATE);
                    int hours = c.get(Calendar.HOUR);
                    int mins = c.get(Calendar.MINUTE);
                    int seconds = c.get(Calendar.SECOND);
                    int milliseconds = c.get(Calendar.MILLISECOND);
                    Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
                    dataSensorAcc.setTime(time);
                    bdd.open();
                    List<Run> listRun =  bdd.getAllRunWithProfil(session.getLoginPref());
                    int nbRunListP = listRun.size();
                    dataSensorAcc.setRunName(listRun.get(nbRunListP-1).getName());
                    Log.d("save data","nameRun="+listRun.get(nbRunListP-1).getName());
                    bdd.insertDataAcc(dataSensorAcc);
                    bdd.close();
                    //enregistrement dans la bdd
                    Log.d("save data","AccData save !");
                    dataSensorAcc= new DataSensor(null,session.getLoginPref(),null,null,null,null);
                }
                if(dataSensorGyro.getDataX()!=null && dataSensorGyro.getDataY()!=null && dataSensorGyro.getDataZ()!=null ){
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH) + 1;
                    int day = c.get(Calendar.DATE);
                    int hours = c.get(Calendar.HOUR);
                    int mins = c.get(Calendar.MINUTE);
                    int seconds = c.get(Calendar.SECOND);
                    int milliseconds = c.get(Calendar.MILLISECOND);
                    Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
                    dataSensorGyro.setTime(time);
                    bdd.open();
                    List<Run> listRun =  bdd.getAllRunWithProfil(session.getLoginPref());
                    int nbRunListP = listRun.size();
                    dataSensorGyro.setRunName(listRun.get(nbRunListP-1).getName());
                    Log.d("save data","nameRun="+listRun.get(nbRunListP-1).getName());
                    bdd.insertDataGyro(dataSensorGyro);
                    bdd.close();
                    //enregistrement dans la bdd
                    Log.d("save data","GyroData save !");
                    dataSensorGyro= new DataSensor(null,session.getLoginPref(),null,null,null,null);
                }
            }
            if(indexOfLastLetter+13>=splitedString.length){
                lastString=data.substring(indexOfLastLetter, indexOfLastLetter+(splitedString.length-1-indexOfLastLetter));
            }
            return lastString;
        }
    }
}
