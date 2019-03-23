package com.example.martin.lugobus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        FragmentFavoritas.OnFragmentInteractionListener, FragmentParadas.OnFragmentInteractionListener,
        FragmentLineas.OnFragmentInteractionListener, FragmentMapa.OnFragmentInteractionListener,FragmentRegistro.OnFragmentInteractionListener , FragmentDescripcionLinea.OnFragmentInteractionListener, TarefaDescargaXML.Cliente {

    private ImageView ivParadas;
    private ImageView ivFavoritas;
    private ImageView ivLineas;
    private ImageView ivMapa;
    private View FLContenedorFragments;
    private static SQLiteDatabase db;
    private SharedPreferences sp;
    private static final int LOGIN = 1;
    private String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //region MétodosQueCreaPorDefecto
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //endregion

        //Aquí comienzo a trabajar, lo de arriba lo crea por el tipo de Activity


        FragmentParadas fParadas = new FragmentParadas();
        getSupportFragmentManager().beginTransaction().add(R.id.FLContenedorFragments, fParadas);


        ivParadas = (ImageView) findViewById(R.id.ivParadas);
        ivFavoritas = (ImageView) findViewById(R.id.ivFavoritas);
        ivLineas = (ImageView) findViewById(R.id.ivLineas);
        ivMapa = (ImageView) findViewById(R.id.ivMapa);

        sp = getSharedPreferences("lugoBus", MODE_PRIVATE);

        db = new DBOpenHelper(this).getWritableDatabase();

        ivParadas.setOnClickListener(this);
        ivFavoritas.setOnClickListener(this);

        FLContenedorFragments = (View) findViewById(R.id.FLContenedorFragments);
        FragmentParadas fPrincipal = new FragmentParadas();
        getSupportFragmentManager().beginTransaction().add(R.id.FLContenedorFragments, fPrincipal).commit();


       // sp = getSharedPreferences("sp", MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.miLogin) {
            login();
        } else if (id == R.id.miNuevo) {
            //Esto es para registrar un nuevo usuario en caso de ser necesario en la Aplicacion
            // por ahora ponto un toast que diga "Crearemos un nuevo Usuario"
            Toast.makeText(this, "Crearemos un nuevo Usuario", Toast.LENGTH_LONG).show();
        } else if (id == R.id.miCerrarSesion) {
            sp.edit().remove("usuario").commit();
            sp.edit().remove("password").commit();
            usuario=null;
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch ((v.getId())) {
            case R.id.ivParadas:
                ivParadas.setImageResource(R.drawable.parada_verde);
                ivFavoritas.setImageResource(R.drawable.corazon_negro);
                ivLineas.setImageResource(R.drawable.lineas_negro);
                ivMapa.setImageResource(R.drawable.brujula_negro);

                FragmentParadas fParadas = new FragmentParadas();
                transaction.replace(R.id.FLContenedorFragments, fParadas);

                transaction.commit();
                break;

            case R.id.ivFavoritas:
                ivParadas.setImageResource(R.drawable.parada_negro);
                ivFavoritas.setImageResource(R.drawable.corazon_verde);
                ivLineas.setImageResource(R.drawable.lineas_negro);
                ivMapa.setImageResource(R.drawable.brujula_negro);

                usuario=sp.getString("usuario",null);
                System.out.println("usuario: "+usuario);


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(null, 0);

                if (usuario == null) {
                    FragmentRegistro fRegistro = new FragmentRegistro();
                    transaction.replace(R.id.FLContenedorFragments, fRegistro);
                    transaction.commit();

                } else {

                    FragmentFavoritas fFavoritas = new FragmentFavoritas();
                    transaction.replace(R.id.FLContenedorFragments, fFavoritas);
                    transaction.commit();
                }
                break;

            case R.id.ivLineas:
                ivParadas.setImageResource(R.drawable.parada_negro);
                ivFavoritas.setImageResource(R.drawable.corazon_negro);
                ivLineas.setImageResource(R.drawable.lineas_verde);
                ivMapa.setImageResource(R.drawable.brujula_negro);

                FragmentLineas fLineas = new FragmentLineas();
                transaction.replace(R.id.FLContenedorFragments, fLineas);
                transaction.commit();
                break;

            case R.id.ivMapa:
                ivParadas.setImageResource(R.drawable.parada_negro);
                ivFavoritas.setImageResource(R.drawable.corazon_negro);
                ivLineas.setImageResource(R.drawable.lineas_negro);
                ivMapa.setImageResource(R.drawable.brujul_verde);

                FragmentMapa fMapa = new FragmentMapa();
                transaction.replace(R.id.FLContenedorFragments, fMapa);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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


            AlertDialog.Builder adb = new AlertDialog.Builder(this);

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


                    TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, LOGIN);
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

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if (resultado == null) {
            Toast.makeText(this, "Problemas de conexion", Toast.LENGTH_LONG).show();
            return;
        }

        if (tipoDescarga == LOGIN) {
            String mensaxeResultado = resultado.getDocumentElement().getTextContent();
            Toast.makeText(this, mensaxeResultado, Toast.LENGTH_LONG).show();

            if (mensaxeResultado.equals("Login incorrecto")) {
                // En caso de login Incorrecto quitamos de las Shared Preferences el usuario y contraseña
                sp.edit().remove("usuario").remove("password").commit();
                login();
            } else {

            }
        }
    }


}
