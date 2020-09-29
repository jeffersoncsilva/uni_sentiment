package com.projetos.redes.services;

import android.Manifest;
import android.app.IntentService;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.NetworkUsage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NetworkUsageService extends IntentService {
    private final String tag = "NetworkUsageService";
    private final String net_config = "network_configs";
    private final String ultimo_tempo = "last_time";

    public NetworkUsageService(){
        super("NetworkUsageService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "autorização de READ_PHONE_STATE não obitida.");
            return;
        }
        Context context = getApplicationContext();
        long min = getTimePrefs();
        //pega a data atual
        String dateFormater = "dd-MM-yyyy hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormater);
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(System.currentTimeMillis() - min);
        long end = c1.getTimeInMillis();
        long start = c2.getTimeInMillis();

        NetworkStatsManager man = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);

        long wifi = getWifi(man, start, end);
        long mobyle = getMobile(man, start, end, context);

        NetworkUsage nu = new NetworkUsage();
        nu.setDt_inicio(sdf.format(start));
        nu.setDt_fim(sdf.format(end));
        nu.setBytes_mobile(mobyle);
        nu.setBytes_wifi(wifi);
        LexicoDb.insertDb(nu, context);
        Log.d(tag, "concluido. WIFI: " + wifi + " -- MOBILE: " + mobyle + " --- TOTAL: " +(wifi + mobyle));
    }

    /**
     * Retorna o tempo do service em millisegundos. Pega o intervalo nas preferencias de usuario.
      * @return
     */
    private long getTimePrefs(){
        Context context = getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(net_config, MODE_PRIVATE);
        int min = prefs.getInt("time", -1);
        if(min < 0)
            min = 10;
        min *= 60000; // Converte para milissegundos.
        return min;
    }

    private long getWifi(NetworkStatsManager man, long start, long end){
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

    private long getMobile(NetworkStatsManager m, long start, long end, Context c){
        try{
            NetworkStats.Bucket bucket = m.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(c, ConnectivityManager.TYPE_MOBILE), start, end);
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
