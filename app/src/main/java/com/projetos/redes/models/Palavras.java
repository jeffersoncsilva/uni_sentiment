package com.projetos.redes.models;

import java.io.Serializable;

public class Palavras extends Data implements Serializable {
    public static final String TB_PALAVRAS = "tb_palavras";
    private String palavra;
    private int peso;

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
