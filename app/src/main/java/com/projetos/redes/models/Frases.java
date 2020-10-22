package com.projetos.redes.models;

import java.io.Serializable;

public class Frases extends Data implements Serializable {
    public static final String TB_FRASES = "tb_frases";
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
