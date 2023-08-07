package com.example.inclusao.Screens;

public class Curso {
    private String titulo;
    private String link;
    private String userID;

    // Construtor vazio necessário para o Firebase Firestore
    public Curso() {}

    public Curso(String titulo, String link, String userID) {
        this.titulo = titulo;
        this.link = link;
        this.userID = userID;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getLink() {
        return link;
    }

    public String getUserID() {
        return userID;
    }
}

