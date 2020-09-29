package com.projetos.redes.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.projetos.redes.models.NetworkUsage;

public class LexicoBroadcast extends BroadcastReceiver {
    public static final int REQUEST_CODE = 13456;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, LexicoService.class);
        context.startService(in);
        Intent in2 = new Intent(context, NetworkUsageService.class);
        context.startService(in2);

        Log.d("LexicoBroadcast", "Inicializado serviço do lexico e do network usage.");
    }
}
