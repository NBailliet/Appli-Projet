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

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.Services.LocalService;
import com.example.nicolas.smartride2.Services.RideLocationGetter;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

/**
 * Created by Nicolas on 30/01/2017.
 */

public class ManualModeFragment extends Fragment implements View.OnClickListener {

    Button buttonStartRun;
    Button buttonStopRun;
    Chronometer chronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int min = 0;
        int hour = 0;
        int sec = 0;
        View manualView = inflater.inflate(R.layout.manualmode, container, false);
        buttonStartRun = (Button) manualView.findViewById(R.id.buttonStartRun);
        buttonStartRun.setOnClickListener(this);
        buttonStopRun = (Button) manualView.findViewById(R.id.buttonStopRun);
        buttonStopRun.setOnClickListener(this);
        chronometer = (Chronometer) manualView.findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h= (int)(time /3600000);
                int m= (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);

            }
        });
        if (isMyServiceRunning(LocalService.class)==true) {
            buttonStartRun.setVisibility(View.INVISIBLE);
            buttonStopRun.setVisibility(View.VISIBLE);
            System.out.println(LocalService.getSeconds());
            sec=LocalService.getSeconds();
            if (sec>60) {
                min = (sec%3600)/60;
                if (min>60) {
                    hour = sec / 3600;
                }
                sec = sec - (hour*3600 + min*60);
            }
            System.out.println(sec);
            System.out.println(min);
            System.out.println(hour);
            chronometer.setBase(SystemClock.elapsedRealtime() - (hour * 60000 + min * 60000 + sec * 1000));
            //chronometer.setText(hh+":"+mm+":"+ss);
            chronometer.start();
        }

        return manualView;
    }

    @Override
    public void onClick(View v) {

        View parent = (View)v.getParent();

        if (v.hasOnClickListeners()) {

            Button buttonStopRun = (Button) parent.findViewById(R.id.buttonStopRun);
            Button buttonStartRun = (Button) parent.findViewById(R.id.buttonStartRun);
            Chronometer chronometerRun = (Chronometer) parent.findViewById(R.id.chronometer);
            System.out.println("Test" + isMyServiceRunning(LocalService.class));

                switch (v.getId()) {

                    case (R.id.buttonStartRun):
                        System.out.println(isMyServiceRunning(LocalService.class));
                        if (isMyServiceRunning(LocalService.class)==false) {

                            System.out.println("Bouton Start OK");
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            chronometerRun.setBase(SystemClock.elapsedRealtime());
                            //chronometerRun.setFormat("H:MM:SS");
                            chronometerRun.start();
                            Intent intentChrono = new Intent(getActivity(), LocalService.class);
                            getActivity().startService(intentChrono);
                            buttonStartRun.setVisibility(View.INVISIBLE);
                            buttonStopRun.setVisibility(View.VISIBLE);
                        }
                        else {
                            buttonStartRun.setVisibility(View.INVISIBLE);
                            buttonStopRun.setVisibility(View.VISIBLE);
                        }

                        break;

                    case (R.id.buttonStopRun):

                        System.out.println("Bouton Stop OK");
                        SettingsManager settings = SmartRide.getSettingsManager();
                        settings.setRunPref(true);
                        Intent intentChrono2 = new Intent(getActivity(), LocalService.class);
                        getActivity().stopService(intentChrono2);
                        chronometerRun.stop();
                        chronometerRun.setBase(SystemClock.elapsedRealtime());
                        buttonStopRun.setVisibility(View.INVISIBLE);
                        buttonStartRun.setVisibility(View.VISIBLE);
                        break;

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
