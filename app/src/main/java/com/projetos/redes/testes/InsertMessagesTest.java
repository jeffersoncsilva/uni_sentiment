package com.projetos.redes.testes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.projetos.redes.Lexico;
import com.projetos.redes.bd.LexicoDb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class InsertMessagesTest extends AsyncTask<Void, Void,Void> {
    private final String tag = "InsertMessagesTest";
    private Context c;
    private LexicoDb db;
    private Lexico lexico;

    public InsertMessagesTest(Context c){
        this.c = c;
        db = new LexicoDb(c);
        lexico= new Lexico(c);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //Log.d(tag, "Pegando dados de rede.");
        //long d = lexico.pegarDadosRede();
        //Log.d(tag, "Populando base para testes");
        insertDatabaseTest();
        lexico.db.execSql("delete from tb_lexico_result;");
        lexico.executarLexico();
        Log.d(tag, "Lexico executado.");

        // Log.d(tag, "Classificando sentimentos do usuario.");
        // lexico.classificaSentimentos(d);

        return null;
    }

    private void insertDatabaseTest(){
        try {
            db.apagarTbUsrMsg();
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.c.getAssets().open("sql.txt"), "UTF-8"));
            int k = 0;
            while((line = reader.readLine()) != null){
                db.execSql(line);
                k++;
            }
            Log.d(tag, "Linhas: " + k);
        }catch (Exception e){
            Log.d(tag, "Erro ao ler arquivos de insert de sql. ERRO:  "+e.getMessage());
            e.printStackTrace();
        }
    }

}
