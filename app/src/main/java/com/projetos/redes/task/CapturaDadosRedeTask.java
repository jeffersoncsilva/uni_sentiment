package com.projetos.redes.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.projetos.redes.Utils;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.ConsumoInternet;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.services.BuscaConsumoInternet;
import com.projetos.redes.utilidades.UtilidadeData;

import java.util.Date;
import java.util.List;

public class CapturaDadosRedeTask {
    private List<MensagemUsuario> msgs;
    private BancoDeDados banco;
    private Context mContext;
    private BuscaConsumoInternet buscador;

    public CapturaDadosRedeTask(Context context, List<MensagemUsuario> ms){
        banco = new BancoDeDados(context);
        buscador = new BuscaConsumoInternet(context);
        this.mContext = context;
        this.msgs = ms;
    }


    public void doIt() {
        Log.d("CapturaDadosRede", "INICIO: " + Utils.pegarDataAtual());
        for(MensagemUsuario mu : msgs) {
            int hora = mu.getUtilidadeData().getHora();
            int minuto = mu.getUtilidadeData().getMinutos();
            ConsumoInternet uso = banco.pegaIntervaloDoBanco(hora, minuto);
            if(uso == null)
                insereIntervaloBanco(mu.getUtilidadeData(), hora, minuto);
            else
                atualizaIntervaloNoBanco(uso, mu.getUtilidadeData());
        }
        Log.d("CapturaDadosRede", "FIM: " + Utils.pegarDataAtual());
    }

    private void insereIntervaloBanco(UtilidadeData utilidadeData, int hora, int minuto){
        Date d = utilidadeData.getDate();
        d.setHours(hora);
        int mIni = pegaMinutoInicial(minuto);
        d.setMinutes(mIni);
        long inicio = d.getTime();
        int mFim = pegaMinutoFinal(minuto);
        d.setMinutes(mFim);
        long fim = d.getTime();
        long wifi = buscador.pegarConsumoWiFi(inicio, fim);
        long mobile = buscador.pegarConsumoMobile(inicio, fim);
        banco.insereIntervaloConsumoInternet(new ConsumoInternet( utilidadeData.pegarDataSemHoras(), hora, mIni, mFim, wifi, mobile));
    }

    // int hora, int minuto_inicial, int minuto_final, long wifi, long mobile
    private void atualizaIntervaloNoBanco(ConsumoInternet uso, UtilidadeData da){
        Date d = da.getDate();
        d.setHours(uso.getHora());
        d.setMinutes(uso.getMinuto_inicial());
        long inicio = d.getTime();
        d.setMinutes(uso.getMinuto_final());
        long fim = d.getTime();
        uso.setWifi(buscador.pegarConsumoWiFi(inicio, fim));
        uso.setMobile(buscador.pegarConsumoMobile(inicio, fim));
        banco.atualizaIntervaloConsumoInternet(uso);
    }

    // Pega menor multiplo mais proximo de m.
    private int pegaMinutoInicial(int m){
        if(m >= 55)
            return 55;
        while(m % 5 != 0)
            m--;
        return m;
    }

    // Pega maior multiplo mais proximo de m.
    private int pegaMinutoFinal(int m){
        if(m >= 55)
            return 59;
        while(m % 5 != 0)
            m++;
        return m;
    }
}
