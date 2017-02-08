package com.example.nicolas.smartride2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SmartRide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Application app;
    public static final String TAG = "debug";
    public int stateco = 0;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));

        View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SmartRide.app.getUser()!=null) {
                    Intent intent = new Intent(SmartRide.this, ProfilActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.profil_animation1, R.anim.profil_animation2);
                }
                else Toast.makeText(SmartRide.this, "Vous n'êtes pas connecté", Toast.LENGTH_SHORT).show();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Log.i(TAG, "getProfil: toast");
            }
        });

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogbegin, null);

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        adb.setTitle("Bienvenue sur l'application SmartRide");

        //On affecte un bouton "Connexion" à notre AlertDialog et on lui affecte un évènement

        adb.setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showConnectDialog();
            }
        });

        //On crée un bouton "Créer un compte" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Créer un compte", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showNewAccDialog();
            }
        });
        adb.show();


    }
    public void showNewAccDialog(){

        //On instancie notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogcreate, null);

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        adb.setTitle("Création d'un nouveau compte");

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse);

                String login = et1.getText().toString();
                String pswd = et2.getText().toString();

                //erreur, no user...
                SmartRide.app.getUser().setLogin(login);
                SmartRide.app.getUser().setPassword(pswd);

                dialog.cancel();
                showConnectDialog();

            }


        });

        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
            }
        });
        adb.show();
    }

    public void showConnectDialog(){
        //On instancie notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogconnect, null);

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        adb.setTitle("Connexion");

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                progress = ProgressDialog.show(SmartRide.this, "Connection",
                        "Please wait...", true);

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        // do the thing that takes a long time
                        //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                        EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant);
                        EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse);

                        String login=et1.getText().toString();
                        String pswd=et2.getText().toString();

                        // Log.i(TAG, ch);

                        app.wantsConnect(login, pswd);

                        if(SmartRide.app.getUser().getName()!=null) {
                            stateco= 1;
                        }
                        else{
                            SmartRide.app.setUser(null);
                            progress.dismiss();
                            stateco = 2;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {

                                Log.i(TAG, Integer.toString(stateco));
                                switch(stateco){
                                    case 1 :
                                        progress.dismiss();
                                        Toast.makeText(SmartRide.this, "Vous êtes connecté", Toast.LENGTH_SHORT).show();
                                        break;

                                    case 2 : Toast.makeText(SmartRide.this, "Identifiant/Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        });
                    }
                }).start();


                //Toast.makeText(MainActivity.this, "Vous êtes connecté", Toast.LENGTH_SHORT).show();


            }

        });

        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
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
            fm.beginTransaction().replace(R.id.frame,new SettingsFragment()).commit();
            setTitle(getString(R.string.action_settings));
            return true;
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
        }
        else if (id == R.id.action_quitter) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

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
}
