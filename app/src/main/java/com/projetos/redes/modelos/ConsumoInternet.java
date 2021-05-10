package com.projetos.redes.modelos;

import com.projetos.redes.utilidades.UtilidadeData;

public class ConsumoInternet {
    private UtilidadeData data;
    private int minuto_inicial, minuto_final, hora;
    private long wifi, mobile, id;

    public ConsumoInternet(UtilidadeData data, int hora, int minuto_inicial, int minuto_final, long wifi, long mobile) {
        this.hora = hora;
        this.data = data;
        this.minuto_inicial = minuto_inicial;
        this.minuto_final = minuto_final;
        this.wifi = wifi;
        this.mobile = mobile;
    }

    public ConsumoInternet(UtilidadeData data, int hora, int minuto_inicial, int minuto_final, long wifi, long mobile, int id){
        this(data, hora, minuto_inicial, minuto_final, wifi, mobile);
        this.id = id;
    }

    public UtilidadeData getData(){
        return data;
    }

    public String getId(){
        return id + "";
    }

    public void setWifi(long wifi) {
        this.wifi = wifi;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public int getMinuto_inicial() {
        return minuto_inicial;
    }

    public int getMinuto_final() {
        return minuto_final;
    }

    public long getWifi() {
        return wifi;
    }

    public long getMobile() {
        return mobile;
    }

    public String toString(){
        return "Data: " + data.toString() +
                "\nINICIO: "+hora+":"+minuto_inicial+
                "\nFIM: "+hora+":"+minuto_final+
                "\nWIFI: "+convertMb(wifi)+"\nMOBILE: "+convertMb(mobile);
    }

    private String convertMb(long size){
        long n = 1000;
        String s = "";
        double kb = size / n;
        double mb = kb / n;
        double gb = mb / n;
        double tb = gb / n;
        if(size < n) {
            s = size + " Bytes";
        } else if(size >= n && size < (n * n)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (n * n) && size < (n * n * n)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (n * n * n) && size < (n * n * n * n)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (n * n * n * n)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }
}