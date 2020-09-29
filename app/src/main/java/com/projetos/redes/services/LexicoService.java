package com.projetos.redes.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.projetos.redes.Lexico;

public class LexicoService extends IntentService {
    private final String tag = "LexicoService";

    public LexicoService(){
        super("LexicoService");
        Log.d(tag, "LexicoService criado pelo construtor 1.");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Lexico l = new Lexico(getApplicationContext());
        l.executarLexico();
        Toast.makeText(getApplicationContext(), "Lexico foi executado. ", Toast.LENGTH_LONG).show();
        Log.d(tag, "lexico executado.");
    }

}
