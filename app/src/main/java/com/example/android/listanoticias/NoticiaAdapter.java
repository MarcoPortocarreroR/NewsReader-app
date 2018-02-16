package com.example.android.listanoticias;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Usuario on 10/12/2017.
 */

class NoticiaAdapter extends ArrayAdapter<Noticia>{
    Context context;

    public NoticiaAdapter(@NonNull Context context, ArrayList<Noticia> noticias) {
        super(context, R.layout.fila_noticia, noticias);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Noticia noticia = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_noticia, parent, false);
        }

        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView fuente = (TextView) convertView.findViewById(R.id.fuente);
        TextView autor  = (TextView) convertView.findViewById(R.id.autor);

        titulo.setText(noticia.getTitulo());
        fuente.setText(noticia.getNombre());
        autor.setText(noticia.getAutor());

        return convertView;
    }
}
