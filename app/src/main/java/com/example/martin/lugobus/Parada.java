package com.example.martin.lugobus;

import android.content.ContentValues;
import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Parada {

    private long id_parada;
    private String nombre_parada;
    private String calle;
    private String lineasCoincidentes;

    private final static String taboa = "parada";
    private final static String[] columnas = {"_id", "nombre_parada", "calle","lineasCoincidentes"};

    public Parada(long id_parada, String nombre_parada, String calle, String lineasCoincidentes) {
        super();
        this.id_parada = id_parada;
        this.nombre_parada = nombre_parada;
        this.calle = calle;
        this.lineasCoincidentes=lineasCoincidentes;
    }

    public long getId() {
        return id_parada;
    }

    public String getNombre() {
        return nombre_parada;
    }

    public String getCalle() {
        return calle;
    }

    public String getLineasCoincidentes() {
        return lineasCoincidentes;
    }

    public static Cursor getAllCursor() {
        return MainActivity.getDb().query(taboa, columnas, null, null, null, null, null);
    }

    public static void crearParadaDendeXML(Document xmlParadas) {
        Parada.borrarTodas();

        Element raiz = xmlParadas.getDocumentElement();
        NodeList nl = raiz.getElementsByTagName("linea");

        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            //extaemos valores del xml
            String id = e.getAttribute("id");
            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            String calle = e.getElementsByTagName("calle").item(0).getTextContent();
            String lineas= e.getElementsByTagName("lineas").item(0).getTextContent();
            System.out.println("id:  " + id + "****** nombre: " + nombre + " ****** Calle: " + calle);
            Parada parada = new Parada(Long.parseLong(id), nombre, calle, lineas);
            parada.gardarParada();

        }
    }

    //Debería de ser público e recibir por parámetro unha linea
    private void gardarParada() {
        ContentValues cv = new ContentValues();

        cv.put(columnas[0], this.id_parada);
        cv.put(columnas[1], this.nombre_parada);
        cv.put(columnas[2], this.calle);
        cv.put(columnas[3], this.calle);


        MainActivity.getDb().insert(taboa, null, cv);
    }

    public static Parada buscarPorId(long idParada) {
        Parada parada = null;

        Cursor c = MainActivity.getDb().query(taboa, columnas, "_id=?", new String[]{String.valueOf(idParada)}, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(0);
            String nombre = c.getString(1);
            String calle = c.getString(2);


            parada = new Parada(id, nombre, calle,null);
        }

        return parada;
    }

    public static void borrarTodas() {
        MainActivity.getDb().delete(taboa, null, null);
    }


    public static Cursor paradasFavoritas(){
        return MainActivity.getDb().query("paradaFavorita", columnas, null, null, null, null, null);
    }

    public static Parada buscarParadaFavoritaId(long idParada){
        Parada parada = null;

        Cursor c = MainActivity.getDb().query("paradaFavorita", columnas, "_id=?", new String[]{String.valueOf(idParada)}, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(0);
            String nombre = c.getString(1);
            String calle = c.getString(2);


            parada = new Parada(id, nombre, calle,null);
        }

        return parada;
    }


    public static Cursor paradasRecientes(){
        return MainActivity.getDb().query("paradaReciente", columnas, null, null, null, null, null);
    }


    public static Parada buscarParadaRecientePorId(long idParada) {
        Parada parada = null;

        Cursor c = MainActivity.getDb().query("paradaReciente", columnas, "_id=?", new String[]{String.valueOf(idParada)}, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(0);
            String nombre = c.getString(1);
            String calle = c.getString(2);



            System.out.println("id: "+id+" nombre: "+nombre+" calle: "+calle);
            parada = new Parada(id, nombre, calle,null);
        }

        return parada;
    }

    // public static void borrarParadasRecientes(){
    //    MainActivity.getDb().delete("paradaReciente", null, null);
    //}


    public void gardarParadaReciente(){
        ContentValues cv = new ContentValues();

        cv.put(columnas[0], this.id_parada);
        cv.put(columnas[1], this.nombre_parada);
        cv.put(columnas[2], this.calle);
        cv.put(columnas[3], this.lineasCoincidentes);

        MainActivity.getDb().insert("paradaReciente", null, cv);
    }

    public void gardarParadaFavorita() {
        ContentValues cv = new ContentValues();

        cv.put(columnas[0], this.id_parada);
        cv.put(columnas[1], this.nombre_parada);
        cv.put(columnas[2], this.calle);
        cv.put(columnas[3], this.lineasCoincidentes);

        MainActivity.getDb().insert("paradaFavorita", null, cv);
    }
}
