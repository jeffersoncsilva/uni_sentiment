package com.projetos.redes.modelos;

public class ConsumoInternet {
    private String dia;
    private int hora, minuto_inicial, minuto_final;
    private long wifi, mobile, id;

    public String getId(){
        return id + "";
    }

    public void setWifi(long wifi) {
        this.wifi = wifi;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public ConsumoInternet(int id, String dia, int hora, int minuto_inicial, int minuto_final, long wifi, long mobile){
        this(dia, hora, minuto_inicial, minuto_final, wifi, mobile);
        this.id = id;
    }

    public ConsumoInternet(String dia, int hora, int minuto_inicial, int minuto_final, long wifi, long mobile) {
        this.hora = hora;
        this.minuto_inicial = minuto_inicial;
        this.minuto_final = minuto_final;
        this.wifi = wifi;
        this.mobile = mobile;
        this.dia = dia;
    }

    public String getDia(){
        return this.dia;
    }

    public int getHora() {
        return hora;
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
}
