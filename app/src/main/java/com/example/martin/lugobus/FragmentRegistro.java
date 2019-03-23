package com.example.martin.lugobus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistro.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRegistro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistro extends Fragment implements TarefaDescargaXML.Cliente {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOGIN = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button bttnRegistro;
    private Button bttnLogin;
    private SharedPreferences sp;
    private String usuario;


    public FragmentRegistro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegistro.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegistro newInstance(String param1, String param2) {
        FragmentRegistro fragment = new FragmentRegistro();
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
        View view= inflater.inflate(R.layout.fragment_registro, container, false);

        sp = getActivity().getSharedPreferences("lugoBus", MODE_PRIVATE);

        bttnRegistro=(Button)view.findViewById(R.id.bttnRegistro);
        bttnLogin=(Button)view.findViewById(R.id.bttnLogin);

        bttnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                login();

            }
        });

        bttnRegistro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registro();

            }
        });

        return view;
    }

    private void registro() {

        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

        adb.setTitle("Registro");

        adb.setView(R.layout.dialog_registro);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog ad = (AlertDialog) dialog;

                EditText etUsuario = (EditText) ad.findViewById(R.id.etLoginUsuario);
                EditText etPassword = (EditText) ad.findViewById(R.id.etLoginPassword);

                String usuario = etUsuario.getText().toString();
                String password = etPassword.getText().toString();

                System.out.println(usuario + " " + password);


                TarefaDescargaXML tdx = new TarefaDescargaXML(FragmentRegistro.this, LOGIN);
                tdx.execute(Servizo.urlLogin(usuario, password));

                // Guardamos el usuario y contraseña para proximo uso
                sp.edit().putString("usuario", usuario)
                        .putString("password", password)
                        .commit();

            }
        });
        adb.setNegativeButton("Cancelar", null);

        adb.show();


    }


    public void login() {
        //Comprobamos si el usuario ya se Logeo previamente
        usuario = sp.getString("usuario", null);

        if (usuario != null) {
            System.out.println("++++++++++++++++++++++++++++DEBERIA ESTAR A NULL" + usuario);

            // Si ya se logeo usamos esos datos para loguearnos
            String password = sp.getString("password", null);

            // Co usuario e password que acabamos de recuperar, facemos login
            System.out.println(password + "    " + usuario);
            TarefaDescargaXML tdx = new TarefaDescargaXML(this, LOGIN);
            tdx.execute(Servizo.urlLogin(usuario, password));
        } else {
            //Si no los tenemos los pedimos mediante un AlertDialog


            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

            adb.setTitle("Login");

            adb.setView(R.layout.dialog_login);

            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog ad = (AlertDialog) dialog;

                    EditText etUsuario = (EditText) ad.findViewById(R.id.etLoginUsuario);
                    EditText etPassword = (EditText) ad.findViewById(R.id.etLoginPassword);

                    String usuario = etUsuario.getText().toString();
                    String password = etPassword.getText().toString();

                    System.out.println(usuario + " " + password);


                    TarefaDescargaXML tdx = new TarefaDescargaXML(FragmentRegistro.this, LOGIN);
                    tdx.execute(Servizo.urlLogin(usuario, password));

                    // Guardamos el usuario y contraseña para proximo uso
                    sp.edit().putString("usuario", usuario)
                            .putString("password", password)
                            .commit();

                }
            });
            adb.setNegativeButton("Cancelar", null);

            adb.show();
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

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if (resultado == null) {
            Toast.makeText(getContext(), "Problemas de conexion", Toast.LENGTH_LONG).show();
            return;
        }

        if (tipoDescarga == LOGIN) {
            String mensaxeResultado = resultado.getDocumentElement().getTextContent();
            Toast.makeText(getContext(), mensaxeResultado, Toast.LENGTH_LONG).show();

            if (mensaxeResultado.equals("Login incorrecto")) {
                // En caso de login Incorrecto quitamos de las Shared Preferences el usuario y contraseña
                sp.edit().remove("usuario").remove("password").commit();
                login();
            } else {

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
