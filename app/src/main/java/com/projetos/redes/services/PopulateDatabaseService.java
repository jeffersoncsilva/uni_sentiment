package com.projetos.redes.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.tasks.InsertLeixcoUnificado;
import com.projetos.redes.tasks.InsertSentences;

public class PopulateDatabaseService extends IntentService {

    private static final String TAG = "PopulateDatabaseService";

    public PopulateDatabaseService(){
        super("PopulateDatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        LexicoDb db = LexicoDb.getInstance(context);
        if(!db.hasSentencaDatabase())
            new InsertSentences(db, context);
        else
            Log.v(TAG, "Tabela sentenca ja criada.");
        if(!db.hasLexicoUnificado())
            new InsertLeixcoUnificado(db, context);
        else
            Log.v(TAG, "Tabela lexico unificado criado");
    }


}
