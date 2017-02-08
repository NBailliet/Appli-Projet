package com.example.nicolas.smartride2.bdd;

/**
 * Created by Valentin on 03/02/2017.
 */

public class DataBDD {/*
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "sensors.db";

    private static final String TABLE_DATA = "table_data";
    private static final String COL_TYPE_SENSOR = "TYPE_SENSOR";
    private static final int NUM_COL_TYPE_SENSOR= 0;
    private static final String COL_DATA_SENSOR = "TYPE_SENSOR";
    private static final int NUM_COL_DATA_SENSOR = 1;
    private static final String COL_TIME = "TYPE_SENSOR";
    private static final int NUM_COL_TIME = 2;


    private SQLiteDatabase bdd;

    private BaseSQLite maBaseSQLite;

    public DataBDD(Context context){
        //On crée la BDD et sa table
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

    public long insertLivre(Data livre){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_TYPE_SENSOR, livre.getType());
        values.put(COL_DATA_SENSOR, livre.getValues());
        values.put(COL_TIME, livre.getTime());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_DATA, null, values);
    }

    public int updateLivre(int id, Data livre){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_DATA_SENSOR, livre.getIsbn());
        values.put(COL_TITRE, livre.getTitre());
        return bdd.update(TABLE_DATA, values, COL_ID + " = " +id, null);
    }

    public int removeLivreWithID(int id){
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_LIVRES, COL_ID + " = " +id, null);
    }

    public Data getLivreWithTitre(String titre){
        //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_DATA, new String[] {COL_TYPE_SENSOR, COL_DATA_SENSOR, COL_TIME}, COL_TIME + " LIKE \"" + titre +"\"", null, null, null, null);
        return cursorToLivre(c);
    }

    //Cette méthode permet de convertir un cursor en un livre
    private Data cursorToLivre(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Data data = new Data();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        data.setId(c.getInt(NUM_COL_TYPE_SENSOR));
        data.setIsbn(c.getString(NUM_COL_DATA_SENSOR));
        data.setTitre(c.getString(NUM_COL_TIME));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return Data;
    }*/
}
