package com.projetos.redes.modelos;

import com.projetos.redes.enums.MinutosCapturaDados;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.utilidades.UtilidadeData;

public class ResultadoFinalLexico {
    private UtilidadeData data;
    private Sentimento sentimento;
    private ConsumoInternet usoRede;
    private MinutosCapturaDados intervaloConsiderado;

    public ResultadoFinalLexico(UtilidadeData data, Sentimento sentimento, ConsumoInternet usoRede, MinutosCapturaDados intervaloConsiderado) {
        this.data = data;
        this.sentimento = sentimento;
        this.usoRede = usoRede;
        this.intervaloConsiderado = intervaloConsiderado;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public ConsumoInternet getUsoRede() {
        return usoRede;
    }

    public String toString(){
        return String.format("%s;%s;%d;%s;%d",
                data.pegarDataSemHoras(),data.pegarHorasDaData(),usoRede.getTotal(),sentimento.toString(),this.intervaloConsiderado.getValor());
    }

}