package com.projetos.redes;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import com.projetos.redes.models.ResultadoFinal;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

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

    public static String convertMb(long size){
        long n = 1000;
        String s = "";
        double kb = size / n;
        double mb = kb / n;
        double gb = mb / n;
        double tb = gb / n;
        if(size < n) {
            s = size + " Bytes";
        } else if(size >= n && size < (n * n)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (n * n) && size < (n * n * n)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (n * n * n) && size < (n * n * n * n)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (n * n * n * n)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

    public static void gravaResultadoFinalTxt(ResultadoFinal rf, Context con){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            try {
                String path = con.getFilesDir() + "/app_test/";
                Log.d(tag, path);
                File directory = new File(path);
                directory.mkdir();
                File res = new File(directory, "arquivo_para_enviar.txt");
                Log.d(tag, "res: "  +res.getPath());
                FileOutputStream out = new FileOutputStream(res, true);
                StringBuilder sb = new StringBuilder();
                DateFormat df = getDateFormatter();
                Date d = df.parse(rf.getDtInicio());
                Date d2 = df.parse(rf.getDtFim());
                long min = (d2.getTime() - d.getTime())/1000; // obtém minutos
                sb.append(d.getDay());
                sb.append("\\");
                sb.append(d.getMonth());
                sb.append("\\");
                sb.append(d.getYear());
                sb.append(";");
                sb.append(d.getHours());
                sb.append(";");
                sb.append(rf.getTotalBytes());
                sb.append(";");
                sb.append(rf.getSentimento());
                sb.append(";");
                sb.append(min);
                out.write("teste".getBytes());
                out.flush();
                out.close();
                Log.d(tag, "SB: " + sb.toString());
            }catch (Exception e){
                Log.d(tag, "Erro ao salvar arquivos. ERRO: " + e.getMessage());
                e.printStackTrace();
            }
        }else{
            Toast.makeText(con, "Desculpe, não foi possível gravar resultados na pasta downloads.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean MyAccebilityServiceIsRunning(Context c){
        return true;
    }

}
