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

import com.projetos.redes.Utils;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.NetworkUsage;
import com.projetos.redes.models.UsrMsg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class NetworkUsageService {
    private final String tag = "NetworkUsageService";
    private final String net_config = "network_configs";
    private long[] interval;

    private LexicoDb lexico;


    public NetworkUsageService(LexicoDb l){
        this.lexico = l;
        this.interval = new long[2];
    }

    public long getTotalNetUsage(Context context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "autorização de READ_PHONE_STATE não obitida.");
            return -1;
        }
        //Seta a data que esta pegando os dados
        setTimeInterval(context);

        DateFormat df  = Utils.getDateFormatter();
        Log.d(tag, "Data inicio: " + df.format(new Date(interval[0])));
        Log.d(tag, "Data fim: " + df.format(new Date(interval[1])));

        NetworkStatsManager man = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);

        // interval[0] tempo inicial (mais antigo) ||| inteval[1] tempo final (mais recente)
        long wifi = getWifi(man, this.interval[0], this.interval[1]);
        long mobyle = getMobile(man, this.interval[0], this.interval[1], context);

        Log.d(tag, "Dados wifi: " + wifi);
        Log.d(tag, "Dados mobile: " + mobyle);


        DateFormat sdf = Utils.getDateFormatter();
        NetworkUsage nu = new NetworkUsage();
        nu.setDt_inicio(sdf.format(interval[0]));
        nu.setDt_fim(sdf.format(interval[1]));
        nu.setBytes_mobile(mobyle);
        nu.setBytes_wifi(wifi);
        LexicoDb.insertDb(nu, context);
        Log.d(tag, "dt_fim: " + nu.getDt_fim() + " --- dt_inicio: " + nu.getDt_inicio() + " --- bytes: " + nu.getTotal());
        return nu.getTotal();
    }

    private void setTimeInterval(Context context){
        DateFormat df = Utils.getDateFormatter();
        try {
            long min = getTimePrefs(context); // Intervalo de tempo que deve ser considerado em millisegundos
            String dt_last = this.lexico.getLastTimeNetUsage();
            Log.d(tag, "Data recuperada do banco de dados: " + dt_last);
            if(dt_last.isEmpty()){
                // 0 e o inicio
                this.interval[0] = System.currentTimeMillis() - min; // tempo inicial (mais antigo)
                this.interval[1] = System.currentTimeMillis();// tempo final (mais recente)
            }else {
                Date d1 = df.parse(dt_last);
                Calendar c1 = Calendar.getInstance();
                c1.setTime(d1);
                this.interval[0] = c1.getTimeInMillis()+1000;// tempo inicial (mais antigo)
                this.interval[1] = System.currentTimeMillis();// tempo final (mais recente)
            }
            Date d1 = new Date(this.interval[0]);
            Date d2 = new Date(this.interval[1]);

            Log.d(tag, "[0] : " + df.format(d1));
            Log.d(tag, "[1] : " + df.format(d2));

            Log.d(tag, "diff: " + (interval[0] - interval[1]));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna o tempo do service em millisegundos. Pega o intervalo nas preferencias de usuario.
      * @return
     */
    private long getTimePrefs(Context context){
        SharedPreferences prefs = context.getSharedPreferences(net_config, MODE_PRIVATE);
        int min = prefs.getInt("time", -1);
        if(min < 0)
            min = 15;
        min *= 60000; // Converte para milissegundos.
        return 15 * 60000;
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

    public long[] getInterval(){ return this.interval; }
}
