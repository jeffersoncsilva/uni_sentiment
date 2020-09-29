package com.projetos.redes.models;

import java.io.Serializable;

public class LexicoUnificado extends Data implements Serializable {
    public static final String TB_LEXICO_UNIFICADO = "tb_lexico_unificado";
    private String sentenca;
    private int peso;

    public String getSentenca() {
        return sentenca;
    }

    public void setSentenca(String sentenca) {
        this.sentenca = sentenca;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
