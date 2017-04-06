package com.example.nicolas.smartride2.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolas.smartride2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class ActualRideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View actualRideView = inflater.inflate(R.layout.actualride, container, false);

        GraphView graph = (GraphView) actualRideView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph.addSeries(series);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        series.setBackgroundColor(Color.RED);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        //GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        //gridLabelRenderer.draw();
        //gridLabelRenderer.reloadStyles();

        GraphView graph2 = (GraphView) actualRideView.findViewById(R.id.graph2);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph2.addSeries(series2);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);

        // styling
        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series2.setSpacing(50);

        // draw values on top
        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.WHITE);
        series2.setAnimated(true);
        //series2.setValuesOnTopSize(50);

        GraphView graph3 = (GraphView) actualRideView.findViewById(R.id.graph3);
        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -2),
                new DataPoint(1, 1),
                new DataPoint(2, 8),
                new DataPoint(3, 10),
                new DataPoint(4, 5)
        });
        graph3.addSeries(series3);
        graph3.getViewport().setScalable(true);
        graph3.getViewport().setScalableY(true);

        series3.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series3.setSpacing(50);

        // draw values on top
        series3.setDrawValuesOnTop(true);
        series3.setValuesOnTopColor(Color.WHITE);
        series3.setAnimated(true);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        graph3.addSeries(series4);
        series4.setColor(Color.DKGRAY);
        series4.setAnimated(true);
        series4.setDrawDataPoints(true);

        GraphView graph4 = (GraphView) actualRideView.findViewById(R.id.graph4);
        PointsGraphSeries<DataPoint> series5 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -2),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph4.addSeries(series5);
        graph4.getViewport().setScalable(true);
        graph4.getViewport().setScalableY(true);
        series5.setShape(PointsGraphSeries.Shape.POINT);

        PointsGraphSeries<DataPoint> series6 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, -1),
                new DataPoint(1, 4),
                new DataPoint(2, 2),
                new DataPoint(3, 1),
                new DataPoint(4, 5)
        });
        graph4.addSeries(series6);
        series6.setShape(PointsGraphSeries.Shape.RECTANGLE);
        series6.setColor(Color.RED);

        PointsGraphSeries<DataPoint> series7 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 3),
                new DataPoint(2, 1),
                new DataPoint(3, 0),
                new DataPoint(4, 4)
        });
        graph4.addSeries(series7);
        series7.setShape(PointsGraphSeries.Shape.TRIANGLE);
        series7.setColor(Color.YELLOW);

        PointsGraphSeries<DataPoint> series8 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 2),
                new DataPoint(2, 0),
                new DataPoint(3, -1),
                new DataPoint(4, 3)
        });
        graph4.addSeries(series8);
        series8.setColor(Color.GREEN);
        series8.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });
        return actualRideView;
    }
}
