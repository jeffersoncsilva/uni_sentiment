package com.projetos.redes.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.Palavras;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InserePalavras {
    private final String TAG = "DB.InsereSentencas";
    private final String nameFile = "palavras.txt";
    private LexicoDb db;
    private Context context;

    public InserePalavras(LexicoDb db, Context con){
        this.db = db;
        this.context = con;
        doIt();
    }

    private void doIt() {
        Log.i(TAG, "Inserindo dados do arquivo lexico_unificado.txt na base de dados.");
        String line = "";
        StringBuilder sb;
        int linha = 0;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(nameFile)));
            while((line = reader.readLine()) != null){
                try{
                    String[] v = line.split(" ");
                    sb = new StringBuilder();
                    for(int i = 0; i < v.length-1; i++)
                        sb.append(v[i]);
                    Palavras lu = new Palavras();
                    lu.setPalavra(sb.toString());
                    lu.setPeso(Integer.parseInt(v[v.length - 1]));
                    db.insertDb(lu, context);
                }catch (NumberFormatException nfe){
                    Log.d(TAG, "linha: "+linha);
                } catch(SQLiteConstraintException sq){
                    Log.d(TAG, "linha: "+linha);
                    sq.printStackTrace();
                } catch(Exception e){
                    Log.d(TAG, "linha: "+linha);
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            Log.v(TAG, "Erro ao ler arquivo de sentença. ERRO: " + e.getMessage());
        }
        Log.i(TAG, "Inserção de dados concluida. Dados Inseridos: " + db.getSizeTableLexicoUnificado());
    }
}
