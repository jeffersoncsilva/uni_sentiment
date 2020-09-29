package com.projetos.redes.models;

import java.io.Serializable;

public class Sentenca extends Data implements Serializable {
    public static final String TB_SENTENCA = "tb_sentenca";
    private String frase;
    private int peso;

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
