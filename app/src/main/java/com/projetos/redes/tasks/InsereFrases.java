package com.projetos.redes.tasks;

import android.content.Context;
import android.util.Log;

import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.Frases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InsereFrases {
    private final String TAG = "DB.InsereFrases";
    private final String nameFile = "frases.txt";
    private LexicoDb db;
    private Context context;

    public InsereFrases(LexicoDb db, Context con){
        this.db = db;
        this.context = con;
        doIt();
    }


    private void doIt()  {
        Log.i(TAG, "Inserindo dados do arquivo lexico_unificado.txt na base de dados.");
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(nameFile)));
            String line;
            while((line = reader.readLine()) != null){
                String[] values = line.split(";");
                Frases s = new Frases();
                s.setFrase(values[0]);
                s.setPeso(Integer.parseInt(values[1].replaceAll(" ","")));
                db.insertDb(s, context);
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            Log.v(TAG, "Erro ao ler arquivo de sentença. ERRO: " + e.getMessage());
        }
        Log.i(TAG, "Inserção de dados concluida. Dados Inseridos: " + db.getSizeTableSentenca());
    }
}
