package com.projetos.redes.models;

import java.io.Serializable;

public class NetworkUsage extends Data implements Serializable {
    public static final String TB_NET_USAGE = "tb_net_usage";
    public static final String COLUMN_DT_INICIO = "dt_inicio";
    public static final String COLUMN_DT_FIM = "dt_fim";
    public static final String COLUMN_BYTES_WIFI = "bytes_wifi";
    public static final String COLUMN_BYTES_MOBILE = "bytes_mobile";
    public static final String COLUMN_ID = "id";

    public static final String SQL_TB_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)",
            TB_NET_USAGE, COLUMN_ID, COLUMN_DT_INICIO,COLUMN_DT_FIM, COLUMN_BYTES_MOBILE, COLUMN_BYTES_WIFI);
    public static final String SQL_SELECT = "SELECT dt_inicio, dt_fim, bytes_wifi, bytes_mobile FROM tb_net_usage;";
    private long time_stamp;
    private long bytes_wifi;
    private long bytes_mobile;

    private int id;

    private String dt_inicio, dt_fim, tempo;

    public long getTotal() {
        return bytes_mobile + bytes_wifi;
    }

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

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public long getBytes_mobile() {
        return bytes_mobile;
    }

    public void setBytes_mobile(long bytes_mobile) {
        this.bytes_mobile = bytes_mobile;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getBytes_wifi() {
        return bytes_wifi;
    }

    public void setBytes_wifi(long bytes_wifi) {
        this.bytes_wifi = bytes_wifi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
