package com.projetos.redes;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public static DateFormat getDateFormatter(){
        return new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
    }

}
