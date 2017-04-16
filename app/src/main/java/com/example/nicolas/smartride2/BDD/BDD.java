package com.example.nicolas.smartride2.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 10/02/2017.
 */

public class BDD {

    Gson gson = new Gson();

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "SmartRideBDD.db";

    //Profil Table
    private static final String TABLE_PROFIL = "TABLE_PROFIL";
    private static final String COL_PROFIL_ID = "PROFIL_ID";
    private static final int NUM_COL_PROFIL_ID = 0;
    private static final String COL_PROFIL_LOGIN = "PROFIL_LOGIN";
    private static final int NUM_COL_PROFIL_LOGIN =1;
    private static final String COL_PROFIL_PWD = "PROFIL_PWD";
    private static final int NUM_COL_PROFIL_PWD = 2;
    private static final String COL_PROFIL_NAME = "PROFIL_NAME";
    private static final int NUM_COL_PROFIL_NAME = 3;
    private static final String COL_PROFIL_SURNAME = "PROFIL_SURNAME";
    private static final int NUM_COL_PROFIL_SURNAME = 4;
    private static final String COL_PROFIL_AGE = "PROFIL_AGE";
    private static final int NUM_COL_PROFIL_AGE = 5;
    private static final String COL_PROFIL_CREATION = "PROFIL_CREATION";
    private static final int NUM_COL_PROFIL_CREATION = 6;

    //Run Table
    private static final String TABLE_RUN = "TABLE_RUN";
    private static final String COL_RUN_ID = "RUN_ID";
    private static final int NUM_COL_RUN_ID = 0;
    private static final String COL_RUN_NAME = "RUN_NAME";
    private static final int NUM_COL_RUN_NAME = 1;
    private static final String COL_RUN_DATE = "RUN_DATE";
    private static final int NUM_COL_RUN_DATE = 2;
    private static final String COL_RUN_PROFIL = "RUN_PROFIL";
    private static final int NUM_COL_RUN_PROFIL= 3;

    //Location Table
    private static final String TABLE_LOC = "TABLE_LOC";
    private static final String COL_LOC_ID = "LOC_ID";
    private static final int NUM_COL_LOC_ID = 0;
    private static final String COL_LOC_RUN_NAME = "LOC_RUN_NAME";
    private static final int NUM_COL_LOC_RUN_NAME = 1;
    private static final String COL_LOC = "LOC";
    private static final int NUM_COL_LOC = 2;
    private static final String COL_LOC_TIME = "LOC_TIME";
    private static final int NUM_COL_LOC_TIME = 3;
    private static final String COL_LOC_ALT = "LOC_ALT";
    private static final int NUM_COL_LOC_ALT = 4;

    //Accelerometer Table
    private static final String TABLE_ACCELEROMETER = "TABLE_ACCELEROMETER";
    private static final String COL_ACC_DATA_ID = "ACC_DATA_ID";
    private static final int NUM_COL_ACC_DATA_ID = 0;
    private static final String COL_ACC_RUN_NAME = "ACC_RUN_NAME";
    private static final int NUM_COL_ACC_RUN_NAME = 1;
    private static final String COL_ACC_RUN_PROFIL = "ACC_RUN_PROFIL";
    private static final int NUM_COL_ACC_RUN_PROFIL = 2;
    private static final String COL_ACC_DATA_X = "ACC_DATA_X";
    private static final int NUM_COL_ACC_DATA_X = 3;
    private static final String COL_ACC_DATA_Y = "ACC_DATA_Y";
    private static final int NUM_COL_ACC_DATA_Y = 4;
    private static final String COL_ACC_DATA_Z = "ACC_DATA_Z";
    private static final int NUM_COL_ACC_DATA_Z = 5;

    //Gyro Table
    private static final String TABLE_GYRO = "TABLE_GYRO";
    private static final String COL_GYRO_DATA_ID = "GYRO_DATA_ID";
    private static final int NUM_COL_GYRO_DATA_ID = 0;
    private static final String COL_GYRO_RUN_NAME = "GYRO_RUN_NAME";
    private static final int NUM_COL_GYRO_RUN_NAME = 1;
    private static final String COL_GYRO_RUN_PROFIL = "GYRO_RUN_PROFIL";
    private static final int NUM_COL_GYRO_RUN_PROFIL = 2;
    private static final String COL_GYRO_DATA_X = "GYRO_DATA_X";
    private static final int NUM_COL_GYRO_DATA_X = 3;
    private static final String COL_GYRO_DATA_Y = "GYRO_DATA_Y";
    private static final int NUM_COL_GYRO_DATA_Y = 4;
    private static final String COL_GYRO_DATA_Z = "GYRO_DATA_Z";
    private static final int NUM_COL_GYRO_DATA_Z = 5;



    private SQLiteDatabase bdd;

    private BaseSQLite maBaseSQLite;

    public BDD(Context context){
        //On créer la BDD et sa table
        maBaseSQLite = new BaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

//Localisation Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertLoc(Localisation localisation){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        Log.v("BDD",t1);
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        values.put(COL_LOC_ALT,localisation.getAltitude());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_LOC, null, values);
    }

    public int updateLoc(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        values.put(COL_LOC_ALT,localisation.getAltitude());
        return bdd.update(TABLE_LOC, values, COL_LOC_RUN_NAME + " = " +localisation.getNameOfRun(), null);
    }

    public Localisation getLocalisationWithRunName(String name){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_LOC, new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME, COL_LOC_ALT}, COL_LOC_RUN_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToLoc(c);
    }

    public List<Localisation> getAllLoc() {
        List<Localisation> locs = new ArrayList<Localisation>();
        Cursor cursor = bdd.query(TABLE_LOC,
                new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME , COL_LOC_ALT}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Localisation localisation = cursorToLoc(cursor);
            locs.add(localisation);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return locs;
    }

    private Localisation cursorToLoc(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //On créé un livre
        Localisation localisation = new Localisation(null,null,null,0);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        localisation.setNameOfRun(c.getString(NUM_COL_LOC_RUN_NAME));

        Type type2 = new TypeToken<LatLng>() {}.getType();
        LatLng location = gson.fromJson(c.getString(NUM_COL_LOC), type2);
        localisation.setLocation(location);

        Type type = new TypeToken<Time>() {}.getType();
        Time time = gson.fromJson(c.getString(NUM_COL_LOC_TIME), type);
        localisation.setTime(time);

        localisation.setAltitude(c.getDouble(NUM_COL_LOC_ALT));

        //On retourne le livre
        return localisation;
    }

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//Profil Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public long insertProfil(User user){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_PROFIL_LOGIN, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPassword());
        values.put(COL_PROFIL_NAME, user.getName());
        values.put(COL_PROFIL_SURNAME, user.getSurname());
        values.put(COL_PROFIL_AGE, user.getAge());
        String t2=gson.toJson(user.getCreationDate());
        values.put(COL_PROFIL_CREATION, t2);
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_PROFIL, null, values);
    }

    public int updateProfil(User user){
        ContentValues values = new ContentValues();
        values.put(COL_PROFIL_LOGIN, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPassword());
        values.put(COL_PROFIL_NAME, user.getName());
        values.put(COL_PROFIL_SURNAME, user.getSurname());
        values.put(COL_PROFIL_AGE, user.getAge());
        String t2=gson.toJson(user.getCreationDate());
        values.put(COL_PROFIL_CREATION, t2);
       return bdd.update(TABLE_PROFIL, values, COL_PROFIL_LOGIN + " LIKE \"" + user.getLogin() +"\"", null);
    }

    public User getUserWithLogin(String login){
        Cursor c = bdd.query(TABLE_PROFIL, new String[] {COL_PROFIL_ID, COL_PROFIL_LOGIN, COL_PROFIL_PWD, COL_PROFIL_NAME,COL_PROFIL_SURNAME,COL_PROFIL_AGE, COL_PROFIL_CREATION}, COL_PROFIL_LOGIN + " LIKE \"" + login +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToProfil(c);
    }

    public List<User> getAllProfil() {
        List<User> users = new ArrayList<User>();
        Cursor cursor = bdd.query(TABLE_PROFIL,
              new String[] {COL_PROFIL_ID, COL_PROFIL_LOGIN, COL_PROFIL_PWD, COL_PROFIL_NAME,COL_PROFIL_SURNAME, COL_PROFIL_AGE, COL_PROFIL_CREATION}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToProfil(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return users;
    }

    private User cursorToProfil(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Log.v("cursor", Integer.toString(c.getCount()));
        if (c.getCount() >= 1) {
            Log.v("cursor", "in the if");
            User user = new User(null, null, null, null, 0, null);
            Log.v("cursor", "after new user");
            //Log.v("cursor", "Namelogincolonne"+c.getString(1));
            user.setLogin(c.getString(NUM_COL_PROFIL_LOGIN));
            Log.v("cursor", "after set login"+user.getLogin());
            user.setPassword(c.getString(NUM_COL_PROFIL_PWD));
            Log.v("cursor", "after set pwd");
            user.setName(c.getString(NUM_COL_PROFIL_NAME));
            Log.v("cursor", "after set name");
            user.setSurname(c.getString(NUM_COL_PROFIL_SURNAME));
            Log.v("cursor", "after set Surname");
            user.setAge(c.getInt(NUM_COL_PROFIL_AGE));
            Type type = new TypeToken<Time>() {
            }.getType();
            Time time = gson.fromJson(c.getString(NUM_COL_PROFIL_CREATION), type);
            user.setCreationDate(time);
            Log.v("cursor", "after time");
            return user;
        } else  return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //Run Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertRun(Run run){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_RUN_NAME, run.getName());
        values.put(COL_RUN_PROFIL, run.getProfil());
        String t2=gson.toJson(run.getCreationDate());
        values.put(COL_RUN_DATE, t2);
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_RUN, null, values);
    }

    public int updateRun(Run run){
        ContentValues values = new ContentValues();
        values.put(COL_RUN_NAME, run.getName());
        values.put(COL_RUN_PROFIL, run.getProfil());
        String t2=gson.toJson(run.getCreationDate());
        values.put(COL_RUN_DATE, t2);
        return bdd.update(TABLE_RUN, values, COL_RUN_NAME + " LIKE \"" + run.getName() +"\"" +" AND " + COL_RUN_PROFIL + " LIKE \"" + run.getProfil() +"\"", null);
    }

    public Run getRunWithNameAndProfil(String name,String profil){
        Cursor c = bdd.query(TABLE_RUN, new String[] {COL_RUN_ID, COL_RUN_NAME, COL_RUN_DATE,COL_RUN_PROFIL}, COL_RUN_NAME + " LIKE \"" + name +"\"" +" AND " +COL_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToRun(c);
    }

    public List<Run> getAllRunWithProfil(String profil) {
        List<Run> runs = new ArrayList<Run>();
        Cursor cursor = bdd.query(TABLE_RUN,
                new String[] {COL_RUN_ID, COL_RUN_NAME, COL_RUN_DATE,COL_RUN_PROFIL}, COL_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Run run = cursorToRun(cursor);
            runs.add(run);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return runs;
    }

    private Run cursorToRun(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Log.v("cursor", Integer.toString(c.getCount()));
        if (c.getCount() >= 1) {
            Run run = new Run(null, null, null);
            run.setName(c.getString(NUM_COL_RUN_NAME));
            run.setProfil(c.getString(NUM_COL_RUN_PROFIL));
            Type type = new TypeToken<Time>() {
            }.getType();
            Time time = gson.fromJson(c.getString(NUM_COL_RUN_DATE), type);
            run.setCreationDate(time);
            return run;
        } else  return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Accelerometer Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertDataAcc(DataSensor dataSensor){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_ACC_RUN_NAME, dataSensor.getRunName());
        values.put(COL_ACC_RUN_PROFIL, dataSensor.getProfil());
        values.put(COL_ACC_DATA_X,  dataSensor.getDataX());
        values.put(COL_ACC_DATA_Y,  dataSensor.getDataY());
        values.put(COL_ACC_DATA_Z,  dataSensor.getDataZ());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_ACCELEROMETER, null, values);
    }

    public int updateDataAcc(DataSensor dataSensor){
        ContentValues values = new ContentValues();
        values.put(COL_ACC_RUN_NAME, dataSensor.getRunName());
        values.put(COL_ACC_RUN_PROFIL, dataSensor.getProfil());
        values.put(COL_ACC_DATA_X,  dataSensor.getDataX());
        values.put(COL_ACC_DATA_Y,  dataSensor.getDataY());
        values.put(COL_ACC_DATA_Z,  dataSensor.getDataZ());
        return bdd.update(TABLE_ACCELEROMETER, values, COL_ACC_RUN_NAME + " LIKE \"" + dataSensor.getRunName() +"\"" +" AND " + COL_ACC_RUN_PROFIL + " LIKE \"" + dataSensor.getProfil() +"\"", null);
    }

    public Run getDataAccWithRunNameAndProfil(String name,String profil){
        Cursor c = bdd.query(TABLE_ACCELEROMETER, new String[] {COL_ACC_DATA_ID, COL_ACC_RUN_NAME, COL_ACC_RUN_PROFIL, COL_ACC_DATA_X,COL_ACC_DATA_Y,COL_ACC_DATA_Z}, COL_ACC_RUN_NAME + " LIKE \"" + name +"\""+" AND " +COL_ACC_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToRun(c);
    }

    public List<DataSensor> getAllDataAccWithRunAndProfil(String name,String profil) {
        List<DataSensor> dataSensors = new ArrayList<DataSensor>();
        Cursor cursor = bdd.query(TABLE_ACCELEROMETER,
                new String[] {COL_ACC_DATA_ID, COL_ACC_RUN_NAME, COL_ACC_RUN_PROFIL, COL_ACC_DATA_X,COL_ACC_DATA_Y,COL_ACC_DATA_Z}, COL_ACC_RUN_NAME + " LIKE \"" + name +"\"" +" AND " +COL_ACC_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DataSensor dataSensor = cursorToAccData(cursor);
            dataSensors.add(dataSensor);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return dataSensors;
    }

    private DataSensor cursorToAccData(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Log.v("cursor", Integer.toString(c.getCount()));
        if (c.getCount() >= 1) {
            DataSensor dataSensor = new DataSensor(null, null,null,null,null);
            dataSensor.setRunName(c.getString(NUM_COL_ACC_RUN_NAME));
            dataSensor.setProfil(c.getString(NUM_COL_ACC_RUN_PROFIL));
            dataSensor.setDataX(c.getString(NUM_COL_ACC_DATA_X));
            dataSensor.setDataY(c.getString(NUM_COL_ACC_DATA_Y));
            dataSensor.setDataZ(c.getString(NUM_COL_ACC_DATA_Z));
            return dataSensor;
        } else  return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Gyro Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertDataGyro(DataSensor dataSensor){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_GYRO_RUN_NAME, dataSensor.getRunName());
        values.put(COL_GYRO_RUN_PROFIL, dataSensor.getProfil());
        values.put(COL_GYRO_DATA_X,  dataSensor.getDataX());
        values.put(COL_GYRO_DATA_Y,  dataSensor.getDataY());
        values.put(COL_GYRO_DATA_Z,  dataSensor.getDataZ());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_GYRO, null, values);
    }

    public int updateDataGyro(DataSensor dataSensor){
        ContentValues values = new ContentValues();
        values.put(COL_GYRO_RUN_NAME, dataSensor.getRunName());
        values.put(COL_GYRO_RUN_PROFIL, dataSensor.getProfil());
        values.put(COL_GYRO_DATA_X,  dataSensor.getDataX());
        values.put(COL_GYRO_DATA_Y,  dataSensor.getDataY());
        values.put(COL_GYRO_DATA_Z,  dataSensor.getDataZ());
        return bdd.update(TABLE_GYRO, values, COL_GYRO_RUN_NAME + " LIKE \"" + dataSensor.getRunName() +"\"" +" AND " + COL_GYRO_RUN_PROFIL + " LIKE \"" + dataSensor.getProfil() +"\"", null);
    }

    public Run getDataGyroWithRunNameAndProfil(String name,String profil){
        Cursor c = bdd.query(TABLE_GYRO, new String[] {COL_GYRO_DATA_ID, COL_GYRO_RUN_NAME, COL_GYRO_RUN_PROFIL, COL_GYRO_DATA_X,COL_GYRO_DATA_Y,COL_GYRO_DATA_Z}, COL_GYRO_RUN_NAME + " LIKE \"" + name +"\""+" AND " +COL_GYRO_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToRun(c);
    }

    public List<DataSensor> getAllDataGyroWithRunAndProfil(String name,String profil) {
        List<DataSensor> dataSensors = new ArrayList<DataSensor>();
        Cursor cursor = bdd.query(TABLE_GYRO,
                new String[] {COL_GYRO_DATA_ID, COL_GYRO_RUN_NAME, COL_GYRO_RUN_PROFIL, COL_GYRO_DATA_X,COL_GYRO_DATA_Y,COL_GYRO_DATA_Z}, COL_GYRO_RUN_NAME + " LIKE \"" + name +"\"" +" AND " +COL_GYRO_RUN_PROFIL + " LIKE \"" + profil +"\"", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DataSensor dataSensor = cursorToGyroData(cursor);
            dataSensors.add(dataSensor);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return dataSensors;
    }

    private DataSensor cursorToGyroData(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Log.v("cursor", Integer.toString(c.getCount()));
        if (c.getCount() >= 1) {
            DataSensor dataSensor = new DataSensor(null, null,null,null,null);
            dataSensor.setRunName(c.getString(NUM_COL_GYRO_RUN_NAME));
            dataSensor.setProfil(c.getString(NUM_COL_GYRO_RUN_PROFIL));
            dataSensor.setDataX(c.getString(NUM_COL_GYRO_DATA_X));
            dataSensor.setDataY(c.getString(NUM_COL_GYRO_DATA_Y));
            dataSensor.setDataZ(c.getString(NUM_COL_GYRO_DATA_Z));
            return dataSensor;
        } else  return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void clearTable(String nameoftable){
        bdd=maBaseSQLite.getWritableDatabase();
        bdd.delete(nameoftable,null,null);
        bdd.close();
    }
}
