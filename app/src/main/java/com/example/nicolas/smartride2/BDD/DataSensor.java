package com.example.nicolas.smartride2.BDD;

/**
 * Created by Valentin on 15/04/2017.
 */

public class DataSensor {
    String runName;
    String profil;
    String dataX;
    String dataY;
    String dataZ;

    public DataSensor( String runName, String profil, String dataX, String dataY, String dataZ) {
        this.runName = runName;
        this.profil = profil;
        this.dataX = dataX;
        this.dataY = dataY;
        this.dataZ = dataZ;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getDataX() {
        return dataX;
    }

    public void setDataX(String dataX) {
        this.dataX = dataX;
    }

    public String getDataY() {
        return dataY;
    }

    public void setDataY(String dataY) {
        this.dataY = dataY;
    }

    public String getDataZ() {
        return dataZ;
    }

    public void setDataZ(String dataZ) {
        this.dataZ = dataZ;
    }
}
