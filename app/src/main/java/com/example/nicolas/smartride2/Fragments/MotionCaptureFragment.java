package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

/**
 * Created by Nicolas on 30/01/2017.
 */

public class MotionCaptureFragment extends Fragment implements View.OnClickListener {

    Button buttonStopRun;
    Chronometer chronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View motionView = inflater.inflate(R.layout.motioncapture, container, false);
        buttonStopRun = (Button) motionView.findViewById(R.id.buttonStopRun);
        buttonStopRun.setOnClickListener(this);
        chronometer = (Chronometer) motionView.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chronometer.start();
        setHasOptionsMenu(true);
        return motionView;
    }

    @Override
    public void onClick(View v) {

        View parent = (View)v.getParent();

        if (v.hasOnClickListeners()) {

            Button buttonStopRun = (Button) parent.findViewById(R.id.buttonStopRun);
            Chronometer chronometerRun = (Chronometer) parent.findViewById(R.id.chronometer);
            chronometerRun.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
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

            switch (v.getId()) {


                case (R.id.buttonStopRun):

                    System.out.println("Bouton Stop OK");
                    SettingsManager settings = SmartRide.getSettingsManager();
                    settings.setRunPref(true);
                    chronometerRun.stop();
                    chronometerRun.setBase(SystemClock.elapsedRealtime());
                    break;

            }

        }
    }


}
