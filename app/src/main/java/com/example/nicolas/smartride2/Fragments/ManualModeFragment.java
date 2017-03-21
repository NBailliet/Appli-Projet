package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.nicolas.smartride2.R;

/**
 * Created by Nicolas on 30/01/2017.
 */

public class RecordFragment extends Fragment implements View.OnClickListener {

    Button buttonStartRun;
    Button buttonStopRun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.record, container, false);
        buttonStartRun = (Button) recordView.findViewById(R.id.buttonStartRun);
        buttonStartRun.setOnClickListener(this);
        buttonStopRun = (Button) recordView.findViewById(R.id.buttonStopRun);
        buttonStopRun.setOnClickListener(this);
        return recordView;
    }

    @Override
    public void onClick(View v) {

        View parent = (View)v.getParent();

        if (v.hasOnClickListeners()) {

            Button buttonStopRun = (Button) parent.findViewById(R.id.buttonStopRun);
            Button buttonStartRun = (Button) parent.findViewById(R.id.buttonStartRun);
            Chronometer chronometerRun = (Chronometer) parent.findViewById(R.id.chronometer);

            switch (v.getId()) {

                case (R.id.buttonStartRun):

                    System.out.println("Bouton Start OK");
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chronometerRun.setBase(SystemClock.elapsedRealtime());
                    chronometerRun.start();
                    buttonStartRun.setVisibility(View.INVISIBLE);
                    buttonStopRun.setVisibility(View.VISIBLE);
                    break;

                case (R.id.buttonStopRun):

                    System.out.println("Bouton Stop OK");
                    chronometerRun.stop();
                    chronometerRun.setBase(SystemClock.elapsedRealtime());
                    buttonStopRun.setVisibility(View.INVISIBLE);
                    buttonStartRun.setVisibility(View.VISIBLE);
                    break;

            }

        }
    }

    /*@Override
    public void onChronometerTick(Chronometer chronometer) {
        String s = String.valueOf(chronometer.getBase());
        chronometer.setText(s);
    }*/


}
