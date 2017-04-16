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
import android.support.v4.media.session.MediaSessionCompat;
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

import static android.content.ContentValues.TAG;

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
    String PLAY_ACTION;
    String PAUSE_ACTION;
    String STOP_ACTION;
    Boolean testPause;


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
        testPause = false;

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

        /*long time = SystemClock.elapsedRealtime() - cArg.getBase();
        int h= (int)(time /3600000);
        int m= (int)(time - h*3600000)/60000;
        int s= (int)(time - h*3600000- m*60000)/1000 ;
        String hh = h < 10 ? "0"+h: h+"";
        String mm = m < 10 ? "0"+m: m+"";
        String ss = s < 10 ? "0"+s: s+"";

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.title, "SmartRide Run");
        contentView.setTextViewText(R.id.text, "Click to go back to SmartRide app");
        contentView.setChronometer(R.id.chronometerNotif,SystemClock.elapsedRealtime(),hh+":"+mm+":"+ss,true);


        Chronometer chronoManual = ManualModeFragment.getChronometer();
        Chronometer chronoMotion = MotionCaptureFragment.getChronometer();


        switch (contentView.getLayoutId()) {

            case R.id.playButton :
                if (testPause) {
                    if (settings.getStartManualRunPref()) {
                        chronoManual.start();
                    }
                    else if (settings.getStartMotionRunPref()) {
                        chronoMotion.start();
                    }
                    startTimer();
                }
                break;

            case R.id.



    }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logosmartridemini)
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);*/


        /*Intent intentPlay = new Intent(this, SmartRide.class);
        intentPlay.setAction(PLAY_ACTION);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 12345, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intentPause = new Intent(this, SmartRide.class);
        intentPlay.setAction(PAUSE_ACTION);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(this, 12345, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intentStop = new Intent(this, SmartRide.class);
        intentPlay.setAction(STOP_ACTION);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 12345, intentStop, PendingIntent.FLAG_UPDATE_CURRENT);*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logosmartridemini)
                        .setContentTitle("SmartRide Run")
                        .setContentText("Click to go back to SmartRide app")
                        .setColor(2)
                        .setUsesChronometer(true)
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

        //onReceive(context,intentPlay);

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


    /*public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        testPause = false;
        Chronometer chrono = ManualModeFragment.getChronometer();

        if(PLAY_ACTION.equals(action)) {
            if (testPause == true) {
                chrono.start();
                startTimer();
            }
        } else if(PAUSE_ACTION.equals(action)) {
            chrono.stop();
            stopTimer();
            testPause=true;
        } else if(STOP_ACTION.equals(action)) {
            chrono.stop();
            stopTimer();
            testPause=true;
        }
    }*/

}