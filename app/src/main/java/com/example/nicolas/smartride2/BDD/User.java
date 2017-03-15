package com.example.nicolas.smartride2.BDD;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User {

    private String login;
    private String password;
    private String name;
    private String surname;
    private int age;
    private Time creationDate;

    public String getLogin() {return login;}
    public String getPassword() {return password;}
    public String getName() {return this.name;}
    public String getSurname() {return this.surname;}
    public int getAge() {return this.age;}
    public Time getCreationDate() {
        return creationDate;
    }

    public User(String login, String password, String name, String surname, int age, Time creationDate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.creationDate = creationDate;
    }

    public User(String login){
        this.login=login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCreationDate(Time creationDate) {
        this.creationDate = creationDate;
    }



}
