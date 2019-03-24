package com.example.martin.lugobus;

import android.content.ContentValues;
import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Linea {

    private long id;
    private String nombre;
    private String inicio;
    private String fin;
    private String actividad;
    private int numeroParadas;

    private final static String taboa = "linea";
    private final static String[] columnas = {"_id", "nombre", "inicio", "fin", "actividad", "numeroParadas"};


    public Linea(long id, String nombre, String inicio, String fin, String actividad, int numeroParadas) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.actividad = actividad;
        this.numeroParadas = numeroParadas;
    }

    public static Cursor lineasFavoritas() {

        return null;
    }

    public long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public String getActividad() {
        return actividad;
    }

    public int getNumeroParadas() {
        return numeroParadas;
    }

    public static Cursor getAllCursor() {
        return MainActivity.getDb().query(taboa, columnas, null, null, null, null, null);
    }


   /* public String NomeLineaPorId(long idLinea){
        Cursor c = MainActivity.getDb().query(taboa, columnas, "_id=?", new String[]{String.valueOf(idLinea)}, null, null, null);



        return null;
    }*/

    public static void crearLineaDendeXML(Document xmlLineas) {
        Linea.borrarTodas();

        Element raiz = xmlLineas.getDocumentElement();
        NodeList nl = raiz.getElementsByTagName("linea");

        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            //extaemos valores del xml
            String id = e.getAttribute("id");
            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            String inicio = e.getElementsByTagName("inicio").item(0).getTextContent();
            String fin = e.getElementsByTagName("fin").item(0).getTextContent();
            String actividad = e.getElementsByTagName("actividad").item(0).getTextContent();
            int numeroParadas = Integer.parseInt(e.getElementsByTagName("numeroparadas").item(0).getTextContent());

            Linea linea = new Linea(Long.parseLong(id), nombre, inicio, fin, actividad, numeroParadas);
            linea.gardarLinea();

        }
    }

//Debería de ser público e recibir por parámetro unha linea
    private void gardarLinea() {
        ContentValues cv = new ContentValues();

        cv.put(columnas[0], this.id);
        cv.put(columnas[1], this.nombre);
        cv.put(columnas[2], this.inicio);
        cv.put(columnas[3], this.fin);
        cv.put(columnas[4], this.actividad);
        cv.put(columnas[5], this.numeroParadas);


        MainActivity.getDb().insert(taboa, null, cv);
    }

    public static Linea buscarPorId(long idLinea) {
        Linea linea = null;

        Cursor c = MainActivity.getDb().query(taboa, columnas, "_id=?", new String[]{String.valueOf(idLinea)}, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(0);
            String nombre = c.getString(1);
            String inicio = c.getString(2);
            String fin = c.getString(3);
            String actividad = c.getString(4);
            int numeroParadas = c.getInt(5);

            linea = new Linea(id,nombre,inicio,fin,actividad,numeroParadas);

        }

        return linea;
    }


    public static void borrarTodas() {
        MainActivity.getDb().delete(taboa, null, null);
    }

}
