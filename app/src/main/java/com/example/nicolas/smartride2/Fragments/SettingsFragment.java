package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;

import com.example.nicolas.smartride2.R;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    RadioButton radioButton1;
    RadioButton radioButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.settings, container, false);
        radioButton1 = (RadioButton) settingsView.findViewById(R.id.radioButton1);
        radioButton1.setOnClickListener(this);
        radioButton2 = (RadioButton) settingsView.findViewById(R.id.radioButton2);
        radioButton2.setOnClickListener(this);
        return settingsView;
    }

    @Override
    public void onClick(View v) {

        //View parent = (View)v.getParent();

        if (v.hasOnClickListeners()) {


            switch (v.getId()) {


                case R.id.radioButton1 :

                    if (radioButton1.isChecked()) {
                        System.out.println("Bouton checked ON");
                    }
                    break;

                case R.id.radioButton2 :

                    if (radioButton2.isChecked()) {
                        System.out.println("Bouton checked OFF");
                    }
                    break;

            }

        }
    }
}
