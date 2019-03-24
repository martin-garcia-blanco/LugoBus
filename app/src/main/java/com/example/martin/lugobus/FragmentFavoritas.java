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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFavoritas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFavoritas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFavoritas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView tvFavoritasParada;
    private TextView tvFavoritasLineas;
    private int PARADAS_LINEAS=0;
    private ListView lvFavoritas;

    public FragmentFavoritas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFavoritas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFavoritas newInstance(String param1, String param2) {
        FragmentFavoritas fragment = new FragmentFavoritas();
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
        View view= inflater.inflate(R.layout.fragment_favoritas, container, false);

        lvFavoritas=(ListView) view.findViewById(R.id.lvFavoritas);

        final Cursor c = Parada.paradasFavoritas();
        encherLvParadas(c);

        tvFavoritasParada=(TextView)view.findViewById(R.id.tvFavoritasParada);
        tvFavoritasLineas=(TextView)view.findViewById(R.id.tvFavoritasLineas);

        tvFavoritasParada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tvFavoritasParada.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvFavoritasLineas.setTextColor(0xff000000);
                tvFavoritasParada.setTextSize(30);
                tvFavoritasLineas.setTextSize(20);
                tvFavoritasParada.setTypeface(null, Typeface.BOLD);
                tvFavoritasLineas.setTypeface(null, Typeface.NORMAL);




                PARADAS_LINEAS=0;
                Cursor c = Parada.paradasFavoritas();

                encherLvParadas(c);
            }
        });


        tvFavoritasLineas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tvFavoritasLineas.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvFavoritasParada.setTextColor(0xff000000);
                tvFavoritasLineas.setTextSize(30);
                tvFavoritasParada.setTextSize(20);
                tvFavoritasParada.setTypeface(null, Typeface.NORMAL);
                tvFavoritasLineas.setTypeface(null, Typeface.BOLD);


                PARADAS_LINEAS=0;
                Cursor c = Linea.lineasFavoritas();
                encherLvParadas(c);
            }
        });


        return view;
    }


    private void encherLvParadas(Cursor c) {

        //Antes de encher o lvAlumnos co novo cursor, pechamos o cursor co que poidera estar xa cheo
        //if(lvParadas.getAdapter() != null && ((SimpleCursorAdapter) lvParadas.getAdapter()).getCursor() != null) {
        //  ((SimpleCursorAdapter) lvParadas.getAdapter()).getCursor().close();
        //}

        if (c!=null && c.getCount() > 0) {
            String[] from = new String[]{"_id", "nombre_parada", "lineasCoincidentes"};
            int[] to = new int[]{R.id.tvItemNumeroParada, R.id.tvItemNomeParada,R.id.tvItemParadasCoincidentes};
            //System.out.println("********************************************************");
            // System.out.println((c.getString(c.getColumnIndex("lineasCoincidentes"))).toString());
            SimpleCursorAdapter aca = new SimpleCursorAdapter(getContext(), R.layout.item_lv_paradas, c, from, to, 0);
            lvFavoritas.setAdapter(aca);
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
