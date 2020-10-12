package com.projetos.redes.models;

import com.projetos.redes.enums.Sentimento;

import java.util.Date;

public class ResultadoFinal extends Data{
    public static String TB_RESULT_FINAL = "CREATE TABLE  IF NOT EXISTS tb_result_final (dt_inicio TEXT, dt_fim TEXT, bytes INTEGER, sentimento TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT);";
    private String dt_inicio, dt_fim;
    private long bytes;
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

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }

    public String toString(){
        return "DT_INICIO: " + dt_inicio + " | DT_FIM: " + dt_fim + " | Sentimento: " + sentimento.toString() + " | BYTES: "  + bytes;
    }
}
