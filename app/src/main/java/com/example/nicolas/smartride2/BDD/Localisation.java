package com.example.nicolas.smartride2.BDD;

import android.location.Location;

/**
 * Created by Valentin on 10/02/2017.
 */

public class Localisation {

    String NameOfRun;
    Location location;
    Time time;

    public Localisation(String nameOfRun, Location location, Time time) {
        NameOfRun = nameOfRun;
        this.location = location;
        this.time = time;
    }

    public String getNameOfRun() {
        return NameOfRun;
    }

    public void setNameOfRun(String nameOfRun) {
        NameOfRun = nameOfRun;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}
