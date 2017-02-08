package com.example.nicolas.smartride2.bdd;

/**
 * Created by Valentin on 03/02/2017.
 */

public class Data {
    private int type;
    private float[] values;
    private float[] time;

    public Data(){}


    public Data(int type, float[] values, float[] time) {
        this.type = type;
        this.values = values;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public float[] getTime() {
        return time;
    }

    public void setTime(float[] time) {
        this.time = time;
    }
}
