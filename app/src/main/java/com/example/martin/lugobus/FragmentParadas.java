package com.example.martin.lugobus;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentParadas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentParadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentParadas extends Fragment implements TarefaDescargaXML.Cliente {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int DESCARGA_PARADA_ID = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText etBusquedaParada;
    private ListView lvParadas;

    public FragmentParadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentParadas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentParadas newInstance(String param1, String param2) {
        FragmentParadas fragment = new FragmentParadas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paradas, container, false);

        etBusquedaParada = (EditText) view.findViewById(R.id.etBusquedaParada);

        TextView btBuscar = (TextView) view.findViewById(R.id.btBuscar);

        lvParadas = (ListView) view.findViewById(R.id.lvParadas);

        Cursor c = Parada.paradasRecientes();
        encherLvParadas(c);

        btBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etBusquedaParada.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Escriba número de parada", Toast.LENGTH_SHORT).show();
                } else {
                    buscaParada();
                }
            }
        });

        return view;
    }

    private void encherLvParadas(Cursor c) {

        //Antes de encher o lvAlumnos co novo cursor, pechamos o cursor co que poidera estar xa cheo
        //if(lvParadas.getAdapter() != null && ((SimpleCursorAdapter) lvParadas.getAdapter()).getCursor() != null) {
        //  ((SimpleCursorAdapter) lvParadas.getAdapter()).getCursor().close();
        //}

        if (c.getCount() > 0) {
            String[] from = new String[]{"_id", "nombre_parada", "lineasCoincidentes"};
            int[] to = new int[]{R.id.tvItemNumeroParada, R.id.tvItemNomeParada,R.id.tvItemParadasCoincidentes};
            //System.out.println("********************************************************");
           // System.out.println((c.getString(c.getColumnIndex("lineasCoincidentes"))).toString());
            SimpleCursorAdapter aca = new SimpleCursorAdapter(getContext(), R.layout.item_lv_paradas, c, from, to, 0);
            lvParadas.setAdapter(aca);
        }

    }

    private void buscaParada() {

        //Comprobo se existe a parada, para non volvela a descargar
        Parada parada = null;
        String texto = etBusquedaParada.getText().toString();
        if ((parada = Parada.buscarParadaRecientePorId(Long.parseLong(texto))) != null) {
            Toast.makeText(getContext(), "Parada xa descargada", Toast.LENGTH_SHORT).show();
        } else {
            TarefaDescargaXML tdx = new TarefaDescargaXML(this, DESCARGA_PARADA_ID);
            tdx.execute(Servizo.urlDescargaParadaId(Long.parseLong(String.valueOf(etBusquedaParada.getText()))));
        }
        etBusquedaParada.setText("");
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

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if (resultado == null) {
            Toast.makeText(getContext(), "Erro na busqueda", Toast.LENGTH_LONG).show();
        } else {

            Element raiz = resultado.getDocumentElement();
            if((raiz.getAttribute("debug")).equals("0")){
                Toast.makeText(getContext(),"No existe la parada",Toast.LENGTH_SHORT).show();

            } else {

                String id = raiz.getElementsByTagName("id").item(0).getTextContent();
                String nombre = raiz.getElementsByTagName("nombre").item(0).getTextContent();
                String calle = raiz.getElementsByTagName("calle").item(0).getTextContent();
                String lineas= raiz.getElementsByTagName("lineas").item(0).getTextContent();

                Parada parada = new Parada(Long.parseLong(id), nombre, calle,lineas);
                parada.gardarParadaReciente();

                Cursor c = Parada.paradasRecientes();
                encherLvParadas(c);
            }
        }

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
