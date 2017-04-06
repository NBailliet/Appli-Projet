package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolas.smartride2.R;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class LastRideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lastRideView = inflater.inflate(R.layout.lastride, container, false);
        return lastRideView;
    }
}
