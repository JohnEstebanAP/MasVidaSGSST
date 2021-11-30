package com.johnestebanap.myapplication.db;

import androidx.annotation.NonNull;

public class Document {
    private int id;
    private String tipoDoc;
    private String description;
    private String url;
    private String estado;
    private String date;

    public Document() {
    }

    public Document(String tipoDoc, String description, String url, String estado) {
        //this.id = id;
        this.tipoDoc = tipoDoc;
        this.description = description;
        this.url = url;
        this.estado = estado;
        //this.date = date;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", tipoDoc='" + tipoDoc + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", estado='" + estado + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

