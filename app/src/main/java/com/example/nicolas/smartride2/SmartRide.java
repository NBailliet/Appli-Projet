package com.example.nicolas.smartride2;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.Time;
import com.example.nicolas.smartride2.BDD.User;
import com.example.nicolas.smartride2.Fragments.HomeFragment;
import com.example.nicolas.smartride2.Fragments.MapViewFragment;
import com.example.nicolas.smartride2.Fragments.OverviewFragment;
import com.example.nicolas.smartride2.Fragments.RecordFragment;
import com.example.nicolas.smartride2.Fragments.SettingsFragment;
import com.example.nicolas.smartride2.Services.BluetoothService;
import com.example.nicolas.smartride2.Services.Constants;
import com.example.nicolas.smartride2.Services.LocalService;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
    private BluetoothService mBTService = null;
    private ListView listViewBluetoothDevices;
    private ListView listViewProfiles;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private ArrayList<String> mProfilesList = new ArrayList<String>();
    public ProgressDialog progress;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String action;
    SessionManager session;
    static SettingsManager settings;
    String rxBuffer="";
    private BDD bdd;
    public User user;
    public User utilisateurCo;


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

        if (!isMyServiceRunning(LocalService.class)) {
            settings = new SettingsManager(getApplicationContext());
        }

        if (connectionFlag==null){
            connectionFlag=false;
        }

//todo http://stackoverflow.com/questions/14940657/android-speech-recognition-as-a-service-on-android-4-1-4-2

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

    }


    public void showBeginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SmartRide.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SmartRide.this, android.R.layout.simple_list_item_1);
        bdd.open();
        List<User> list = bdd.getAllProfil();
        bdd.close();
        int i =0;

        while(i<list.size()){
            mProfilesList.add(list.get(i).getLogin());
            i++;
        }

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.profiles_list, null);

        listViewProfiles = (ListView) convertView.findViewById(R.id.listViewProfiles);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmartRide.this, android.R.layout.simple_list_item_1, mProfilesList);
        View footerView = getLayoutInflater().inflate(R.layout.new_account_item_custom, null);
        listViewProfiles.addFooterView(footerView);
        listViewProfiles.setAdapter(adapter);
        builder.setView(convertView);
        builder.setIcon(R.drawable.logosmartrideg);
        builder.setTitle("Welcome on SmartRide !");
        builder.setMessage("Please create an account or log in.");
        builder.setCancelable(false);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        listViewProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) listViewProfiles.getItemAtPosition(position);

                if(temp!=null){
                    showConnectDialog(temp);
                    mProfilesList.clear();
                    dialog.cancel();
                    //Toast.makeText(SmartRide.this, temp, Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(SmartRide.this, "footview", Toast.LENGTH_SHORT).show();
                    showNewAccDialog();
                    mProfilesList.clear();
                    dialog.cancel();
                }

            }
        });


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
                        showConnectDialog(login);
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

    public void showConnectDialog(final String log) {

        Log.v("Connect dialog...", "Start");

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogconnect, null);
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        EditText editText = (EditText) alertDialogView.findViewById(R.id.identifiant);
        editText.setText(log, TextView.BufferType.EDITABLE);
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
                            //settings.setRunPref(false);
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
                        showConnectDialog(log);
                        Log.v("Connect dialog", "startcom1");
                    }
                } else {
                    Toast.makeText(SmartRide.this, "No user found, a problem has happened...", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showConnectDialog(log);
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

    public void showInfoDialog(final User user) {

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
                //showConnectDialog();
                showBeginDialog();
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

        //TODO GERER BACK OPTIONS VERS DRAWER
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
                // Get a set of currently paired devices
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // Initialize the BluetoothChatService to perform bluetooth connections
                mBTService = new BluetoothService(this,mHandler);
                //ensureDiscoverable();

                List<BluetoothDevice> listPairedDevices = new ArrayList<>();
                listPairedDevices.addAll(pairedDevices);
                List<String> listNamePairedDevices = new ArrayList<>();
                Log.i(TAG,Integer.toString(listPairedDevices.size()));
                int i=0;
                while(i<listPairedDevices.size()){
                    listNamePairedDevices.add(listPairedDevices.get(i).getName());
                    Log.i(TAG,listPairedDevices.get(i).getName());
                    i++;
                }
                i=0;
               if (pairedDevices.size() > 0 && (listNamePairedDevices.contains("SkiRight") || listNamePairedDevices.contains("SkiLeft"))) {
                   if(listNamePairedDevices.contains("SkiRight")){
                       //lancer connection ski droit
                       //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                       // Attempt to connect to the device
                       /*if(device!=null) {
                           mBTService.connect(device);
                       }*/
                   }else{
                       findDevice();
                       Toast.makeText(SmartRide.this, "Right Ski not paired", Toast.LENGTH_SHORT).show();
                   }
                   if(listNamePairedDevices.contains("SkiLeft")){
                       //lancer connection ski droit
                       //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                       // Attempt to connect to the device
                       /*if(device!=null) {
                           mBTService.connect(device);
                       }*/
                   }else{
                       findDevice();
                       Toast.makeText(SmartRide.this, "Left Ski not paired", Toast.LENGTH_SHORT).show();
                   }

                } else {
                    findDevice();
                //mBTService.start();//todo voir si c utile
               }
            }

            return true;
        } else if (id == R.id.action_profile) {
            if(connectionFlag) {
                Intent intent = new Intent(SmartRide.this, ProfilActivity.class);
                intent.putExtra("User_for_BDD",utilisateurCo.getLogin());
                startActivity(intent);
                overridePendingTransition(R.anim.profil_animation1, R.anim.profil_animation2);
            }
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
                mBTService = new BluetoothService(this,mHandler);
                //ensureDiscoverable();
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
                if (/*device.getName() != null &&*/ !mDeviceList.contains(device.getName() + "\n" + device.getAddress())) {
                    mDeviceList.add(device.getName() + "\n" + device.getAddress());
                }
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                //Toast.makeText(SmartRide.this,"yo", Toast.LENGTH_SHORT).show();
                if (listViewBluetoothDevices !=null) {
                    listViewBluetoothDevices.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
                    //TODO Expandable List item nom du device et sub item adresse mac
                    //TODO un meilleur design pour la listViewBluetoothDevices
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
            //if (findDevice == 0) {
            fm.beginTransaction().replace(R.id.frame, new RecordFragment()).commit();
            setTitle(getString(R.string.action_record));
        } else if (id == R.id.overview) {
            fm.beginTransaction().replace(R.id.frame, new OverviewFragment()).commit();
            setTitle(getString(R.string.action_overview));
        } else if (id == R.id.map) {
            fm.beginTransaction().replace(R.id.frame, new MapViewFragment()).commit();
            setTitle(getString(R.string.action_map));
        } else if (id == R.id.nav_send) {
            setTitle(getString(R.string.action_send));

            /*Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));*/
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "SmartRide Data");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
            File root = Environment.getExternalStorageDirectory();
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(root.getAbsolutePath() + "/DCIM/100MEDIA/IMG0398.jpg"))); //ATTENTION CHANGER NOM FICHIER JPG POUR TEST !!!
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, you can find attached my recent results with SmartRide system !");

            startActivity(Intent.createChooser(intent, "Send Email"));
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

                    long start = System.currentTimeMillis();
                    long end = start + 8*1000; // 60 seconds * 1000 ms/sec
                    Log.i("BT", start + "   " + end);
                    while((!BluetoothDevice.ACTION_FOUND.equals(action)) && (System.currentTimeMillis() < end)){
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
                                            mBluetoothAdapter.cancelDiscovery();
                                            mDeviceList.clear();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                            mBluetoothAdapter.cancelDiscovery();
                                            mDeviceList.clear();
                                        }
                                    });

                                    builder.setIcon(R.drawable.bluetooth);

                                    builder.setTitle("Bluetooth devices");
                                    builder.setMessage("Please select a device");

                                    LayoutInflater inflater = getLayoutInflater();
                                    View convertView = (View) inflater.inflate(R.layout.bluetooth_device_list, null);


                                    listViewBluetoothDevices = (ListView) convertView.findViewById(R.id.listViewBluetoothDevice);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SmartRide.this, android.R.layout.simple_list_item_1, mDeviceList);
                                    listViewBluetoothDevices.setAdapter(adapter);

                                    builder.setView(convertView);

                                    // Create the AlertDialog
                                    AlertDialog dialog = builder.create();

                                    dialog.show();
                                    progress.dismiss();

                                    listViewBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            String temp = (String) listViewBluetoothDevices.getItemAtPosition(position);
                                            Toast.makeText(SmartRide.this, temp, Toast.LENGTH_SHORT).show();
                                            String address = temp.substring(temp.length() - 17);
                                            Log.i("BT find", "find device adress mac is =" + address);
                                            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                                            // Attempt to connect to the device
                                            if(device!=null) {
                                                mBTService.connect(device);
                                            }

                                        }
                                    });

                                } else {
                                    Toast.makeText(SmartRide.this, "No Bluetooth device found...", Toast.LENGTH_SHORT).show();
                                    mBluetoothAdapter.cancelDiscovery();
                                    progress.dismiss();
                                }

                        }

                    });
                }
            }).start();


        } else
            Toast.makeText(SmartRide.this, "Application can't work fine without access to the location...", Toast.LENGTH_SHORT).show();

    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    //byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    //String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    Log.d("Write","message envoyé");
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                   // mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                   Log.d("message recu",readMessage);
                   //Toast.makeText(SmartRide.this, readMessage, Toast.LENGTH_SHORT).show();
                    //rxBuffer=rxBuffer+readMessage;
                    //Log.d("message recu","rxBuffer="+rxBuffer+" size="+rxBuffer.length());
                    if (rxBuffer.contains("AX") && rxBuffer.contains("GX")&& rxBuffer.contains("AY") && rxBuffer.contains("GY") && rxBuffer.contains("AZ") && rxBuffer.contains("GZ")){
                        //testBuffer(rxBuffer);
                        // Toast.makeText(SmartRide.this, rxBuffer, Toast.LENGTH_SHORT).show();
                        //Log.d("full data",rxBuffer + "   size ="+rxBuffer.length());
                        rxBuffer="";
                    }
                    //SmartRide.this.sendMessage(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                   String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != SmartRide.this) {
                        Toast.makeText(SmartRide.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != SmartRide.this) {
                        Toast.makeText(SmartRide.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };



    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
            startActivity(discoverableIntent);
        }
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBTService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(SmartRide.this,"not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBTService.write(send);
        }
    }


    private void testBuffer(String data) {
        data.
        if (data.contains("A")) {
            if (data.contains("X")) {
                //Log.d("testBufferAX",data);
            }
            if (data.contains("Y")) {
                //Log.d("testBufferAY",data);
            }
            if (data.contains("Z")) {
               // Log.d("testBufferAZ",data);
            }

        }
        if (data.contains("G")) {
            if (data.contains("X")) {
                //Log.d("testBufferGX",data);
            }
            if (data.contains("Y")) {
                //Log.d("testBufferGY",data);
            }
            if (data.contains("Z")) {
               // Log.d("testBufferGZ",data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onDestroy(LocalService.this);

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
        if (isMyServiceRunning(LocalService.class)){
            Intent intentService = new Intent(SmartRide.this, LocalService.class);
            SmartRide.this.stopService(intentService);
        }
        if (mBTService != null) {
            mBTService.stop();
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public static SettingsManager getSettingsManager() {
        return settings;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) SmartRide.this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}


//TODO Faire un tableau de données des capteurs avec valeurs et temps