package com.projetos.redes.enums;

public enum DiasAnterioresParaAnalise {
    UM_DIA(0,1, "1 Dia"),
    DOIS_DIAS(1,2, "2 Dias"),
    QUATRO_DIAS(2,3, "4 Dias"),
    UMA_SEMANA(3,7, "7 Dias"),
    DUAS_SEMANAS(4,14, "14 Dias");

    private final int id;
    private final int valor;
    private final String desc;

    DiasAnterioresParaAnalise(int id, int valor, String s){
        this.id = id;
        this.desc= s;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getValor(){return valor;}

    @Override
    public String toString(){
        return this.desc;
    }

    public static DiasAnterioresParaAnalise factory(int i){
        switch (i){
            case 0:
                return UM_DIA;
            case 1:
                return DOIS_DIAS;
            case 2:
                return QUATRO_DIAS;
            case 3:
                return UMA_SEMANA;
            default:
                return DUAS_SEMANAS;
        }
    }
}
