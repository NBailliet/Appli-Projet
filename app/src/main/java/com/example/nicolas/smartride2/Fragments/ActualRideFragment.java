package com.example.nicolas.smartride2.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.smartride2.CSVFile;
import com.example.nicolas.smartride2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    InputStream inputStream;
    CSVFile csvFile;
    List fileList;
    List resultList;

    List<String> timeListTemp = new ArrayList();
    List<Float> timeList = new ArrayList();
    String[][] newTime = new String[1000][4];
    List<Integer> dataLFList = new ArrayList();
    List<Integer> AXList = new ArrayList();
    List<Integer> AYList = new ArrayList();
    List<Integer> AZList = new ArrayList();
    List<Integer> GXList = new ArrayList();
    List<Integer> GYList = new ArrayList();
    List<Integer> GZList = new ArrayList();

    boolean isSkiD=false;

    int counter=0;
    int check=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View actualRideView = inflater.inflate(R.layout.actualride, container, false);
        textViewAX = (TextView) actualRideView.findViewById(R.id.textGraphAX);
        textViewAY = (TextView) actualRideView.findViewById(R.id.textGraphAY);
        textViewAZ = (TextView) actualRideView.findViewById(R.id.textGraphAZ);
        textViewGX = (TextView) actualRideView.findViewById(R.id.textGraphGX);
        textViewGY = (TextView) actualRideView.findViewById(R.id.textGraphGY);
        textViewGZ = (TextView) actualRideView.findViewById(R.id.textGraphGZ);
        System.out.println("CHECK TEST ONCREATE = " + check);
        Spinner spinner = (Spinner) actualRideView.findViewById(R.id.ride_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.actual_ride_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        System.out.println(isSkiD);
        if (isSkiD) {
            inputStream = getResources().openRawResource(R.raw.run5skid);
        }
        else {
            inputStream = getResources().openRawResource(R.raw.run5skig);
        }
        if (counter>0) {
            newTime = new String[1000][4];
            AXList.clear();
            AYList.clear();
            AZList.clear();
            GXList.clear();
            GYList.clear();
            GZList.clear();
            dataLFList.clear();
            timeList.clear();
            timeListTemp.clear();
        }
        csvFile = new CSVFile(inputStream);
        fileList = csvFile.read();
        fileList.remove(0);
        newTime = new String[fileList.size()][4];
        //System.out.println(fileList);
        //System.out.println(fileList.get(1));
        //System.out.println(fileList.get(2));
        resultList=fromListToRow(fileList);
        timeListTemp = (List) resultList.get(0);
        //System.out.println("TIMELISTTEMP : " + timeListTemp);
        dataLFList = (List) resultList.get(1);
        //System.out.println("DATALFLIST : " + dataLFList);
        AXList = (List) resultList.get(2);
        //System.out.println("AXLIST : " + AXList);
        AYList = (List) resultList.get(3);
        //System.out.println("AYLIST : " + AYList);
        AZList = (List) resultList.get(4);
        //System.out.println("AZLIST : " + AZList);
        GXList = (List) resultList.get(5);
        //System.out.println("GXLIST : " + GXList);
        GYList = (List) resultList.get(6);
        //System.out.println("GYLIST : " + GYList);
        GZList = (List) resultList.get(7);
        //System.out.println("GZLIST : " + GZList);

        timeList = timeStringToFloat(timeListTemp,newTime);
        //System.out.println("TIMELIST : " + timeList);


        LineGraphSeries<DataPoint> series = null;
        GraphView graph = (GraphView) actualRideView.findViewById(R.id.graphDataLogger);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(dataLFList.get(i));

                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), dataLFList.get(i))
                });

            }
            else {

                //System.out.println(timeList.get(i));
                //System.out.println(dataLFList.get(i));

                series.appendData(new DataPoint(timeList.get(i),dataLFList.get(i)),false,timeList.size());
            }
        }
        graph.addSeries(series);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setMinX(timeList.get(0));
        graph.getViewport().setMaxX(timeList.get(0)+1);
        graph.getViewport().setMinY(Collections.min(dataLFList));
        graph.getViewport().setMaxY(Collections.max(dataLFList));
        series.setBackgroundColor(Color.RED);
        series.setAnimated(true);
        series.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series2 = null;
        GraphView graph2 = (GraphView) actualRideView.findViewById(R.id.graphAX);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(AXList.get(i));

                series2 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), AXList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(AXList.get(i));

                series2.appendData(new DataPoint(timeList.get(i),AXList.get(i)),false,timeList.size());
            }
        }
        graph2.addSeries(series2);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getViewport().setMinX(timeList.get(0));
        graph2.getViewport().setMaxX(timeList.get(0)+1);
        series2.setBackgroundColor(Color.RED);
        series2.setAnimated(true);
        series2.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series3 = null;
        GraphView graph3 = (GraphView) actualRideView.findViewById(R.id.graphAY);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(AYList.get(i));

                series3 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), AYList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(AYList.get(i));

                series3.appendData(new DataPoint(timeList.get(i),AYList.get(i)),false,timeList.size());
            }
        }
        graph3.addSeries(series3);
        graph3.getViewport().setScalable(true);
        graph3.getViewport().setScalableY(true);
        graph3.getViewport().setMinX(timeList.get(0));
        graph3.getViewport().setMaxX(timeList.get(0)+1);
        series3.setBackgroundColor(Color.RED);
        series3.setAnimated(true);
        series3.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series4 = null;
        GraphView graph4 = (GraphView) actualRideView.findViewById(R.id.graphAZ);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(AZList.get(i));

                series4 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), AZList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(AZList.get(i));

                series4.appendData(new DataPoint(timeList.get(i),AZList.get(i)),false,timeList.size());
            }
        }
        graph4.addSeries(series4);
        graph4.getViewport().setScalable(true);
        graph4.getViewport().setScalableY(true);
        graph4.getViewport().setMinX(timeList.get(0));
        graph4.getViewport().setMaxX(timeList.get(0)+1);
        series4.setBackgroundColor(Color.RED);
        series4.setAnimated(true);
        series4.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series5 = null;
        GraphView graph5 = (GraphView) actualRideView.findViewById(R.id.graphGX);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(GXList.get(i));

                series5 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), GXList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(GXList.get(i));

                series5.appendData(new DataPoint(timeList.get(i),GXList.get(i)),false,timeList.size());
            }
        }
        graph5.addSeries(series5);
        graph5.getViewport().setScalable(true);
        graph5.getViewport().setScalableY(true);
        graph5.getViewport().setMinX(timeList.get(0));
        graph5.getViewport().setMaxX(timeList.get(0)+1);
        series5.setBackgroundColor(Color.RED);
        series5.setAnimated(true);
        series5.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series6 = null;
        GraphView graph6 = (GraphView) actualRideView.findViewById(R.id.graphGY);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(GYList.get(i));

                series6 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), GYList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(GYList.get(i));

                series6.appendData(new DataPoint(timeList.get(i),GYList.get(i)),false,timeList.size());
            }
        }
        graph6.addSeries(series6);
        graph6.getViewport().setScalable(true);
        graph6.getViewport().setScalableY(true);
        graph6.getViewport().setMinX(timeList.get(0));
        graph6.getViewport().setMaxX(timeList.get(0)+1);
        series6.setBackgroundColor(Color.RED);
        series6.setAnimated(true);
        series6.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series7 = null;
        GraphView graph7 = (GraphView) actualRideView.findViewById(R.id.graphGZ);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                //System.out.println(timeList.get(i));
                //System.out.println(GZList.get(i));

                series7 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), GZList.get(i))
                });

            }
            else {
                //System.out.println(timeList.get(i));
                //System.out.println(GZList.get(i));

                series7.appendData(new DataPoint(timeList.get(i),GZList.get(i)),false,timeList.size());
            }
        }
        graph7.addSeries(series7);
        graph7.getViewport().setScalable(true);
        graph7.getViewport().setScalableY(true);
        graph7.getViewport().setMinX(timeList.get(0));
        graph7.getViewport().setMaxX(timeList.get(0)+1);
        series7.setBackgroundColor(Color.RED);
        series7.setAnimated(true);
        series7.setDrawDataPoints(true);

        return actualRideView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();

        //System.out.println("CHECK TEST = " + check);

        switch (index) {
            case 0 :
                if (isSkiD) {
                    check=0;
                }
                if (check == 0) {
                    Toast.makeText(getActivity(), "SWITCH SKI GAUCHE SPINNER", Toast.LENGTH_SHORT).show();

                    if (isSkiD) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(this).attach(this).commit();
                        counter++;
                        check=check+1;
                        //System.out.println("CHECK TEST = " + check);
                        isSkiD=false;
                    }
                }


                break;

            case 1:
                if (!isSkiD) {
                    check=0;
                }
                if (check == 0) {
                    Toast.makeText(getActivity(), "SWITCH SKI DROIT SPINNER", Toast.LENGTH_SHORT).show();

                    isSkiD = true;
                    counter++;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                    check=check+1;
                    //System.out.println("CHECK TEST" + check);
                }

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public List fromListToRow(List fileList) {

        List newList = new ArrayList();
        String[] listt = new String[fileList.size()];
        String[] listp = new String[fileList.size()];

        List<String> time = new ArrayList();
        List<Integer> dataLF = new ArrayList();
        List<Integer> AX = new ArrayList();
        List<Integer> AY = new ArrayList();
        List<Integer> AZ = new ArrayList();
        List<Integer> GX = new ArrayList();
        List<Integer> GY = new ArrayList();
        List<Integer> GZ = new ArrayList();

        for (int i=1;i<fileList.size();i++) {
            listt[i] = (String) fileList.get(i);
            listp = listt[i].split(", ");

            time.add(listp[0]);
            dataLF.add(Integer.parseInt(listp[1]));
            AX.add(Integer.parseInt(listp[2]));
            AY.add(Integer.parseInt(listp[3]));
            AZ.add(Integer.parseInt(listp[4]));
            GX.add(Integer.parseInt(listp[5]));
            GY.add(Integer.parseInt(listp[6]));
            GZ.add(Integer.parseInt(listp[7]));
        }

//        System.out.println("LISTT : " + listt);
//        System.out.println("LISTP : " + listp);
          System.out.println("TIME IN FUNCTION: " + time);
//        System.out.println("DATALF : " + dataLF);
//        System.out.println("AX : " + AX);
//        System.out.println("AY : " + AY);
//        System.out.println("AZ : " + AZ);
//        System.out.println("GX : " + GX);
//        System.out.println("GY : " + GY);
//        System.out.println("GZ : " + GZ);

        newList.add(time);
        newList.add(dataLF);
        newList.add(AX);
        newList.add(AY);
        newList.add(AZ);
        newList.add(GX);
        newList.add(GY);
        newList.add(GZ);

        //System.out.println(newList);

        return newList;
    }

    public List<Float> timeStringToFloat(List<String> timeListTemp1, String[][] newTime1) {

        for (int i = 0;i<timeListTemp1.size();i++) {
            newTime1[i] = timeListTemp1.get(i).split(":");
            //String hour = newTime[i][0];
            String min = newTime1[i][1];
            String sec = newTime1[i][2];
            String msec = newTime1[i][3];
            if (msec.length()==2){
                msec = '0'+msec;
            }
            String floatStringMin = min + "." + sec + msec;
            float floatTime = Float.parseFloat(floatStringMin);
            timeList.add(floatTime);
        }

        return timeList;
    }
}
