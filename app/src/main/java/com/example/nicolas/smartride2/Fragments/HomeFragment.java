package com.example.nicolas.smartride2.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.Services.LocalService;
import com.example.nicolas.smartride2.SettingsManager;
import com.example.nicolas.smartride2.SmartRide;

import org.w3c.dom.Text;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

        TextView textrun;
        TextView textnorun;
        Button buttonRun;
        SettingsManager settings;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View homeView = inflater.inflate(R.layout.home, container, false);
            textrun = (TextView) homeView.findViewById(R.id.textViewRun);
            textnorun = (TextView) homeView.findViewById(R.id.textViewNoRun);
            buttonRun = (Button) homeView.findViewById(R.id.buttonRun);
            buttonRun.setOnClickListener(this);
            settings = SmartRide.getSettingsManager();
            if ((settings.getStartMotionRunPref()==true || settings.getStartManualRunPref()==true) || isMyServiceRunning(LocalService.class) == true) {
                textrun.setVisibility(View.VISIBLE);
                textnorun.setVisibility(View.INVISIBLE);
                buttonRun.setVisibility(View.VISIBLE);
            }
            return homeView;
        }

    @Override
    public void onClick(View v) {

        View parent = (View) v.getParent();

        Button buttonRun = (Button) parent.findViewById(R.id.buttonRun);

        if (v.hasOnClickListeners()) {


            switch (v.getId()) {

                case (R.id.buttonRun):

                    if (settings.getStartMotionRunPref()) {
                        MotionCaptureFragment motionFrag = new MotionCaptureFragment();
                        this.getFragmentManager().beginTransaction()
                                .replace(R.id.frame, motionFrag, "Motion Fragment OK")
                                .addToBackStack(null)
                                .commit();
                    } else if (settings.getStartManualRunPref()) {
                        ManualModeFragment manualFrag = new ManualModeFragment();
                        this.getFragmentManager().beginTransaction()
                                .replace(R.id.frame, manualFrag, "Manual Fragment OK")
                                .addToBackStack(null)
                                .commit();
                    } else if (settings.getStartMotionRunPref() && settings.getStartManualRunPref()) {
                        Toast.makeText(getActivity(), "Error : 2 runs at the same time !", Toast.LENGTH_SHORT).show();
                    }

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

