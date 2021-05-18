package com.projetos.redes.modelos;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.utilidades.UtilidadeData;

import java.io.Serializable;

public class ResultadoLexicoProcessado implements Serializable {
    private String frase;
    private Sentimento sentimento;
    private UtilidadeData data;

    public ResultadoLexicoProcessado(String frase, Sentimento sent, UtilidadeData data) {
        this.frase = frase;
        this.sentimento = sent;
        this.data = data;
    }

    public UtilidadeData getData(){
        return this.data;
    }

    public String getFrase() {
        return frase;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public String toString(){
         return String.format("Frase: %s\nSentimento: %s \nData: %s", frase, sentimento.toString(), data.toString());
    }
}
