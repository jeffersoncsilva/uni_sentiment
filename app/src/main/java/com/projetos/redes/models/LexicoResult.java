package com.projetos.redes.models;

import com.projetos.redes.enums.Sentimento;

import java.io.Serializable;

public class LexicoResult extends Data implements Serializable {
    public static final String TB_LEXICO_RESULT = "tb_lexico_result";
    public static final String _ID = "id";
    public static final String _DATA = "data";
    public static final String _FRASE = "frase";
    public static final String _SENTIMENTO = "sentimento";
    public static final String SQL_CREATE_TABLE_LR = "CREATE TABLE IF NOT EXISTS " + LexicoResult.TB_LEXICO_RESULT +
                                    " ( " + _DATA + " TEXT, " +
                                    _FRASE + " TEXT, " +
                                    _SENTIMENTO + " TEXT, " +
                                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";


    private String data;
    private String frase;
    private Sentimento sentimento;
    private int id;

    public String getSentimento() {
        return sentimento.toString();
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getSqlInsert(){
        return String.format(" INSERT INTO "+TB_LEXICO_RESULT + " (" + _DATA + ", " + _FRASE + ", "+ _SENTIMENTO + ") VALUES ( '"+getData()+"', '"+getFrase()+"', '"+getSentimento()+"');");
    }
}
