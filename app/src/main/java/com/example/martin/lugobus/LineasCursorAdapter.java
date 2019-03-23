package com.example.martin.lugobus;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LineasCursorAdapter extends SimpleCursorAdapter {

    String[] coloresLineasNumero = {"d8a785","f78383","f1adde","7bb5d2","60d7df","d8a785","f78383","f1adde","7bb5d2","60d7df","d8a785","f78383","f1adde","7bb5d2","60d7df"};
    String[]    coloresLineasInicioFin = {"dab9a2","f5adad","edd5e6","a7c1ce","b3d8db","dab9a2","f5adad","edd5e6","a7c1ce","b3d8db","dab9a2","f5adad","edd5e6","a7c1ce","b3d8db"};


    public LineasCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView tvItemLineaNumeroParada=(TextView)view.findViewById(R.id.tvItemLineaNumeroParada);
        LinearLayout llInicioFin=(LinearLayout) view.findViewById(R.id.llInicioFin);
        tvItemLineaNumeroParada.setBackgroundColor(Color.parseColor("#"+coloresLineasNumero[cursor.getPosition()]));
        llInicioFin.setBackgroundColor(Color.parseColor("#"+coloresLineasInicioFin[cursor.getPosition()]));

    }
}
