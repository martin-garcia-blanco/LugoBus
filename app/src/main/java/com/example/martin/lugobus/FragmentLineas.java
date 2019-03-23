package com.example.martin.lugobus;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;


//Descripción de Fragmento

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLineas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLineas#newInstance} factory method to
 * create an instance of this fragment.
 */


public class FragmentLineas extends Fragment implements TarefaDescargaXML.Cliente, FragmentDescripcionLinea.OnFragmentInteractionListener {
    //region parametrosDelFragment
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sp;

    //endregion

    private static final int DESCARGA_LINEAS = 0;


    private OnFragmentInteractionListener mListener;
    private ListView lvLineas;
    private ImageView ivLineas;


    //Constructor, quiere que esté vacío
    public FragmentLineas() {
        // Required empty public constructor
    }


    //Explicación de como crear más instancias de este fragment

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLineas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLineas newInstance(String param1, String param2) {
        FragmentLineas fragment = new FragmentLineas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lineas, container, false);

        lvLineas = (ListView) view.findViewById(R.id.lvLineas);
        Cursor c = Linea.getAllCursor();

        descargaLineas();

        encherLvLineas(c);

        lvLineas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Pásolle o id para facer consulta de que paradas traer.
                sp.edit().putLong("lineaClick",id).commit();


                //CHAMADA A UN NOVO FRAGMENTO DESCRIPCION LINEA
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentDescripcionLinea fDescripcionLineas = new FragmentDescripcionLinea();
                transaction.replace(R.id.FLContenedorFragments, fDescripcionLineas);
                transaction.commit();


            }
        });


         ivLineas=(ImageView)view.findViewById(R.id.ivLineas);

        //TODO problema co cambio de botón preguntarlle a Quique


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sp = getActivity().getSharedPreferences("lugoBus", MODE_PRIVATE);

    }


    private void descargaLineas() {

        TarefaDescargaXML tdx = new TarefaDescargaXML(this, DESCARGA_LINEAS);
        tdx.execute(Servizo.urlDescargaLineas());
        Cursor c = Linea.getAllCursor();
        encherLvLineas(c);
        System.out.println("chego");

    }


    private void encherLvLineas(Cursor c) {
        String[] from = new String[]{"nombre", "inicio", "fin"};
        int[] to = new int[]{R.id.tvItemLineaNumeroParada, R.id.tvItemLineaInicio, R.id.tvItemLineaFin};


        LineasCursorAdapter lca = new LineasCursorAdapter(getContext(), R.layout.item_lv_lineas, c, from, to, 0);
        lvLineas.setAdapter(lca);
        //todo esto é a animación ainda non sei si o final se usará solo hei que descomentar para que funcione
        //lvLineas.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation));

    }


    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if (resultado == null) {
            Toast.makeText(getActivity(), "Problemas de conexion", LENGTH_LONG).show();
            return;
        } else {
            Linea.crearLineaDendeXML(resultado);
            //Toast.makeText(getActivity(), "Lineas actualizadas", LENGTH_LONG).show();
            Cursor c = Linea.getAllCursor();
            encherLvLineas(c);
            return;
        }
    }


    //Metodos que no utilizo onCreateView, onButtonPressed, onAttach, onDetach
    //region MetodosQueNoUso

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
    public void onFragmentInteraction(Uri uri) {

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

    //endregion
}
