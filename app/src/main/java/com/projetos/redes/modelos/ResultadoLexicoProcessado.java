package com.projetos.redes.modelos;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.utilidades.UtilidadeData;

import java.io.Serializable;

public class ResultadoLexicoProcessado implements Serializable {
    public static final String TB_LEXICO_RESULT = "tb_lexico_result";

    private String frase, dia;
    private Sentimento sentimento;
    private int hora, minuto;

    public ResultadoLexicoProcessado(String frase, int iSentimento, int hora, int minuto, String dia) {
        this.frase = frase;
        this.sentimento = (iSentimento == 2 ? Sentimento.NEGATIVO : Sentimento.POSITIVO);
        this.hora = hora;
        this.minuto = minuto;
        this.dia = dia;
    }

    public String getDia(){
        return dia;
    }

    public String getFrase() {
        return frase;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public String toString(){
         return String.format("Frase: %s - Sentimento: %s - Hora: %d - Minuto: %d - Dia: %s", frase, sentimento, hora, minuto, dia);
    }
}
