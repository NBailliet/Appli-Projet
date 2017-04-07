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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SmartRide;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    //RadioButton radioButton1;
    //RadioButton radioButton2;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;
    CheckBox checkBox9;
    CheckBox checkBox10;
    Switch switchVoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.settings, container, false);
//        radioButton1 = (RadioButton) settingsView.findViewById(R.id.radioButton1);
//        radioButton1.setOnClickListener(this);
//        radioButton2 = (RadioButton) settingsView.findViewById(R.id.radioButton2);
//        radioButton2.setOnClickListener(this);
        checkBox1 = (CheckBox) settingsView.findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(this);
        checkBox2 = (CheckBox) settingsView.findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(this);
        checkBox3 = (CheckBox) settingsView.findViewById(R.id.checkBox3);
        checkBox3.setOnClickListener(this);
        checkBox4 = (CheckBox) settingsView.findViewById(R.id.checkBox4);
        checkBox4.setOnClickListener(this);
        checkBox5 = (CheckBox) settingsView.findViewById(R.id.checkBox5);
        checkBox5.setOnClickListener(this);
        checkBox6 = (CheckBox) settingsView.findViewById(R.id.checkBox6);
        checkBox6.setOnClickListener(this);
        checkBox7 = (CheckBox) settingsView.findViewById(R.id.checkBox7);
        checkBox7.setOnClickListener(this);
        checkBox8 = (CheckBox) settingsView.findViewById(R.id.checkBox8);
        checkBox8.setOnClickListener(this);
        checkBox9 = (CheckBox) settingsView.findViewById(R.id.checkBox9);
        checkBox9.setOnClickListener(this);
        checkBox10 = (CheckBox) settingsView.findViewById(R.id.checkBox10);
        checkBox10.setOnClickListener(this);
        switchVoice = (Switch) settingsView.findViewById(R.id.switchVoice);
        switchVoice.setOnCheckedChangeListener(this);
        return settingsView;
    }

    @Override
    public void onClick(View v) {

        //View parent = (View)v.getParent();
        Activity activity = getActivity();
        if (v.hasOnClickListeners()) {


            switch (v.getId()) {

                case R.id.checkBox1 :

                    if (checkBox1.isChecked()) {
                        Toast.makeText(activity, "Test1 Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Test1 UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox2 :

                    if (checkBox2.isChecked()) {
                        Toast.makeText(activity, "Test2 Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Test2 UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox3 :

                    if (checkBox3.isChecked()) {
                        Toast.makeText(activity, "Accélérommètres G Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Accélérommètres G UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox4 :

                    if (checkBox4.isChecked()) {
                        Toast.makeText(activity, "Accélérommètres D Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Accélérommètres D UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox5 :

                    if (checkBox5.isChecked()) {
                        Toast.makeText(activity, "Gyroscopes G Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Gyroscopes G UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox6 :

                    if (checkBox6.isChecked()) {
                        Toast.makeText(activity, "Gyroscopes D Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Gyroscopes D UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox7 :

                    if (checkBox7.isChecked()) {
                        Toast.makeText(activity, "Capteurs de pression G Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Capteurs de pression G UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox8 :

                    if (checkBox8.isChecked()) {
                        Toast.makeText(activity, "Capteurs de pression D Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Capteurs de pression D UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox9 :

                    if (checkBox9.isChecked()) {
                        Toast.makeText(activity, "Jauges de contrainte G Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Jauges de contrainte G UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkBox10 :

                    if (checkBox10.isChecked()) {
                        Toast.makeText(activity, "Jauges de contrainte D Checked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(activity, "Jauges de contrainte D UnChecked", Toast.LENGTH_SHORT).show();
                    }
                    break;


            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Activity activity = getActivity();
        switch (buttonView.getId()) {

            case R.id.switchVoice :

                if (switchVoice.isChecked()) {
                    Toast.makeText(activity, "Voice Command ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "Voice Command OFF", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
