package com.example.nicolas.smartride2.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nicolas on 07/04/2017.
 */

public class LocalService extends Service
{
    Timer timer;
    private Context ctx;
    private Date date;
    Chronometer chronometer;
    static long seconds;
    private static final int LOC_API_CALL_INTERVAL = 1000;

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        System.out.println("Service start :D");
        startService();
    }

    public void startService()
    {
        System.out.println("Service started");
        //timer.scheduleAtFixedRate(new mainTask(), 0, 1000);
        chronometer = new Chronometer(LocalService.this);
        Log.e("Coucou", "1");
        chronometer.setBase(SystemClock.elapsedRealtime());
        Log.e("Coucou", "2");
        chronometer.start();
        Log.e("Coucou", "avant");

        startTimer();
        Log.e("Coucou", "apres");
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
            Log.e("in", "timertask");
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        stopTimer();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    private final Handler toastHandler = new Handler()
    {
        public void handleMessage(Notification.MessagingStyle.Message msg)
        {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };

    //Timer related functions

    private void startTimer(){
        Log.e("starttimer", "debut");
        if(timer!=null ){
            Log.e("Coucou", "PROBLEME");
            return;
        }
        timer=new Timer();
        Log.e("starttimer", "1");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                long millis= SystemClock.elapsedRealtime() - chronometer.getBase();
                seconds = millis / 1000;
                Log.e("timefortest", "" + seconds);
                if (seconds>600) {
                    stopSelf();
                }
            }

        }, 0, LOC_API_CALL_INTERVAL);
        Log.e("starttimer", "2");
    }

    private void stopTimer(){

        if(null!=timer){
            timer.cancel();
            timer=null;
        }
    }

    public static int getSeconds(){


        return (int) seconds;
    }

}