package com.example.nicolas.smartride2.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Created by Valentin on 03/02/2017.
*/



public class BaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_DATA = "table_data";
    private static final String COL_TYPE_SENSOR = "TYPE_SENSOR";
    private static final String COL_DATA_SENSOR = "TYPE_SENSOR";
    private static final String COL_TIME = "TYPE_SENSOR";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_DATA + " ("
            + COL_TYPE_SENSOR + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DATA_SENSOR + "REAL,"+ COL_TIME +"REAL );";

    public BaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_DATA + ";");
        onCreate(db);
    }

}

