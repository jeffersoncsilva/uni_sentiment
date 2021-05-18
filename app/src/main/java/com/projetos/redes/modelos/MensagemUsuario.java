package com.projetos.redes.modelos;

import com.projetos.redes.utilidades.UtilidadeData;

public class MensagemUsuario {
    private final String autor;
    private final String mensagem;
    private final UtilidadeData utilidadeData;


    public MensagemUsuario(UtilidadeData utilidadeData, String autor, String msg){
        this.utilidadeData = utilidadeData;
        this.autor = autor;
        this.mensagem = msg;
    }

    public String getAutor() {
        return autor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public UtilidadeData getUtilidadeData() {
        return utilidadeData;
    }

    public String toString(){
        return utilidadeData + " " + autor + " " + mensagem;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof MensagemUsuario){
            MensagemUsuario mu = (MensagemUsuario)o;
            return mu.getAutor().equals(autor);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return autor.hashCode();
    }
}
