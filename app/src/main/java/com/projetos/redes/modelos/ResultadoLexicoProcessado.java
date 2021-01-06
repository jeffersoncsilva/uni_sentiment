package com.projetos.redes.modelos;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.utilidades.Data;

import java.io.Serializable;
import java.util.Date;

public class ResultadoLexicoProcessado implements Serializable {
    public static final String TB_LEXICO_RESULT = "tb_lexico_result";
    public static final String _ID = "id";
    public static final String _DATA = "data";
    public static final String _FRASE = "frase";
    public static final String _SENTIMENTO = "sentimento";
    public static final String SQL_CREATE_TABLE_LR = "CREATE TABLE IF NOT EXISTS " + ResultadoLexicoProcessado.TB_LEXICO_RESULT +
                                    " ( " + _DATA + " TEXT, " +
                                    _FRASE + " TEXT, " +
                                    _SENTIMENTO + " TEXT, " +
                                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";
    private Data data;
    private String frase;
    private Sentimento sentimento;

    public ResultadoLexicoProcessado(Data d, String frase, Sentimento s){
        this.data = d;
        this.frase = frase;
        this.sentimento = s;
    }

    public Data getDate(){
        return data;
    }

    public String getSentimento() {
        return sentimento.toString();
    }

    public String getFrase() {
        return frase;
    }

    public String getSqlInsert(){
        return String.format(" INSERT INTO "+TB_LEXICO_RESULT + " (" + _DATA + ", " + _FRASE + ", "+ _SENTIMENTO + ") VALUES ( '"+data.toString()+"', '"+getFrase()+"', '"+getSentimento()+"');");
    }

    public String toString(){
         return data + " :: " + frase + " :: " + sentimento.toString();
    }
}
