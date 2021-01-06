package com.projetos.redes.modelos;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.utilidades.Data;

public class ResultadoFinalLexico {
    public static String TB_RESULT_FINAL = "CREATE TABLE  IF NOT EXISTS tb_result_final (dt_inicio TEXT, dt_fim TEXT, bytes INTEGER, sentimento TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT);";

    private Data inicio, fim;
    private UsoDeInternet usoDeInternet;
    private Sentimento sentimento;

    public ResultadoFinalLexico(Data inicio, Data fim, UsoDeInternet consumo, Sentimento s){
        this.inicio = inicio;
        this.fim = fim;
        this.usoDeInternet = consumo;
        this.sentimento = s;
    }

    public Data getInicio() {
        return inicio;
    }

    public Data getFim() {
        return fim;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public UsoDeInternet getUsoDeInternet(){
        return this.usoDeInternet;
    }

    public String toString(){
        return inicio.toString() + "::" + fim.toString() + " :: " + usoDeInternet.toString() +" :: " + sentimento.toString();
    }
}
