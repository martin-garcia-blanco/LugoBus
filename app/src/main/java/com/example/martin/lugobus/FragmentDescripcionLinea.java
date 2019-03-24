package com.example.martin.lugobus;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDescripcionLinea.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentDescripcionLinea extends Fragment implements TarefaDescargaXML.Cliente {

    private static final int DESCARGA_PARADAS = 1;
    private static int ORDEN_INICIO_FIN = 0;

    private OnFragmentInteractionListener mListener;
    private Parada[] paradas;
    private SharedPreferences sp;
    private ListView lv_linea_paradas;
    private TextView tv_descripcion_numero_linea;
    private TextView tv_descripcion_inicio;
    private TextView tv_descripcion_fin;
    private TextView tv_guion;
    private TextView tv_descripcion_boton_ida;
    private TextView tv_descripcion_boton_vuelta;
    private Parada paradaFavorita;


    public FragmentDescripcionLinea() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion_linea, container, false);

        sp = getActivity().getSharedPreferences("lugoBus", MODE_PRIVATE);

        lv_linea_paradas = (ListView) view.findViewById(R.id.lv_linea_paradas);

        tv_descripcion_numero_linea=(TextView)view.findViewById(R.id.tv_descripcion_numero_linea);
        tv_descripcion_inicio=(TextView)view.findViewById(R.id.tv_descripcion_Inicio);
        tv_guion=(TextView)view.findViewById(R.id.tv_guion);
        tv_descripcion_fin=(TextView)view.findViewById(R.id.tv_descripcion_Fin);



        Linea linea=Linea.buscarPorId(sp.getLong("lineaClick",-1));

        tv_descripcion_numero_linea.setText(linea.getNombre());
        tv_descripcion_inicio.setText(linea.getInicio());
        tv_descripcion_fin.setText(linea.getFin());


        lv_linea_paradas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                System.out.println("ID: "+id);
                paradaFavorita=Parada.buscarParadaFavoritaId(id);

                if((paradaFavorita)!=null){
                    System.out.println("chego neno");
                    Toast.makeText(getContext(),"Parada ya agregada a favoritas",Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("Entra a gardar");
                    paradaFavorita= (Parada) lv_linea_paradas.getItemAtPosition(position);
                    paradaFavorita.gardarParadaFavorita();
                }

                return true;
            }
        });

        tv_descripcion_boton_ida=(TextView)view.findViewById(R.id.tv_descripcion_boton_ida);
        tv_descripcion_boton_vuelta=(TextView)view.findViewById(R.id.tv_descripcion_boton_vuelta);

        tv_descripcion_boton_ida.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tv_descripcion_boton_ida.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_descripcion_boton_vuelta.setTextColor(0xff000000);
                tv_descripcion_boton_ida.setTextSize(30);
                tv_descripcion_boton_vuelta.setTextSize(20);
                tv_descripcion_boton_ida.setTypeface(null, Typeface.BOLD);
                tv_descripcion_boton_vuelta.setTypeface(null, Typeface.NORMAL);




                ORDEN_INICIO_FIN=0;
                encherLvParadas(0);
            }
        });


        tv_descripcion_boton_vuelta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tv_descripcion_boton_vuelta.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_descripcion_boton_ida.setTextColor(0xff000000);
                tv_descripcion_boton_vuelta.setTextSize(30);
                tv_descripcion_boton_ida.setTextSize(20);
                tv_descripcion_boton_ida.setTypeface(null, Typeface.NORMAL);
                tv_descripcion_boton_vuelta.setTypeface(null, Typeface.BOLD);


                ORDEN_INICIO_FIN=0;
                encherLvParadas(1);
            }
        });


        descargaParadas();


        return view;
    }

    private void descargaParadas() {
        TarefaDescargaXML tdx = new TarefaDescargaXML( this, DESCARGA_PARADAS);
        tdx.execute(Servizo.urlDescargaParadasLinea(sp.getLong("lineaClick",0)));
    }

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if (resultado == null) {
            Toast.makeText(getActivity(), "Problemas de conexion", LENGTH_LONG).show();
            return;
        } else {
            crearParadas(resultado);
            encherLvParadas(0);
            return;
        }

    }

    private void encherLvParadas(int orden) {
        if(orden==0) {
            ArrayAdapterParada aap = new ArrayAdapterParada(getContext(), paradas);
            lv_linea_paradas.setAdapter(aap);
        }
        else if(orden==1){
            int num=0;
            Parada[] paradasInverso= new Parada[paradas.length];
            int maximo = paradas.length;

            for (int i = 0; i<paradas.length; i++) {
                paradasInverso[maximo - 1] = paradas[i];
                maximo--;
            }
            ArrayAdapterParada aap = new ArrayAdapterParada(getContext(), paradasInverso);
            lv_linea_paradas.setAdapter(aap);
        }

    }

    private void crearParadas(Document resultado) {

        Element raiz = resultado.getDocumentElement();
        NodeList nl = raiz.getElementsByTagName("parada");
        paradas = new Parada[nl.getLength()];
        for (int i = 0; i < nl.getLength(); i++) {

            Element e = (Element) nl.item(i);
            //extaemos valores del xml
            String id = e.getAttribute("id");
            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            String calle = e.getElementsByTagName("calle").item(0).getTextContent();
            String lineas= e.getElementsByTagName("lineas").item(0).getTextContent();



            Parada parada = new Parada(Long.parseLong(id), nombre, calle,lineas);
            paradas[i] = parada;
        }
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
