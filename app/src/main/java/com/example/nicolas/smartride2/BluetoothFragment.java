package com.example.nicolas.smartride2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class BluetoothFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bluetoothView = inflater.inflate(R.layout.bluetooth, container, false);
        return bluetoothView;
    }
}