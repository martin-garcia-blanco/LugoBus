package com.example.martin.lugobus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {


    public DBOpenHelper(Context context) {
        super(context, "lugobus.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE linea (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, " +
                "inicio TEXT NOT NULL, fin TEXT NOT NULL,actividad TEXT NOT NULL,numeroParadas INTEGER NOT NULL);");

        //TODO cando toda a app funcione, arreglarei estas 2 taboas facendo unha soa taboa parada.

        db.execSQL("CREATE TABLE paradaReciente(_id INTEGER NOT NULL PRIMARY KEY,nombre_parada TEXT NOT NULL, calle TEXT NOT NULL, lineasCoincidentes TEXT);");
        db.execSQL("CREATE TABLE paradaFavorita(_id INTEGER NOT NULL PRIMARY KEY,nombre_parada TEXT NOT NULL, calle TEXT NOT NULL, lineasCoincidentes TEXT);");
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
