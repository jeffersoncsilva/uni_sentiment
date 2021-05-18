package com.projetos.redes.services;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BuscaConsumoInternet {
    private final String tag = "NetworkUsageService";
    private Context mContext;
    private NetworkStatsManager man;

    public BuscaConsumoInternet(Context context){
        mContext = context;
        man = (NetworkStatsManager) mContext.getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    public long pegarConsumoWiFi(long start, long end){
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

    public long pegarConsumoMobile(long start, long end){
        try{
            NetworkStats.Bucket bucket = man.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(mContext, ConnectivityManager.TYPE_MOBILE), start, end);
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