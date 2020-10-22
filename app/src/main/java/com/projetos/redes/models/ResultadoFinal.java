package com.projetos.redes.models;

import com.projetos.redes.Utils;
import com.projetos.redes.enums.Sentimento;

import java.util.Date;

public class ResultadoFinal extends Data{
    public static String TB_RESULT_FINAL = "CREATE TABLE  IF NOT EXISTS tb_result_final (dt_inicio TEXT, dt_fim TEXT, bytes INTEGER, sentimento TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT);";
    private String dtInicio, dtFim;
    public String final_res;
    private long wifi, mobile;
    private Sentimento sentimento;

    public void setWifi(long wifi) {
        this.wifi = wifi;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public long getWifi() {
        return wifi;
    }

    public long getMobile() {
        return mobile;
    }

    public ResultadoFinal(){
        this.dtFim = "00:00:0000:00:00:00";
        this.dtInicio = "00:00:0000:00:00:00";
        this.wifi = 0;
        this.mobile = 0;
        this.sentimento = Sentimento.POSITIVO;
    }

    public ResultadoFinal(long dtInicio, long dt_fim, long wifi, long mobile, Sentimento s){
        this.dtInicio = Utils.getDateFormatter().format(new Date(dtInicio));
        this.dtFim = Utils.getDateFormatter().format(new Date(dt_fim));
        this.wifi = wifi;
        this.mobile = mobile;
        this.sentimento = s;
    }

    public String getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(String dtInicio) {
        this.dtInicio = dtInicio;
    }

    public String getDtFim() {
        return dtFim;
    }

    public void setDtFim(String dtFim) {
        this.dtFim = dtFim;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }

    public long getTotalBytes(){
        return wifi + mobile;
    }

    public String toString(){
        return "DT_INICIO: " + dtInicio + " | DT_FIM: " + dtFim + " | Sentimento: " + sentimento.toString();
    }
}
