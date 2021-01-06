package com.projetos.redes.modelos;

import com.projetos.redes.utilidades.Data;

import java.io.Serializable;
import java.util.Date;

public class UsoDeInternet implements Serializable {
    public static final String TB_NET_USAGE = "tb_net_usage";
    public static final String COLUMN_DT_INICIO = "dt_inicio";
    public static final String COLUMN_DT_FIM = "dt_fim";
    public static final String COLUMN_BYTES_WIFI = "bytes_wifi";
    public static final String COLUMN_BYTES_MOBILE = "bytes_mobile";
    public static final String COLUMN_ID = "id";

    public static final String SQL_TB_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)",
            TB_NET_USAGE, COLUMN_ID, COLUMN_DT_INICIO,COLUMN_DT_FIM, COLUMN_BYTES_MOBILE, COLUMN_BYTES_WIFI);
    public static final String SQL_SELECT = "SELECT dt_inicio, dt_fim, bytes_wifi, bytes_mobile FROM tb_net_usage;";



    private long id;
    private Consumo consumo;
    private Data inicio, fim;

    public UsoDeInternet(Consumo consumo, Data inicio, Data fim){
        this.consumo = consumo;
        this.inicio = inicio;
        this.fim = fim;
    }

    public long getId() {
        return id;
    }

    public Consumo getConsumo() { return this.consumo; }

    public Data getInicio() { return this.inicio; }

    public Data getFim(){ return this.fim; }


    public static class Consumo{
        private long wifi, mobile;

        public Consumo(long b, long m){
            wifi = b;
            mobile = m;
        }

        public long getWifi(){ return wifi; }

        public long getMobile() { return mobile; }
    }
}
