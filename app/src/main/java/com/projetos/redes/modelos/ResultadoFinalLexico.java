package com.projetos.redes.modelos;

import android.provider.Telephony;

import com.projetos.redes.enums.Sentimento;

public class ResultadoFinalLexico {
    private String data,  hora;
    private Sentimento sentimento;
    private int minuto;
    private long usoRede;

    public ResultadoFinalLexico(String data, int hora, int sentimento, long usoRede, int minuto) {
        this.data = data;
        this.hora = formataHora(hora);
        this.sentimento = (sentimento==1 ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
        this.usoRede = usoRede;
        this.minuto = minuto;
    }

    public ResultadoFinalLexico(String data, int hora, long usoRede, int minuto) {
        this.data = data;
        this.hora = formataHora(hora);
        this.minuto = minuto;
        this.usoRede = usoRede;
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public long getUsoRede() {
        return usoRede;
    }

    public int getMinuto() {
        return minuto;
    }

    public String toString(){
        return data+";"+hora+";"+usoRede+";"+sentimento+";"+minuto;
    }

    private String formataHora(int hora){
        if(hora < 10)
            return "0"+hora+":00";
        else
            return hora+":00";
    }

    public String resultadoParaEnviar(){
        return "Resultado";
    }
}
