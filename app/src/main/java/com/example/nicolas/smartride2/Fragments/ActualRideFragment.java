package com.example.nicolas.smartride2.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.DataSensor;
import com.example.nicolas.smartride2.BDD.Run;
import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SessionManager;
import com.example.nicolas.smartride2.SettingsManager;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class ActualRideFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView textViewAX;
    TextView textViewAY;
    TextView textViewAZ;

    TextView textViewGX;
    TextView textViewGY;
    TextView textViewGZ;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private LineGraphSeries<DataPoint> mSeries3;
    private LineGraphSeries<DataPoint> mSeries4;
    private LineGraphSeries<DataPoint> mSeries5;
    private LineGraphSeries<DataPoint> mSeries6;
    private double graph2LastXValue = 5d;

    BDD bdd;
    static SessionManager session;
    SettingsManager setting;
    List<Long> dataAX= new ArrayList<>();
    List<Long> dataAY= new ArrayList<>();
    List<Long> dataAZ= new ArrayList<>();
    List<Long> dataATime= new ArrayList<>();
    List<Long> dataGX= new ArrayList<>();
    List<Long> dataGY= new ArrayList<>();
    List<Long> dataGZ= new ArrayList<>();
    List<Long> dataGTime= new ArrayList<>();
    String ski="left";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View actualRideView = inflater.inflate(R.layout.actualride, container, false);
        textViewAX = (TextView) actualRideView.findViewById(R.id.textGraphAX);
        textViewAY = (TextView) actualRideView.findViewById(R.id.textGraphAY);
        textViewAZ = (TextView) actualRideView.findViewById(R.id.textGraphAZ);
        textViewGX = (TextView) actualRideView.findViewById(R.id.textGraphGX);
        textViewGY = (TextView) actualRideView.findViewById(R.id.textGraphGY);
        textViewGZ = (TextView) actualRideView.findViewById(R.id.textGraphGZ);
        bdd = new BDD(getContext());
        session = new SessionManager(getActivity());
        setting = new SettingsManager(getActivity());


        Spinner spinner = (Spinner) actualRideView.findViewById(R.id.ride_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.actual_ride_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

         getDataFromBdd();
        //Log.d("ActualRideFragment","Acc ="+dataAX);
        GraphView graph = (GraphView) actualRideView.findViewById(R.id.graphAX);
        mSeries1 = new LineGraphSeries<>(generateDataAX());
        graph.addSeries(mSeries1);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        //graph.getViewport().setMaxXAxisSize(100);
        GraphView graph2 = (GraphView) actualRideView.findViewById(R.id.graphAY);
        mSeries2 = new LineGraphSeries<>(generateDataAY());
        graph2.addSeries(mSeries2);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getViewport().setScrollable(true);
        // graph2.getViewport().setMaxXAxisSize(100);
        GraphView graph3 = (GraphView) actualRideView.findViewById(R.id.graphAZ);
        mSeries3 = new LineGraphSeries<>(generateDataAZ());
        graph3.addSeries(mSeries3);
        graph3.getViewport().setScalable(true);
        graph3.getViewport().setScalableY(true);
        graph3.getViewport().setScrollable(true);
        //graph3.getViewport().setMaxXAxisSize(10);
        GraphView graph4 = (GraphView) actualRideView.findViewById(R.id.graphGX);
        mSeries4 = new LineGraphSeries<>(generateDataGX());
        graph4.addSeries(mSeries4);
        graph4.getViewport().setScalableY(true);
        graph4.getViewport().setScalable(true);
        graph4.getViewport().setScrollable(true);
        // graph4.getViewport().setMaxXAxisSize(10);
        GraphView graph5 = (GraphView) actualRideView.findViewById(R.id.graphGY);
        mSeries5 = new LineGraphSeries<>(generateDataGY());
        graph5.addSeries(mSeries5);
        graph5.getViewport().setScalable(true);
        graph5.getViewport().setScalableY(true);
        graph5.getViewport().setScrollable(true);
        //graph5.getViewport().setMaxXAxisSize(10);
        GraphView graph6 = (GraphView) actualRideView.findViewById(R.id.graphGZ);
        mSeries6 = new LineGraphSeries<>(generateDataGZ());
        graph6.addSeries(mSeries6);
        graph6.getViewport().setScalableY(true);
        graph6.getViewport().setScalable(true);
        graph6.getViewport().setScrollable(true);
        //graph6.getViewport().setMaxXAxisSize(10);


        return actualRideView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();

        switch (index) {
            case 0 :
                ski="left";
                //Toast.makeText(getActivity(), "SWITCH SKI GAUCHE SPINNER", Toast.LENGTH_SHORT).show();
                if (setting.getStartManualRunPref()||setting.getStartMotionRunPref()){
                    getDataFromBdd();
                    mSeries1.resetData(generateDataAX());
                    mSeries2.resetData(generateDataAY());
                    mSeries3.resetData(generateDataAZ());
                    mSeries4.resetData(generateDataGX());
                    mSeries5.resetData(generateDataGY());
                    mSeries6.resetData(generateDataGZ());
                    dataAX.clear();
                    dataAY.clear();
                    dataAZ.clear();
                    dataGX.clear();
                    dataGY.clear();
                    dataGZ.clear();
                }

                break;
            case 1 :
                //Toast.makeText(getActivity(), "SWITCH SKI DROIT SPINNER", Toast.LENGTH_SHORT).show();
                ski="right";
                if (setting.getStartManualRunPref()||setting.getStartMotionRunPref()){
                    getDataFromBdd();mSeries1.resetData(generateDataAX());
                    mSeries2.resetData(generateDataAY());
                    mSeries3.resetData(generateDataAZ());
                    mSeries4.resetData(generateDataGX());
                    mSeries5.resetData(generateDataGY());
                    mSeries6.resetData(generateDataGZ());
                    dataAX.clear();
                    dataAY.clear();
                    dataAZ.clear();
                    dataGX.clear();
                    dataGY.clear();
                    dataGZ.clear();

                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        /*mTimer1 = new Runnable() {
            @Override
            public void run() {
                //Log.d("onresume","refresh");
                getDataFromBdd();
                mSeries1.resetData(generateDataAX());
                mSeries2.resetData(generateDataAY());
                mSeries3.resetData(generateDataAZ());
                mSeries4.resetData(generateDataGX());
                mSeries5.resetData(generateDataGY());
                mSeries6.resetData(generateDataGZ());
                dataAX.clear();
                dataAY.clear();
                dataAZ.clear();
                dataGX.clear();
                dataGY.clear();
                dataGZ.clear();
                mHandler.postDelayed(this, 200);
            }
        };

        mHandler.postDelayed(mTimer1, 1000);*/
       /* mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                mSeries1.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 1000);*/
    }
    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause();
    }

    void getDataFromBdd(){
        List<DataSensor> dataSensorsA = new ArrayList<DataSensor>();
        List<DataSensor> dataSensorsG = new ArrayList<DataSensor>();
        bdd.open();
        if (ski.compareTo("left")==0) {
            List<Run> listRun =  bdd.getAllRunWithProfil(session.getLoginPref());
            int nbRunListP = listRun.size();
            dataSensorsA = bdd.getAllDataAccWithRunAndProfil(listRun.get(nbRunListP-1).getName(), session.getLoginPref());
            dataSensorsG = bdd.getAllDataGyroWithRunAndProfil(listRun.get(nbRunListP-1).getName(), session.getLoginPref());
        }
        if (ski.compareTo("right")==0) {
            List<Run> listRun =  bdd.getAllRunWithProfil(session.getLoginPref());
            int nbRunListP = listRun.size();
            dataSensorsA = bdd.getAllDataAccWithRunAndProfil(listRun.get(nbRunListP-1).getName(), session.getLoginPref());
            dataSensorsG = bdd.getAllDataGyroWithRunAndProfil(listRun.get(nbRunListP-1).getName(), session.getLoginPref());
        }
        bdd.close();

        if (dataSensorsA.size()!=0){
            for(int i=0;i<dataSensorsA.size();i++){
                dataAX.add(Long.parseLong(dataSensorsA.get(i).getDataX().replace('\0','A').replace("A","")));
                dataAY.add(Long.parseLong(dataSensorsA.get(i).getDataY().replace('\0','A').replace("A","")));
                dataAZ.add(Long.parseLong(dataSensorsA.get(i).getDataZ().replace('\0','A').replace("A","")));
            }
        }
        if (dataSensorsG.size()!=0){
            for(int i=0;i<dataSensorsG.size();i++){
                dataGX.add(Long.parseLong(dataSensorsG.get(i).getDataX().replace('\0','A').replace("A","")));
                dataGY.add(Long.parseLong(dataSensorsG.get(i).getDataY().replace('\0','A').replace("A","")));
                dataGZ.add(Long.parseLong(dataSensorsG.get(i).getDataZ().replace('\0','A').replace("A","")));
            }
        }
        Log.d("bdd","Acc ="+dataAX);
    }
    private DataPoint[] generateDataAX() {
        int count = dataAX.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataAX.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    private DataPoint[] generateDataAY() {
        int count = dataAY.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataAY.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] generateDataAZ() {
        int count = dataAZ.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataAZ.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    private DataPoint[] generateDataGX() {
        int count = dataGX.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataGX.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    private DataPoint[] generateDataGY() {
        int count = dataGY.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataGY.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] generateDataGZ() {
        int count = dataGZ.size();
        //Log.d("generate data",Integer.toString(count));
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = dataGZ.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


}
