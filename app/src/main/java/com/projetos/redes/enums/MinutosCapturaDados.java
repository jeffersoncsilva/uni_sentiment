package com.projetos.redes.enums;

public enum MinutosCapturaDados {
    QUINSE_MINUTOS(0,15, "15 Minutos"),
    TRINTA_MINUTO(1, 30, "30 Minutos"),
    QUARENTACINCO_MINUTOS(2, 45, "45 Minutos"),
    UMA_HORA(3, 60, "1 Hora"),
    DUAS_HORAS(4, 120, "2 Horas"),
    QUATRO_HORAS(5, 240, "4 Horas"),
    OITO_HORAS(6, 480, "8 Horas");

    private final int id;
    private final int valor;
    private final String desc;

    MinutosCapturaDados(int id,int valor, String s){
        this.id = id;
        this.desc= s;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getValor(){
        return valor;
    }

    @Override
    public String toString(){
        return this.desc;
    }

    public static MinutosCapturaDados factory(int i){
        switch (i){
            case 0:
                return QUINSE_MINUTOS;
            case 1:
                return TRINTA_MINUTO;
            case 2:
                return QUARENTACINCO_MINUTOS;
            case 3:
                return UMA_HORA;
            case 4:
                return DUAS_HORAS;
            case 5:
                return QUATRO_HORAS;
            default:
                return OITO_HORAS;
        }
    }
}
