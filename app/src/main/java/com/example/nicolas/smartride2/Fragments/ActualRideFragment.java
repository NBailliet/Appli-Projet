package com.example.nicolas.smartride2.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View actualRideView = inflater.inflate(R.layout.actualride, container, false);
        textViewAX = (TextView) actualRideView.findViewById(R.id.textGraphAX);
        textViewAY = (TextView) actualRideView.findViewById(R.id.textGraphAY);
        textViewAZ = (TextView) actualRideView.findViewById(R.id.textGraphAZ);
        textViewGX = (TextView) actualRideView.findViewById(R.id.textGraphGX);
        textViewGY = (TextView) actualRideView.findViewById(R.id.textGraphGY);
        textViewGZ = (TextView) actualRideView.findViewById(R.id.textGraphGZ);


        InputStream inputStream = getResources().openRawResource(R.raw.run5skig);
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

        for (int i = 0;i<timeListTemp.size();i++) {
            newTime[i] = timeListTemp.get(i).split(":");
            int hour = Integer.parseInt(newTime[i][0]);
            int min = Integer.parseInt(newTime[i][1]);
            int sec = Integer.parseInt(newTime[i][2]);
            int msec = Integer.parseInt(newTime[i][3]);
            String floatStringMin = hour*60 + min + "." + sec + msec;
            float floatTime = Float.parseFloat(floatStringMin);
            timeList.add(floatTime);
        }
        System.out.println("TIMELIST : " + timeList);



        Spinner spinner = (Spinner) actualRideView.findViewById(R.id.ride_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.actual_ride_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        /*LineGraphSeries<DataPoint> series = null;
        GraphView graph = (GraphView) actualRideView.findViewById(R.id.graphAX);
        for (int i=0;i<timeList.size();i++) {
            if (i==0) {

                series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(timeList.get(i), AXList.get(i))
                });

            }
            else {
                series.appendData(new DataPoint(timeList.get(i),AXList.get(i)),false,timeList.size());
            }
        }
        graph.addSeries(series);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        series.setBackgroundColor(Color.RED);
        series.setAnimated(true);
        series.setDrawDataPoints(true);*/


        //GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        //gridLabelRenderer.draw();
        //gridLabelRenderer.reloadStyles();

//        GraphView graph2 = (GraphView) actualRideView.findViewById(R.id.graph2);
//        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, -1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph2.addSeries(series2);
//        graph2.getViewport().setScalable(true);
//        graph2.getViewport().setScalableY(true);
//
//        // styling
//        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//            @Override
//            public int get(DataPoint data) {
//                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
//            }
//        });
//
//        series2.setSpacing(50);
//
//        // draw values on top
//        series2.setDrawValuesOnTop(true);
//        series2.setValuesOnTopColor(Color.WHITE);
//        series2.setAnimated(true);
//        //series2.setValuesOnTopSize(50);
//
//        GraphView graph3 = (GraphView) actualRideView.findViewById(R.id.graph3);
//        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, -2),
//                new DataPoint(1, 1),
//                new DataPoint(2, 8),
//                new DataPoint(3, 10),
//                new DataPoint(4, 5)
//        });
//        graph3.addSeries(series3);
//        graph3.getViewport().setScalable(true);
//        graph3.getViewport().setScalableY(true);
//
//        series3.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//            @Override
//            public int get(DataPoint data) {
//                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
//            }
//        });
//
//        series3.setSpacing(50);
//
//        // draw values on top
//        series3.setDrawValuesOnTop(true);
//        series3.setValuesOnTopColor(Color.WHITE);
//        series3.setAnimated(true);
//
//        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 3),
//                new DataPoint(1, 3),
//                new DataPoint(2, 6),
//                new DataPoint(3, 2),
//                new DataPoint(4, 5)
//        });
//        graph3.addSeries(series4);
//        series4.setColor(Color.DKGRAY);
//        series4.setAnimated(true);
//        series4.setDrawDataPoints(true);
//
//        GraphView graph4 = (GraphView) actualRideView.findViewById(R.id.graph4);
//        PointsGraphSeries<DataPoint> series5 = new PointsGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, -2),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph4.addSeries(series5);
//        graph4.getViewport().setScalable(true);
//        graph4.getViewport().setScalableY(true);
//        series5.setShape(PointsGraphSeries.Shape.POINT);
//
//        PointsGraphSeries<DataPoint> series6 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, -1),
//                new DataPoint(1, 4),
//                new DataPoint(2, 2),
//                new DataPoint(3, 1),
//                new DataPoint(4, 5)
//        });
//        graph4.addSeries(series6);
//        series6.setShape(PointsGraphSeries.Shape.RECTANGLE);
//        series6.setColor(Color.RED);
//
//        PointsGraphSeries<DataPoint> series7 = new PointsGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 0),
//                new DataPoint(1, 3),
//                new DataPoint(2, 1),
//                new DataPoint(3, 0),
//                new DataPoint(4, 4)
//        });
//        graph4.addSeries(series7);
//        series7.setShape(PointsGraphSeries.Shape.TRIANGLE);
//        series7.setColor(Color.YELLOW);
//
//        PointsGraphSeries<DataPoint> series8 = new PointsGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 2),
//                new DataPoint(2, 0),
//                new DataPoint(3, -1),
//                new DataPoint(4, 3)
//        });
//        graph4.addSeries(series8);
//        series8.setColor(Color.GREEN);
//        series8.setCustomShape(new PointsGraphSeries.CustomShape() {
//            @Override
//            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
//                paint.setStrokeWidth(10);
//                canvas.drawLine(x-20, y-20, x+20, y+20, paint);
//                canvas.drawLine(x+20, y-20, x-20, y+20, paint);
//            }
//        });
        return actualRideView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();

        switch (index) {
            case 0 :
                Toast.makeText(getActivity(), "SWITCH SKI GAUCHE SPINNER", Toast.LENGTH_SHORT).show();


                break;
            case 1 :
                Toast.makeText(getActivity(), "SWITCH SKI DROIT SPINNER", Toast.LENGTH_SHORT).show();

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
//        System.out.println("TIME : " + time);
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
}
