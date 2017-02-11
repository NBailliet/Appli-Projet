package com.example.nicolas.smartride2.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

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

    private static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "SmartRideBDD.db";

    //Profil Table
    private static final String TABLE_PROFIL = "TABLE_PROFIL";
    private static final String COL_PROFIL_ID = "PROFIL_ID";
    private static final int NUM_COL_PROFIL_ID = 0;
    private static final String COL_PROFIL_NAME = "PROFIL_NAME";
    private static final int NUM_COL_PROFIL_NAME = 1;
    private static final String COL_PROFIL_PWD = "PROFIL_PWD";
    private static final int NUM_COL_PROFIL_PWD = 2;

    //Run Table
    private static final String TABLE_RUN = "table_run";
    private static final String COL_RUN_ID = "run_ID";
    private static final int NUM_COL_RUN_ID = 0;
    private static final String COL_RUN_NAME = "Loc_run_name";
    private static final int NUM_COL_RUN_NAME = 1;
    private static final String COL_RUN_DATE = "run_date";
    private static final int NUM_COL_RUN_DATE = 2;
    private static final String COL_RUN_PROFIL = "run_profil";
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
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_LOC, null, values);
    }

    public int updateLoc(int id, Localisation localisation){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        return bdd.update(TABLE_LOC, values, COL_LOC_ID + " = " +id, null);
    }

    public Localisation getLocalisationWithRunName(String name){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_LOC, new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME}, COL_LOC_RUN_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToLoc(c);
    }

    public List<Localisation> getAllLoc() {
        List<Localisation> locs = new ArrayList<Localisation>();
        Cursor cursor = bdd.query(TABLE_LOC,
                new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME}, null, null, null, null, null);
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
        Localisation localisation = new Localisation(null,null,null);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        localisation.setNameOfRun(c.getString(NUM_COL_LOC_RUN_NAME));

        Type type2 = new TypeToken<Location>() {}.getType();
        Location location = gson.fromJson(c.getString(NUM_COL_LOC), type2);
        localisation.setLocation(location);

        Type type = new TypeToken<Time>() {}.getType();
        Time time = gson.fromJson(c.getString(NUM_COL_LOC_TIME), type);
        localisation.setTime(time);

        //On retourne le livre
        return localisation;
    }

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//Profil Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 /*   public long insertProfil(User user){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_PROFIL_NAME, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPWD );
       /* String t2=gson.toJson(user.getTime());
        values.put(COL_LOC_TIME, t2);*/ //todo add date of profil creation
        //on insère l'objet dans la BDD via le ContentValues
  /*      return bdd.insert(TABLE_LOC, null, values);
    }

    public int updateProfil(int id, User user){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_PROFIL_NAME, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPWD );
       /* String t2=gson.toJson(user.getTime());
        values.put(COL_LOC_TIME, t2);*/ //todo add date of profil creation
  /*      return bdd.update(TABLE_PROFIL, values, COL_PROFIL_ID + " = " +id, null);
    }

    public User getUserWithLogin(String Login){
       */ //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
      //  Cursor c = bdd.query(TABLE_LOC, new String[] {COL_PROFIL_ID, COL_PROFIL_NAME, COL_PROFIL_PWD/*, COL_LOC_TIME*/}, COL_PROFIL_NAME + " LIKE \"" + login +"\"", null, null, null, null);
    /*    return cursorToProfil(c);
    }

    public List<User> getAllProfil() {
        List<User> users = new ArrayList<User>();
        Cursor cursor = bdd.query(TABLE_PROFIL,
        *///        new String[] {COL_PROFIL_ID, COL_PROFIL_NAME, COL_PROFIL_PWD/*, COL_LOC_TIME*/}, null, null, null, null, null);
      /*  cursor.moveToFirst();
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
        if (c.getCount() == 0)
            return null;

        //On créé un livre
        User user = new User(null,null,null);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        user.setLogin(c.getString(NUM_COL_PROFIL_NAME));
        user.setPWD(c.getString(NUM_COL_PROFIL_PWD));

       /* Type type = new TypeToken<Time>() {}.getType();
        Time time = gson.fromJson(c.getString(NUM_COL_LOC_TIME), type);
        user.setTime(time);*/

        //On retourne le livre
 /*       return user;
    }*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void clearTable(String nameoftable){
        bdd=maBaseSQLite.getWritableDatabase();
        bdd.delete(nameoftable,null,null);
    }
}
