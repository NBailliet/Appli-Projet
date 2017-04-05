package com.example.nicolas.smartride2.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SmartRide;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    RadioButton radioButton1;
    RadioButton radioButton2;
    CheckBox checkBox1;
    CheckBox checkBox2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.settings, container, false);
        radioButton1 = (RadioButton) settingsView.findViewById(R.id.radioButton1);
        radioButton1.setOnClickListener(this);
        radioButton2 = (RadioButton) settingsView.findViewById(R.id.radioButton2);
        radioButton2.setOnClickListener(this);
        checkBox1 = (CheckBox) settingsView.findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(this);
        checkBox2 = (CheckBox) settingsView.findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(this);
        return settingsView;
    }

    @Override
    public void onClick(View v) {

        //View parent = (View)v.getParent();
        Activity activity = getActivity();
        if (v.hasOnClickListeners()) {


            switch (v.getId()) {


                case R.id.radioButton1 :

                    if (radioButton1.isChecked()) {
                        Toast.makeText(activity, "Voice command ON", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.radioButton2 :

                    if (radioButton2.isChecked()) {
                        Toast.makeText(activity, "Voice command OFF", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox1 :

                    if (checkBox1.isChecked()) {
                        Toast.makeText(activity, "Test1 Checked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox2 :

                    if (checkBox2.isChecked()) {
                        Toast.makeText(activity, "Test2 Checked", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }
    }
}
