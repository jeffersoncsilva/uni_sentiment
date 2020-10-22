package com.projetos.redes.services;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.projetos.redes.Utils;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.NetworkUsage;
import java.text.DateFormat;
import java.text.ParseException;

public class NetworkUsageService {
    private final String tag = "NetworkUsageService";
    private final String net_config = "network_configs";

    private LexicoDb lexico;

    public NetworkUsageService(LexicoDb l){
        this.lexico = l;
    }

    public long[] getTotalNetUsage(Context context) {
        /*E retornado um vetor de 3 posições, onde:
          dados[0]: hora de inicio do intervalo.
          dados[1]: Hora de fim do intervalo.
          dados[2]: dados wifi.
          dados[3]: dados mobile.
         */
        long[] dados = {0,0,0, 0};
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "autorização de READ_PHONE_STATE não obitida.");
            return dados;
        }
        //Seta a data que esta pegando os dados
        dados[1] = System.currentTimeMillis();
        NetworkStatsManager man = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        long wifi = getWifi(man, 0, dados[1]);
        long mobyle = getMobile(man, 0, dados[1], context);
        //Pega o ultimo registro de data inserido no banco de dados.
        NetworkUsage nu_last = this.lexico.getLastNetUsage();
        dados[2] = wifi - nu_last.getBytes_wifi();
        dados[3] = mobyle - nu_last.getBytes_mobile();

        Log.d(tag, "wifi: " + Utils.convertMb(dados[2]));
        Log.d(tag, "mobile: " + Utils.convertMb(dados[3]));

        DateFormat sdf = Utils.getDateFormatter();
        NetworkUsage net = new NetworkUsage(nu_last.getDt_inicio(), sdf.format(dados[1]), wifi, mobyle);
        // Converte o tempo inicial para millisegundos.
        try {
            dados[0] = sdf.parse(nu_last.getDt_inicio()).getTime();
        }catch (ParseException e){
            Log.d(tag, "erro ao converter para milissegundos: ERRO: "+e.getMessage());
            e.printStackTrace();
        }
        LexicoDb.insertDb(net, context);
        return dados;
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