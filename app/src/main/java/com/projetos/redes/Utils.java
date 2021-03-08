package com.projetos.redes;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static final String ID_USUARIO = "id_usuario";
    public static final String TEMPO_CAPTURA_REDE = "tempo_redes";
    public static final String ATIVAR_TUTORIAL = "ativa_tutorial";
    public final static String PADRAO_DATA = "dd/MM/yy hh:mm a";
    public static final String CONFIG = "config";
    public static final String JA_VIU_TUTORIAL = "tutorial_cumprido";
    public static final String DIAS_ANTERIOR_PARA_ANALISAR = "DAPA  ";

    /**
     * Checa se foi dada autorização do usuario para o app acessar dados do dispositivo (tempo ligado, consumo de redes, e outros)
     * @param c contexto da aplicação
     * @return true caso seja consedido.
     */
    public static boolean verificaPermissaoAcessoAosDadosTelefone(Context c){
        AppOpsManager appOps = (AppOpsManager) c.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), c.getPackageName());
        return mode == MODE_ALLOWED;
    }

    /**
     * Verifica se possui a permissão para poder ter acesso ao telefone e com isso poder visualizar os dados de internet movel usado.
     * URL: https://developer.android.com/reference/android/Manifest.permission#READ_PHONE_STATE
     * @param c contexto da aplicação
     * @return verdadeiro caso seja concedida.
     */
    public static boolean verificaSeTemPermisaoReadPhoneState(Context c){
        return ContextCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static DateFormat pegarFormatadorDatasPadrao(){
        return new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
    }

    public static String converterParaMegabytes(long size){
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

    public static Date formataDataParaDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a");
        String[] dates = data.split(" ");
        String d = dates[0] +" " + dates[1] + " ";
        if(dates[2].contains("da manha"))
            d += " AM";
        else
            d += "PM";
        Date d1 = null;
        try{
            d1 = sdf.parse(d);
        }catch (ParseException pe){

        }
        return d1;
    }

    public static void DesativarPularTutorial(Context con){
        SharedPreferences.Editor edit = con.getSharedPreferences(CONFIG, MODE_PRIVATE).edit();
        edit.putBoolean(JA_VIU_TUTORIAL, false);
        edit.commit();
    }

    public static String pegarDataAtual(){
        return pegarFormatadorDatasPadrao().format(new Date(System.currentTimeMillis()));
    }

}
