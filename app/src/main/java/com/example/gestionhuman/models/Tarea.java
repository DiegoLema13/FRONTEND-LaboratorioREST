package com.example.gestionhuman.models;

import java.io.Serializable;

public class Tarea implements Serializable {

    private Long id;
    private String titulo;

    // Constructor vacío
    public Tarea() {}

    // Constructor completo
    public Tarea(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
