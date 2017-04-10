package com.example.nicolas.smartride2.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.nicolas.smartride2.Fragments.ManualModeFragment;
import com.example.nicolas.smartride2.Fragments.MotionCaptureFragment;
import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

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
    SettingsManager settings;
    private static final int LOC_API_CALL_INTERVAL = 1000;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    Intent resultIntent = new Intent();


    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        Context context = this;
        super.onCreate();
        ctx = this;
        System.out.println("Service start :D");
        startService();
        int mId = 0;
        int layout = R.id.home_layout;
        settings = SmartRide.getSettingsManager();

        if (settings.getManualRunPref()) {
            resultIntent = new Intent(LocalService.this, ManualModeFragment.class);
            layout = R.layout.manualmode;
            System.out.println("Go back to Manual Mode Notification");
        }
        else if (settings.getMotionRunPref()) {
            resultIntent = new Intent(LocalService.this, MotionCaptureFragment.class);
            layout = R.layout.motioncapture;
            System.out.println("Go back to Motion Capture Notification");
        }

        PendingIntent pIntent = PendingIntent.getActivity(LocalService.this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logosmartridemini)
                        .setContentTitle("SmartRide Run")
                        .setContentText("Click to go back to SmartRide app")
                        .setAutoCancel(true)
                        .setColor(2)
                        .setContentIntent(pIntent)
                        .setUsesChronometer(true)
                        .setProgress(10,1,true);

        Notification notification = mBuilder.build();


        // Creates an explicit intent for an Activity in your app


        //TODO Add chronometer and go back to runs

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SmartRide.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, notification);
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
        mNotificationManager.cancel(0);
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