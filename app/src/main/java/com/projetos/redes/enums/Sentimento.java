package com.projetos.redes.enums;

public enum Sentimento {
    POSITIVO(1, "Positivo"),
    NEGATIVO(2, "Negativo");

    private int id;
    private String desc;

    private Sentimento(int id, String s){
        this.id = id;
        this.desc= s;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString(){
        return this.desc;
    }
}
