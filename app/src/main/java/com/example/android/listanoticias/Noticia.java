package com.example.android.listanoticias;

import java.io.Serializable;

/**
 * Created by Usuario on 10/12/2017.
 */

public class Noticia implements Serializable{

    private String id;
    private String nombre;
    private String autor;
    private String titulo;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
