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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.Time;
import com.example.nicolas.smartride2.BDD.User;
import com.example.nicolas.smartride2.Fragments.HomeFragment;
import com.example.nicolas.smartride2.Fragments.MapViewFragment;
import com.example.nicolas.smartride2.Fragments.OverviewFragment;
import com.example.nicolas.smartride2.Fragments.RecordFragment;
import com.example.nicolas.smartride2.Fragments.SendFragment;
import com.example.nicolas.smartride2.Fragments.SettingsFragment;
import com.example.nicolas.smartride2.Fragments.ShareFragment;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;

public class SmartRide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "debug";
    public Boolean connectionFlag = false;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_VOICE_RECOGNIZER = 1;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    public ProgressDialog progress;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String action;
    private int findDevice;
    SessionManager session;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private BDD bdd;
    public User user;
    public User utilisateurCo;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bdd = new BDD(this);
        //bdd.clearTable("TABLE_LOC");
        //bdd.clearTable("TABLE_PROFIL");

        session = new SessionManager(getApplicationContext());

        if (connectionFlag==null){
            connectionFlag=false;
        }
        //bdd.clearTable("TABLE_PROFIL");
//todo http://stackoverflow.com/questions/14940657/android-speech-recognition-as-a-service-on-android-4-1-4-2

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

        /*View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionFlag) {
                    Intent intent = new Intent(SmartRide.this, ProfilActivity.class);
                    //intent.putExtra("login",user.getLogin());
                    startActivity(intent);
                    overridePendingTransition(R.anim.profil_animation1, R.anim.profil_animation2);
                }
                else Toast.makeText(SmartRide.this, "You are not logged in.", Toast.LENGTH_SHORT).show();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Log.i(TAG, "getProfil: toast");
            }
        });*/
        System.out.println(connectionFlag);
        System.out.println(session.isLoggedIn());
        if ((session.isLoggedIn())==false){
            showBeginDialog();
        }
        else {
            System.out.println(session.getLoginPref());
            utilisateurCo = new User(session.getLoginPref());
            System.out.println(utilisateurCo.getLogin());
            connectionFlag=true;
            Toast.makeText(SmartRide.this, "Welcome back " + utilisateurCo.getLogin(), Toast.LENGTH_SHORT).show();


        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void showBeginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SmartRide.this);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showConnectDialog();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showNewAccDialog();
                dialog.cancel();
            }
        });

        builder.setIcon(R.drawable.logosmartrideg);
        builder.setTitle("Welcome on SmartRide !");
        builder.setMessage("Please create an account or log in.");
        builder.setCancelable(false);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showNewAccDialog() {
        final LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogcreate, null);
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Register");
        adb.setIcon(R.drawable.logosmartrideg);
        adb.setCancelable(false);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant1);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse1);
                String login = et1.getText().toString();
                String pswd = et2.getText().toString();
                //System.out.println(pswd);

                if (login.isEmpty() || pswd.isEmpty()) {
                    //showErrorDialog();
                    Toast.makeText(SmartRide.this, "Login or password empty", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showNewAccDialog();
                } else {
                    bdd.open();
                    // Log.w("NewAcount",bdd.getUserWithLogin(login).getLogin());
                    if (bdd.getUserWithLogin(login) == null) {
                        Log.w("NewAccount", "Login not found");
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int day = c.get(Calendar.DATE);
                        int hours = c.get(Calendar.HOUR);
                        int mins = c.get(Calendar.MINUTE);
                        int seconds = c.get(Calendar.SECOND);
                        int milliseconds = c.get(Calendar.MILLISECOND);
                        Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
                        user = new User(login, pswd, null, null, 0, time);

                        bdd.insertProfil(user);

                        Toast.makeText(SmartRide.this, "Account created successfuly !" + user.getLogin() + user.getPassword(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showConnectDialog();
                    } else {
                        Toast.makeText(SmartRide.this, "Account already existing, please choose an other one !", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showNewAccDialog();
                    }
                    bdd.close();

                }

            }


        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
                showBeginDialog();
            }
        });
        adb.show();
    }

    public void showConnectDialog() {

        Log.v("Connect dialog...", "Start");

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogconnect, null);
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setView(alertDialogView);

        adb.setTitle("Login");
        adb.setIcon(R.drawable.logosmartrideg);
        adb.setCancelable(false);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {

                EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse);

                String login = et1.getText().toString();
                String pswd = et2.getText().toString();

                //User utilisateurCo = new User(login,pswd,null,null,0,null);
                bdd.open();
                utilisateurCo = bdd.getUserWithLogin(login);
                bdd.close();

                if (utilisateurCo != null) {
                    Log.v("Connect", "Utilisateurco not null");
                    Log.v("Connect", utilisateurCo.getLogin() + utilisateurCo.getPassword() + login + pswd);
                    if (utilisateurCo.getPassword().equals(pswd)) {
                        Log.v("Connect", "Password correct");
                        connectionFlag=true;
                        if (utilisateurCo.getName() == null || utilisateurCo.getSurname() == null || utilisateurCo.getAge() == 0) {
                            dialog.cancel();
                            session.createLoginSession(login,connectionFlag);
                            System.out.println(session.isLoggedIn());
                            System.out.println(session.getLoginPref());
                            showInfoDialog(utilisateurCo);
                            Toast.makeText(SmartRide.this, "Connection successful !", Toast.LENGTH_SHORT).show();

                        } else {
                            session.createLoginSession(login,connectionFlag);
                            dialog.cancel();
                            Toast.makeText(SmartRide.this, "Connection successful !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SmartRide.this, "Login or password incorrect, please try again !", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showConnectDialog();
                        Log.v("Connect dialog", "startcom1");
                    }
                } else {
                    Toast.makeText(SmartRide.this, "No user found, a problem has happened...", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showConnectDialog();
                    Log.v("Connect dialog", "startcom2");
                }

            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
                showBeginDialog();
            }
        });
        adb.show();
    }

    /*public void showErrorDialog() {

        final LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogerror, null);

        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        adb.setView(alertDialogView);
        adb.setTitle("Error...");
        adb.setIcon(R.drawable.logosmartrideg);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
    }*/

    public void showInfoDialog(final User user) {

        /*bdd.open();
        bdd.insertProfil(user);
        bdd.close();*/

        final LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialoginfo, null);

        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        adb.setView(alertDialogView);
        adb.setTitle("Information");

        adb.setIcon(R.drawable.logosmartrideg);
        adb.setCancelable(false);


        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EditText et1 = (EditText) alertDialogView.findViewById(R.id.name);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.surname);
                EditText et3 = (EditText) alertDialogView.findViewById(R.id.age);

                String nameConnect = et1.getText().toString();
                String surnameConnect = et2.getText().toString();
                int ageConnect;
                if (et3.getText().toString().isEmpty()) {
                    ageConnect = 0;
                } else ageConnect = Integer.parseInt(et3.getText().toString());

                if (nameConnect.isEmpty() || surnameConnect.isEmpty() || ageConnect == 0) {

                    Toast.makeText(SmartRide.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    showInfoDialog(user);

                } else {
                    user.setName(nameConnect);
                    user.setSurname(surnameConnect);
                    user.setAge(ageConnect);
                    bdd.open();
                    bdd.updateProfil(user);
                    bdd.close();
                    Log.v("Information", user.getLogin() + " " + user.getPassword() + " " + user.getAge() + " " + user.getName() + " " + user.getSurname() + " " + user.getCreationDate());
                    Toast.makeText(SmartRide.this, "Information saved !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                }

            }


        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                showConnectDialog();
            }
        });
        adb.show();
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
            fm.beginTransaction().replace(R.id.frame, new SettingsFragment()).commit();
            setTitle(getString(R.string.action_settings));
            return true;
        } else if (id == R.id.action_bluetooth) {

            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(SmartRide.this, "Your device does not support Bluetooth :(", Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            if (mBluetoothAdapter.isEnabled()) {
                //make something
                findDevice();
            }

            return true;
        } else if (id == R.id.action_profile) {
            if(connectionFlag) {
                Intent intent = new Intent(SmartRide.this, ProfilActivity.class);
                intent.putExtra("User_for_BDD",utilisateurCo.getLogin());
                startActivity(intent);
                overridePendingTransition(R.anim.profil_animation1, R.anim.profil_animation2);
            }
            /*Intent intent = new Intent(this, ProfilActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            System.out.println(utilisateurCo.getLogin());
            intent.putExtra("User_for_BDD",utilisateurCo.getLogin());
            startActivity(intent);
            fm.beginTransaction().replace(R.id.frame, new ProfileFragment()).commit();
            setTitle(getString(R.string.action_profile));*/
            return true;
        } else if (id == R.id.action_logout) {
            connectionFlag=false;
            session.setIsLoggedIn(connectionFlag);
            session.logoutUser();
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
                Toast.makeText(SmartRide.this, "Recording data impossible without Bluetooth connection...", Toast.LENGTH_SHORT).show();
            }


        }

        }



    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            Log.i("BT", intent.getAction());
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                /*String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address*/
                if (device.getName() != null && !mDeviceList.contains(device.getName() + "\n" + device.getAddress())) {
                    mDeviceList.add(device.getName() + "\n" + device.getAddress());
                }
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                //Toast.makeText(SmartRide.this,"yo", Toast.LENGTH_SHORT).show();
                if (findDevice == 1) {
                    listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
                    //TODO Expandable List item nom du device et sub item adresse mac
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
            fm.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            setTitle(getString(R.string.action_home));
        } else if (id == R.id.record) {
            fm.beginTransaction().replace(R.id.frame, new RecordFragment()).commit();
            setTitle(getString(R.string.action_record));
        } else if (id == R.id.overview) {
            fm.beginTransaction().replace(R.id.frame, new OverviewFragment()).commit();
            setTitle(getString(R.string.action_overview));
        } else if (id == R.id.map) {
            fm.beginTransaction().replace(R.id.frame, new MapViewFragment()).commit();
            setTitle(getString(R.string.action_map));
        } else if (id == R.id.nav_share) {
            fm.beginTransaction().replace(R.id.frame, new ShareFragment()).commit();
            setTitle(getString(R.string.action_share));
        } else if (id == R.id.nav_send) {
            fm.beginTransaction().replace(R.id.frame, new SendFragment()).commit();
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

    public void findDevice() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            progress = ProgressDialog.show(SmartRide.this, "Finding Bluetooth device...",
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
                        public void run() {
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SmartRide.this);
                                // Add the buttons
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        findDevice = 0;
                                        mBluetoothAdapter.cancelDiscovery();
                                        mDeviceList.clear();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        findDevice = 0;
                                        mBluetoothAdapter.cancelDiscovery();
                                        mDeviceList.clear();
                                    }
                                });

                                builder.setIcon(R.drawable.bluetooth);

                                builder.setTitle("Bluetooth devices");

                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.bluetooth_device_list, null);


                                listView = (ListView) convertView.findViewById(R.id.listViewBluetoothDevice);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmartRide.this, android.R.layout.simple_list_item_1, mDeviceList);
                                listView.setAdapter(adapter);

                                builder.setView(convertView);

                                // Create the AlertDialog
                                AlertDialog dialog = builder.create();

                                dialog.show();
                                //dialog.getWindow().setLayout(700, 600);
                                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                findDevice = 1;
                                progress.dismiss();

                            } else {
                                Toast.makeText(SmartRide.this, "No Bluetooth device found...", Toast.LENGTH_SHORT).show();
                                findDevice = 0;
                                mBluetoothAdapter.cancelDiscovery();
                                progress.dismiss();
                            }


                        }

                    });
                }
            }).start();


        } else
            Toast.makeText(SmartRide.this, "Application can't work fine without access to the location...", Toast.LENGTH_SHORT).show();
        findDevice = 0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    /*public User getUser() {
        return User;
    }*/


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SmartRide Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        client2.disconnect();
    }*/
}

//TODO Faire un tableau de données des capteurs avec valeurs et temps