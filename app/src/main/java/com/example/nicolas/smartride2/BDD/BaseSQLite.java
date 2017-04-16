package com.example.nicolas.smartride2.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Valentin on 10/02/2017.
 */

public class BaseSQLite extends SQLiteOpenHelper {


    //Profil Table
    private static final String TABLE_PROFIL = "TABLE_PROFIL";
    private static final String COL_PROFIL_ID = "PROFIL_ID";
    private static final String COL_PROFIL_LOGIN = "PROFIL_LOGIN";
    private static final String COL_PROFIL_PWD = "PROFIL_PWD";
    private static final String COL_PROFIL_NAME = "PROFIL_NAME";
    private static final String COL_PROFIL_SURNAME = "PROFIL_SURNAME";
    private static final String COL_PROFIL_AGE = "PROFIL_AGE";
    private static final String COL_PROFIL_CREATION = "PROFIL_CREATION";

    private static final String CREATE_TABLE_PROFIL = "CREATE TABLE " + TABLE_PROFIL + " ("
            + COL_PROFIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_PROFIL_LOGIN + " TEXT, "+ COL_PROFIL_PWD + " TEXT, "
            +COL_PROFIL_NAME + " TEXT, "+ COL_PROFIL_SURNAME + " TEXT, "+ COL_PROFIL_AGE + " REAL, " +  COL_PROFIL_CREATION+" TEXT);";

    //Run Table
    private static final String TABLE_RUN = "TABLE_RUN";
    private static final String COL_RUN_ID = "RUN_ID";
    private static final String COL_RUN_NAME = "RUN_NAME";
    private static final String COL_RUN_DATE = "RUN_DATE";
    private static final String COL_RUN_PROFIL = "RUN_PROFIL";

    private static final String CREATE_TABLE_RUN = "CREATE TABLE " + TABLE_RUN + " ("
            + COL_RUN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_RUN_NAME + " TEXT, "+ COL_RUN_DATE +" TEXT, " + COL_RUN_PROFIL + " TEXT );";

    //Localisation Table
    private static final String TABLE_LOC = "TABLE_LOC";
    private static final String COL_LOC_ID = "LOC_ID";
    private static final String COL_LOC_RUN_NAME = "LOC_RUN_NAME";
    private static final String COL_LOC = "LOC";
    private static final String COL_LOC_TIME = "LOC_TIME";
    private static final String COL_LOC_ALT = "LOC_ALT";

    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOC + " ("
            + COL_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_LOC_RUN_NAME + " TEXT, "+ COL_LOC +" TEXT, " + COL_LOC_TIME+ " TEXT, " + COL_LOC_ALT + " TEXT );";

    //Accelerometer Table
    private static final String TABLE_ACCELEROMETER = "TABLE_ACCELEROMETER";
    private static final String COL_ACC_DATA_ID = "ACC_DATA_ID";
    private static final String COL_ACC_RUN_NAME = "ACC_RUN_NAME";
    private static final String COL_ACC_RUN_PROFIL = "ACC_RUN_PROFIL";
    private static final String COL_ACC_DATA_X = "ACC_DATA_X";
    private static final String COL_ACC_DATA_Y = "ACC_DATA_Y";
    private static final String COL_ACC_DATA_Z = "ACC_DATA_Z";


    private static final String CREATE_TABLE_ACCELEROMETER = "CREATE TABLE " + TABLE_ACCELEROMETER + " ("
            + COL_ACC_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ACC_RUN_NAME + " TEXT, "+ COL_ACC_RUN_PROFIL +" TEXT, "+ COL_ACC_DATA_X +" TEXT, "+ COL_ACC_DATA_Y +" TEXT, " + COL_ACC_DATA_Z + " TEXT );";


    //Gyro Table
    private static final String TABLE_GYRO = "TABLE_GYRO";
    private static final String COL_GYRO_DATA_ID = "GYRO_DATA_ID";
    private static final String COL_GYRO_RUN_NAME = "GYRO_RUN_NAME";
    private static final String COL_GYRO_RUN_PROFIL = "GYRO_RUN_PROFIL";
    private static final String COL_GYRO_DATA_X = "GYRO_DATA_X";
    private static final String COL_GYRO_DATA_Y = "GYRO_DATA_Y";
    private static final String COL_GYRO_DATA_Z = "GYRO_DATA_Z";


    private static final String CREATE_TABLE_GYRO = "CREATE TABLE " + TABLE_GYRO + " ("
            + COL_GYRO_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_GYRO_RUN_NAME + " TEXT, "+ COL_GYRO_RUN_PROFIL +" TEXT, "+ COL_GYRO_DATA_X +" TEXT, "+ COL_GYRO_DATA_Y +" TEXT, " + COL_GYRO_DATA_Z + " TEXT );";



    public BaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_TABLE_PROFIL);
        db.execSQL(CREATE_TABLE_RUN);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_ACCELEROMETER);
        db.execSQL(CREATE_TABLE_GYRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_PROFIL + ";");
        db.execSQL("DROP TABLE " + TABLE_RUN + ";");
        db.execSQL("DROP TABLE " + TABLE_LOC + ";");
        db.execSQL("DROP TABLE " + TABLE_ACCELEROMETER + ";");
        db.execSQL("DROP TABLE " + TABLE_GYRO + ";");

        onCreate(db);
    }

}

