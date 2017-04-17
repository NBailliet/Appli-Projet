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
import android.widget.Toast;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.DataSensor;
import com.example.nicolas.smartride2.CSVFile;
import com.example.nicolas.smartride2.R;
import com.example.nicolas.smartride2.SessionManager;
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
    List<Long> dataAX= new ArrayList<>();
    List<Long> dataAY= new ArrayList<>();
    List<Long> dataAZ= new ArrayList<>();
    List<Long> dataATime= new ArrayList<>();
    List<Long> dataGX= new ArrayList<>();
    List<Long> dataGY= new ArrayList<>();
    List<Long> dataGZ= new ArrayList<>();
    List<Long> dataGTime= new ArrayList<>();

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

  /*      InputStream inputStream = getResources().openRawResource(R.raw.run5skig);
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
*/


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
        bdd.open();
        List<DataSensor> dataSensorsA = new ArrayList<DataSensor>();
        dataSensorsA=bdd.getAllDataAccWithRunAndProfil("Run#1",session.getLoginPref());
        List<DataSensor> dataSensorsG = new ArrayList<DataSensor>();
        dataSensorsG=bdd.getAllDataGyroWithRunAndProfil("Run#1",session.getLoginPref());
       // Log.d("bdd","length Acc ="+dataSensorsA.size());
        //Log.d("bdd","length Gyro ="+dataSensorsG.size());
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
