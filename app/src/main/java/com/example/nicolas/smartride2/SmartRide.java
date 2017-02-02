package com.example.nicolas.smartride2;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class SmartRide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //youpi
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
