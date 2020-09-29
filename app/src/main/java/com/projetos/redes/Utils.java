package com.projetos.redes;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.services.LexicoBroadcast;

import java.util.Objects;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class Utils {
    public static final String tag = "UtilsDebug";
    /**
     * Checa se foi dada autorização do usuario para o app acessar dados do dispositivo (tempo ligado, consumo de redes, e outros)
     * @param c contexto da aplicação
     * @return true caso seja consedido.
     */
    public static boolean checkForPermission(Context c){
        AppOpsManager appOps = (AppOpsManager) c.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), c.getPackageName());
        return mode == MODE_ALLOWED;
    }

    /**
     * Segundo a documentação oficial do android:
     * Allows read only access to phone state, including the current cellular network information, the status of any ongoing calls,
     * and a list of any PhoneAccounts registered on the device.
     * URL: https://developer.android.com/reference/android/Manifest.permission#READ_PHONE_STATE
     * @param c contexto da aplicação
     * @return verdadeiro caso seja concedida.
     */
    public static boolean readPhoneStatePermission(Context c){
        return ContextCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean lexicoJaConfigurado(Context c, String ln){
        //SharedPreferences sp = c.getSharedPreferences(MainActivity.LEXICO_CONFIG, Context.MODE_PRIVATE);
        //return sp.contains(MainActivity.LEXICO_TIME);
        Intent in = new Intent(c, LexicoBroadcast.class);
        PendingIntent pin = PendingIntent.getBroadcast(c, LexicoBroadcast.REQUEST_CODE, in, 0);
        if(pin != null){
            Log.d(tag, "alarme esta setado.");
            //Toast.makeText(c, "Alarme ja setado.", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Log.d(tag, "alarme nao esta setado.");
            //Toast.makeText(c, "Alarme não foi setado.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void setAlarmLexico(Context con, int min){
        try {
            AlarmManager alarm = (AlarmManager) Objects.requireNonNull(con).getSystemService(Context.ALARM_SERVICE);
            Intent in = new Intent(con, LexicoBroadcast.class);
            PendingIntent pen = PendingIntent.getBroadcast(con, LexicoBroadcast.REQUEST_CODE, in, 0);
            alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), min*60000, pen);
            Toast.makeText(con, String.format(con.getString(R.string.str_alarm_set), min), Toast.LENGTH_LONG).show();
        }catch (NullPointerException ne){
            Log.e(tag, "Erro ao criar alarme. ERRO: " + ne.getMessage());
            ne.printStackTrace();
        }
    }

    public static void stopAlarmLexico(Context con){
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(con).getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(con, LexicoBroadcast.class);
        PendingIntent pin = PendingIntent.getBroadcast(con, LexicoBroadcast.REQUEST_CODE, in, 0);
        alarm.cancel(pin);
    }

}
