package com.projetos.redes.modelos;

import com.projetos.redes.Utils;
import com.projetos.redes.utilidades.UtilidadeData;

public class ConsumoInternet {

    private long wifi, mobile, data_inicio, data_fim;

    public ConsumoInternet(long wifi, long mobile, long inicio, long fim) {
        this.wifi = wifi;
        this.mobile = mobile;
        this.data_inicio = inicio;
        this.data_fim = fim;
    }

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

    public long getDataInicio() {
        return data_inicio;
    }

    public long getDataFim() {
        return data_fim;
    }

    public long getTotal(){
        return wifi + mobile;
    }

    public String toString(){
        return String.format("Wi-Fi: %s\nMobile: %s\nInicio: %s\nFim: %s",
                Utils.converterParaMegabytes(wifi), Utils.converterParaMegabytes(mobile),
                UtilidadeData.toDateString(data_inicio), UtilidadeData.toDateString(data_fim));
    }

}
