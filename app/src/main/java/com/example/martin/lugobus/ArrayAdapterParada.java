package com.example.martin.lugobus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ArrayAdapterParada extends ArrayAdapter {
    private static final int DESCARGA_LINEAS_COINCIDENTES = 0;
    private final int resource;
    private final Parada[] paradas;
    private String[] lineasCoincidentes;
    private LinearLayout llLineasCoincidentes;


    public ArrayAdapterParada(Context context, Parada[] paradas) {
        super(context, R.layout.item_lv_descripcion,paradas);
        this.resource = R.layout.item_lv_descripcion;
        this.paradas=paradas;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View fila=inflater.inflate(resource,null);

        TextView tvItemNumeroParada=(TextView)fila.findViewById(R.id.tvItemNumeroParada);
        TextView tvItemNomeParada=(TextView)fila.findViewById(R.id.tvItemNomeParada);

        llLineasCoincidentes=(LinearLayout)fila.findViewById(R.id.llLineasCoincidentes);

        tvItemNumeroParada.setText(String.valueOf(paradas[position].getId()));
        tvItemNomeParada.setText(paradas[position].getNombre());

        if(paradas[position].getLineasCoincidentes()!=null){
            TextView tvLineaCoincidente = new TextView(getContext());

            tvLineaCoincidente.setText(paradas[position].getLineasCoincidentes());
            tvLineaCoincidente.setBackgroundColor(tvLineaCoincidente.getResources().getColor(R.color.colorAccent));
            tvLineaCoincidente.setWidth(50);
            tvLineaCoincidente.setHeight(50);
            llLineasCoincidentes.addView(tvLineaCoincidente);
        }

        if(llLineasCoincidentes!=null){
            System.out.println("Ves como non era null parguela");
        }

        return fila;
    }







}
