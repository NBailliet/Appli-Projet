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

import com.example.nicolas.smartride2.Fragments.HomeFragment;
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
    static long lastSeconds;

    SettingsManager settings;
    private static final int LOC_API_CALL_INTERVAL = 1000;
    NotificationManager mNotificationManager;


    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        Context context = this;
        super.onCreate();
        ctx = this;
        settings=SmartRide.getSettingsManager();
        System.out.println("Service created");
        startService();
        int mId = 0;

            /*if (settings.getManualRunPref()) {
                resultIntent = new Intent(LocalService.this, ManualModeFragment.class);
            System.out.println("Go back to Manual Mode Notification");
            }
            else if (settings.getMotionRunPref()) {
            resultIntent = new Intent(LocalService.this, MotionCaptureFragment.class);
            System.out.println("Go back to Motion Capture Notification");
            }*/
        Intent intent = new Intent(this, SmartRide.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logosmartridemini)
                        .setContentTitle("SmartRide Run")
                        .setContentText("Click to go back to SmartRide app")
                        .setColor(2)
                        .setUsesChronometer(true)
                        .setProgress(0,0,true)
                        .setContentIntent(pIntent);


        // Creates an explicit intent for an Activity in your app


            /*Intent resultIntent = new Intent(this, SmartRide.class);

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
            mBuilder.setContentIntent(resultPendingIntent);*/
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

    }

    public void startService()
    {
        System.out.println("Service started");
        chronometer = new Chronometer(LocalService.this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startTimer();
    }

    /*public void pauseService()
    {
        System.out.println("Service paused");
        pauseTimer();
    }

    public void resumeService()
    {
        System.out.println("Service resumed");
        resumeTimer();
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
            Log.e("in", "timertask");
        }
    }*/

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
        if(timer!=null ){
            return;
        }
        final long[] newseconds = new long[1];
        newseconds[0]=0;
        timer=new Timer();
        final long[] counter = new long[1];
        Log.e("START TIMER", "1");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (settings.getStopPausePref()) {
                    newseconds[0] = lastSeconds;
                    System.out.println("NEW SECONDS VALUE = "+String.valueOf(newseconds[0]));
                    counter[0] = counter[0] + 1;
                    settings.setStopPausePref(false);
                }
                if (settings.getStartPausePref()) {
                    lastSeconds = LocalService.getSeconds();
                    System.out.println("LAST SECONDS VALUE = "+String.valueOf(lastSeconds));
                    stopTimer();
                    settings.setStartPausePref(false);
                }
                long millis= SystemClock.elapsedRealtime() - chronometer.getBase() + newseconds[0]*999;
                seconds = millis / 1000;
                Log.e("TIME SERVICE :", "" + seconds);
                if (seconds>600) {
                    stopSelf();
                }
            }

        }, 0, LOC_API_CALL_INTERVAL);
        Log.e("START TIMER", "2");
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