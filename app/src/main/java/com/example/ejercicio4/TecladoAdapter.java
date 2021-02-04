package com.example.ejercicio4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class TecladoAdapter extends BaseAdapter {

    private String[] letras;
    private LayoutInflater inflater;

    public TecladoAdapter(Context context){
        //Tamaño asociado a letras del alfabeto, no incluye Ñ
        letras = new String[26];
        //Llenado del arreglo con las letras correspondientes
        for(int i=0; i<letras.length; i++){
            letras[i] = "" + (char)(i + 'a');
        }
        //Infla la vista en el contexto dado
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //Especifica tamaño del adaptador
        return letras.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creacion de elementos de la vista
        TextView tvTecla = null;
        CardView tecla;

        if(convertView == null){
            //Creacion de la tecla
            tecla = (CardView) inflater.inflate(R.layout.tecla, parent, false);
        }else {
            tecla = (CardView) convertView;
        }

        //Asociacion del TextView
        tvTecla = tecla.findViewById(R.id.tvLetra);
        //Establece letra a mostrar
        tvTecla.setText(letras[position]);
        return tecla;
    }
}
