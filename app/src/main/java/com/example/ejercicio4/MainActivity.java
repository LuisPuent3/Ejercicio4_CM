package com.example.ejercicio4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

    private TecladoAdapter adapter;
    GridView gvTeclado;
    ImageView ivAhorcado;
    TextView tvRacha;
    TextView tvCategoria;
    TextView tvPalabra;
    ProgressBar pbConexion;
    Ahorcado ahorcado;
    MediaPlayer ganar, perder;

    RequestQueue queue;
    JsonObjectRequest request;

    String letra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Objetos de Audio
        ganar = MediaPlayer.create(this, R.raw.excellent);
        perder = MediaPlayer.create(this, R.raw.zim_laugh);

        //Asociacion de elementos de la vista
        gvTeclado = findViewById(R.id.gvTeclado);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvPalabra = findViewById(R.id.tvPalabra);
        tvRacha = findViewById(R.id.tvRacha);
        ivAhorcado = findViewById(R.id.ivAhorcado);
        pbConexion = findViewById(R.id.pbConexion);

        //Invoca al metodo para realizar conexion
        conexion();
    }

    //Metodo para solicitar conexion al servidor
    public void conexion(){
        //Muestra ProgressBar de conexion
        pbConexion.setVisibility(View.VISIBLE);
        
        //Generacion de la cola de conexiones
        queue = Volley.newRequestQueue(this);
        //Generacion de la solicitud, guarda los datos de la respuesta
        request = new JsonObjectRequest(Request.Method.GET, getResources().getString(R.string.url), null, this, this);
        //Realiza la peticion de conexión
        queue.add(request);
    }

    public void jugar(){
        //Creacion del adaptador
        adapter = new TecladoAdapter(this);
        //Asocia el adaptador al GridView
        gvTeclado.setAdapter(adapter);
        //Establece valores de los TextView
        tvCategoria.setText(ahorcado.getCategoria());
        tvPalabra.setText(ahorcado.getPalabra());
        tvRacha.setText(String.valueOf(Ahorcado.getVictorias()));
        actualizarHorca();
    }

    public void actualizarHorca(){
        switch (ahorcado.getErrores()){
            case 1:
                ivAhorcado.setImageResource(R.drawable.ahorcado_1);
                break;
            case 2:
                ivAhorcado.setImageResource(R.drawable.ahorcado_2);
                break;
            case 3:
                ivAhorcado.setImageResource(R.drawable.ahorcado_3);
                break;
            case 4:
                ivAhorcado.setImageResource(R.drawable.ahorcado_4);
                break;
            case 5:
                ivAhorcado.setImageResource(R.drawable.ahorcado_5);
                break;
            case 6:
                ivAhorcado.setImageResource(R.drawable.ahorcado_fin);
                break;
            default:
                ivAhorcado.setImageResource(R.drawable.ahorcado_0);
                break;
        }
    }

    //Metodo para capturar clic sobre letras
    public void teclear(View view){
        //Asocia elementos de la vista seleccionada
        TextView tvLetra = view.findViewById(R.id.tvLetra);
        CardView cvTecla = view.findViewById(R.id.cvTecla);

        //Desactiva tecla
        cvTecla.setBackgroundColor(getResources().getColor(R.color.secondaryTextColor));
        cvTecla.setClickable(false);

        //Recupera la letra presionada
        letra = tvLetra.getText().toString();

        //Valida la letra presionada
        if(ahorcado.validar(letra.charAt(0))){
            //Actualiza palabra con letra encontrada
            tvPalabra.setText(ahorcado.getPalabra());
            //Valida si se ha ganado la partida
            if(ahorcado.victoria()){
                //Reproduce sonido
                ganar.start();
                //Partida ganada, muestra mensaje
                partidaGanada();
            }
        } else {
            //Actualiza visualizacion de imagen ahorcado
            actualizarHorca();
            //Valida si se ha perdido la partida
            if(ahorcado.derrota()){
                //Reproduce sonido
                perder.start();
                //Partida perdida, muestra mensaje
                partidaPerdida();
            }
        }
    }

    //Metodo para mostrar mensaje de victoria
    public void partidaGanada(){
        new AlertDialog.Builder(this, R.style.DialogoPersonalizado)
                .setTitle(getResources().getString(R.string.victoria))
                .setMessage(getResources().getString(R.string.msjVictoria))
                .setPositiveButton(getResources().getString(R.string.btnPartida), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Inicia una nueva partida
                        conexion();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btnSalir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Finaliza el Activity
                        finish();
                    }
                }).create().show();
    }

    //Metodo para mostrar mensaje de derrota
    public void partidaPerdida(){
        //Creacion de un nuevo dialogo
        new AlertDialog.Builder(this, R.style.DialogoPersonalizado)
                .setTitle(getResources().getString(R.string.derrota))
                .setMessage(getResources().getString(R.string.msjDerrota) + ahorcado.getPalabraSecreta())
                .setPositiveButton(getResources().getString(R.string.btnPartida), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Crea una nueva partida
                        conexion();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btnSalir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Finaliza el Activity
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //Creacion de un nuevo dialogo
        new AlertDialog.Builder(this, R.style.DialogoPersonalizado)
                .setTitle(getResources().getString(R.string.error))
                .setMessage(getResources().getString(R.string.errorConexion))
                .setPositiveButton(getResources().getString(R.string.btnReintentar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intenta nuevamente conexion al servidor
                        conexion();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btnSalir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Finaliza la aplicacion
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onResponse(JSONObject response) {
        //Quita visibilidad de el ProgressBar
        pbConexion.setVisibility(View.GONE);

        JSONObject respuesta = response;
        try {
            //Recupera elementos de la respuesta JSON recibida
            String palabra = respuesta.getString(getResources().getString(R.string.rqPalabra));
            String categoria = respuesta.getString(getResources().getString(R.string.rqCategoria));
            //Crea nuevo objeto de partida
            ahorcado = new Ahorcado(palabra, categoria);
            //Inicia la vista con datos de una nueva partida
            jugar();
        } catch (JSONException e) {
            //Creacion de un nuevo dialogo
            new AlertDialog.Builder(this, R.style.DialogoPersonalizado)
                    .setTitle(getResources().getString(R.string.error))
                    .setMessage(getResources().getString(R.string.errorProcesamiento))
                    .setPositiveButton(getResources().getString(R.string.btnReintentar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intenta nuevamente conexion al servidor
                            conexion();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.btnSalir), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Finaliza la aplicacion
                            finish();
                        }
                    }).create().show();
            e.printStackTrace();
        }
    }
}