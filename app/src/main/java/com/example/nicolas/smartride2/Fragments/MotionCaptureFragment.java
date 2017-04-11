package com.example.nicolas.smartride2.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.Services.LocalService;
import com.example.nicolas.smartride2.Services.RideLocationGetter;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

/**
 * Created by Nicolas on 30/01/2017.
 */

public class MotionCaptureFragment extends Fragment implements View.OnClickListener {

    Button buttonStopRun;
    Button buttonStartPauseRun;
    Chronometer chronometer;
    SettingsManager settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int min = 0;
        int hour = 0;
        int sec = 0;
        settings = SmartRide.getSettingsManager();
        View motionView = inflater.inflate(R.layout.motioncapture, container, false);
        buttonStopRun = (Button) motionView.findViewById(R.id.buttonStopRun);
        buttonStopRun.setOnClickListener(this);
        buttonStartPauseRun = (Button) motionView.findViewById(R.id.buttonStartPauseRun);
        buttonStartPauseRun.setOnClickListener(this);
        chronometer = (Chronometer) motionView.findViewById(R.id.chronometer2);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(hh + ":" + mm + ":" + ss);
            }
        });

        if (!settings.getStartManualRunPref()) {

            if (isMyServiceRunning(LocalService.class)) {
                sec = LocalService.getSeconds();
                if (sec > 60) {
                    min = (sec % 3600) / 60;
                    if (min > 60) {
                        hour = sec / 3600;
                    }
                    sec = sec - (hour * 3600 + min * 60);
                }
                System.out.println(sec);
                System.out.println(min);
                System.out.println(hour);
                chronometer.setBase(SystemClock.elapsedRealtime() - (hour * 60000 + min * 60000 + sec * 1000));
                //chronometer.setText(hh+":"+mm+":"+ss);
                chronometer.start();
            } else {

                chronometer.start();
                Intent intentChrono = new Intent(getActivity(), LocalService.class);
                getActivity().startService(intentChrono);
                Intent intentGPS = new Intent(getActivity(), RideLocationGetter.class);
                getActivity().startService(intentGPS);
                System.out.println("GPS lancé :)");
                SettingsManager settings = SmartRide.getSettingsManager();
                settings.setStartMotionRunPref(true);
                setHasOptionsMenu(true);
            }
        }
        else {
            Toast.makeText(getActivity(), "Error : 2 runs at the same time, please stop the other run !", Toast.LENGTH_SHORT).show();
        }

        return motionView;
    }

    @Override
    public void onClick(View v) {

        View parent = (View)v.getParent();
        long timeWhenStopped = 0;

        if (v.hasOnClickListeners()) {

            Button buttonStopRun = (Button) parent.findViewById(R.id.buttonStopRun);
            Button buttonStartPauseRun = (Button) parent.findViewById(R.id.buttonStartPauseRun);

            Chronometer chronometerRun = (Chronometer) parent.findViewById(R.id.chronometer2);

            switch (v.getId()) {


                case (R.id.buttonStopRun):

                    System.out.println("Bouton Stop OK");
                    settings = SmartRide.getSettingsManager();
                    Intent intentChrono2 = new Intent(getActivity(), LocalService.class);
                    getActivity().stopService(intentChrono2);
                    chronometerRun.stop();
                    settings.setStartMotionRunPref(false);
                    settings.setFinishedMotionRunPref(true);
                    System.out.println("FINISHED RUN CHANGED TO : "+String.valueOf(settings.getFinishedMotionRunPref()));
                    settings.addRunNbPref();
                    System.out.println("NB OF RUNS = "+String.valueOf(settings.getRunNbPref()));
                    chronometerRun.setBase(SystemClock.elapsedRealtime());
                    Intent intentGPS2 = new Intent(getActivity(), RideLocationGetter.class);
                    getActivity().stopService(intentGPS2);
                    System.out.println("GPS stoppé :)");
                    break;

                case (R.id.buttonStartPauseRun):
                    System.out.println("Bouton Pause OK");
                    timeWhenStopped = chronometerRun.getBase() - SystemClock.elapsedRealtime();
                    chronometerRun.stop();
                    chronometerRun.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometerRun.start();

            }

        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
