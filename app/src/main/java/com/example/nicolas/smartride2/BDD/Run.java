package com.example.nicolas.smartride2.BDD;

/**
 * Created by Valentin on 13/04/2017.
 */

public class Run {
    private String name;
    private Time creationDate;
    private String profil;

    public Run(String name, Time creationDate, String profil) {
        this.name = name;
        this.creationDate = creationDate;
        this.profil = profil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Time creationDate) {
        this.creationDate = creationDate;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }
}
