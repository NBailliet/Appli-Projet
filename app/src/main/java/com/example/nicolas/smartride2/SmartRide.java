package com.example.nicolas.smartride2;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SmartRide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_ENABLE_BT = 0;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    public ProgressDialog progress;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String action;
    private int findDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findDevice = 0;
///////demande permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Cela signifie que la permission à déjà était
                //demandé et l'utilisateur l'a refusé
                //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                //cette permission est nécessaire et la redemander
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
/////////////////////////////
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));


        // Register for broadcasts when a device is discovered.

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.smart_ride, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fm.beginTransaction().replace(R.id.frame,new SettingsFragment()).commit();
            setTitle(getString(R.string.action_settings));
            return true;
        } else if (id == R.id.action_bluetooth) {

            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(SmartRide.this, "Your device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            if (mBluetoothAdapter.isEnabled()) {
                //make something
                findDevice();
            }



        }
        else if (id == R.id.action_bluetooth) {
            fm.beginTransaction().replace(R.id.frame,new BluetoothFragment()).commit();
            setTitle(getString(R.string.action_bluetooth));
            return true;
        }
        else if (id == R.id.action_profile) {
            fm.beginTransaction().replace(R.id.frame,new ProfileFragment()).commit();
            setTitle(getString(R.string.action_profile));
            return true;
        } else if (id == R.id.action_quitter) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                findDevice();
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(SmartRide.this, "Recording Data impossible without Bluetooth connection...", Toast.LENGTH_SHORT).show();
            }


        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            Log.i("BT",intent.getAction());
            if (BluetoothDevice.ACTION_FOUND.equals(action) ) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                /*String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address*/
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                //Toast.makeText(SmartRide.this,"yo", Toast.LENGTH_SHORT).show();
                if(findDevice==1) {
                    listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
                    //TODO comparer les éléments de la list pour ne pas ajouter plusieurs fois le même device
                    //TODO un meilleur design pour la listView
                    //TODO Implémenter l'action de se connecter aux devices
                    //TODO Conditions pour ne pouvoir se connecter que au skis
                }
            }

        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.home) {
            fm.beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
            setTitle(getString(R.string.action_home));
        } else if (id == R.id.record) {
            fm.beginTransaction().replace(R.id.frame,new RecordFragment()).commit();
            setTitle(getString(R.string.action_record));
        } else if (id == R.id.overview) {
            fm.beginTransaction().replace(R.id.frame,new OverviewFragment()).commit();
            setTitle(getString(R.string.action_overview));
        } else if (id == R.id.map) {
            fm.beginTransaction().replace(R.id.frame,new MapFragment()).commit();
            setTitle(getString(R.string.action_map));
        } else if (id == R.id.nav_share) {
            fm.beginTransaction().replace(R.id.frame,new ShareFragment()).commit();
            setTitle(getString(R.string.action_share));
        } else if (id == R.id.nav_send) {
            fm.beginTransaction().replace(R.id.frame,new SendFragment()).commit();
            setTitle(getString(R.string.action_send));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie
                } else {
                    // La permission est refusée
                }
                return;
            }
        }
    }

    public void findDevice(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            progress = ProgressDialog.show(SmartRide.this, "Find Bluetooth Device",
                    "Please wait...", true);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    mBluetoothAdapter.startDiscovery();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            if(BluetoothDevice.ACTION_FOUND.equals(action) ) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SmartRide.this);
                                // Add the buttons
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        findDevice=0;
                                        mBluetoothAdapter.cancelDiscovery();
                                        mDeviceList.clear();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        findDevice=0;
                                        mBluetoothAdapter.cancelDiscovery();
                                        mDeviceList.clear();
                                    }
                                });

                                builder.setIcon(R.drawable.bluetooth);

                                builder.setTitle("Bluetooth Devices");

                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.bluetooth_device_list, null);
                                builder.setView(convertView);


                                // Create the AlertDialog
                                AlertDialog dialog = builder.create();

                                listView = (ListView) convertView.findViewById(R.id.listView1);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmartRide.this,android.R.layout.simple_list_item_1,mDeviceList);
                                listView.setAdapter(adapter);

                                dialog.show();
                                findDevice = 1;
                                progress.dismiss();

                            }else  {Toast.makeText(SmartRide.this, "No Bluetooth device found...", Toast.LENGTH_SHORT).show();
                                findDevice=0;
                                mBluetoothAdapter.cancelDiscovery();
                                progress.dismiss();
                            }



                        }

                    });
                }
            }).start();


        }
        else Toast.makeText(SmartRide.this, "Application can't work fine without access to the location...", Toast.LENGTH_SHORT).show(); findDevice=0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }
}