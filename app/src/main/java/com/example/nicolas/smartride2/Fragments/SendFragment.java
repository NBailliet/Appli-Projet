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

public class SendFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View sendView = inflater.inflate(R.layout.send, container, false);
        return sendView;
    }
}
