package com.example.nicolas.smartride2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nicolas on 30/01/2017.
 */

public class RecordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.record, container, false);
        return recordView;
    }
}
