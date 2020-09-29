package com.projetos.redes.models;

import com.projetos.redes.enums.Sentimento;

public class LexicoClassificado {
    private String dt_inicio;
    private String dt_fim;
    private Sentimento sentimento;

    public String getDt_inicio() {
        return dt_inicio;
    }

    public void setDt_inicio(String dt_inicio) {
        this.dt_inicio = dt_inicio;
    }

    public String getDt_fim() {
        return dt_fim;
    }

    public void setDt_fim(String dt_fim) {
        this.dt_fim = dt_fim;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }
}
