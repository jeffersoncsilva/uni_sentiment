package com.projetos.redes.services;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.UsoDeInternet;
import com.projetos.redes.utilidades.Data;

public class BuscadorConsumoInternet {
    private final String tag = "NetworkUsageService";

    private final BancoDeDados banco;
    private Context mContext;

    public BuscadorConsumoInternet(Context context){
        banco = new BancoDeDados(context);
        mContext = context;
    }

    public BuscadorConsumoInternet(BancoDeDados l){
        this.banco = l;
    }

    public void salvarConsumoDataInicialAteAtualNoIntervalo(Data inicio, int intervalo){
        banco.limparDadosDeConsumo();
        long fimIntervalo = System.currentTimeMillis();
        long inicioIntervalo = inicio.dataEmMilisegundos();

        while (inicioIntervalo < fimIntervalo){
            Data dtIni = new Data(inicioIntervalo);
            inicioIntervalo += (intervalo * 60000);
            Data dtFim = new Data(inicioIntervalo);
            UsoDeInternet uso = pegarConsumoNoIntervalo(dtIni, dtFim);
            banco.insereDadosDeRede(uso);
        }
    }

    public UsoDeInternet pegarConsumoNoIntervalo(Data dataInicio, Data dataFim){
        NetworkStatsManager man = (NetworkStatsManager) mContext.getSystemService(Context.NETWORK_STATS_SERVICE);
        long wifi = pegarConsumoWiFi(man, dataInicio.dataEmMilisegundos(), dataFim.dataEmMilisegundos());
        long mobile = pegarConsumoMobile(man, dataInicio.dataEmMilisegundos(), dataFim.dataEmMilisegundos());
        return new UsoDeInternet( new UsoDeInternet.Consumo(wifi, mobile), dataInicio, dataFim);
    }

    private long pegarConsumoWiFi(NetworkStatsManager man, long start, long end){
        try{
            NetworkStats.Bucket bucket =man.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", start, end);
            if(bucket != null)
                return bucket.getRxBytes() + bucket.getTxBytes();
        }catch (Exception e){
            Log.e(tag, "Erro ao obter dados de utilização pelo wifi. ERRO: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private long pegarConsumoMobile(NetworkStatsManager m, long start, long end){
        try{
            NetworkStats.Bucket bucket = m.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(mContext, ConnectivityManager.TYPE_MOBILE), start, end);
            if(bucket != null)
                return bucket.getRxBytes() + bucket.getTxBytes();
        }catch (Exception e){
            Log.e(tag, "Erro ao obter dados de utilização pelo wifi. ERRO: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }
}