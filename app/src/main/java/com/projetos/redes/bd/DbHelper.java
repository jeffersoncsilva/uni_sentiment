package com.projetos.redes.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.projetos.redes.models.LexicoResult;
import com.projetos.redes.models.Palavras;
import com.projetos.redes.models.NetworkUsage;
import com.projetos.redes.models.Frases;
import com.projetos.redes.models.UsrMsg;

public class DbHelper extends SQLiteOpenHelper {
    private static final String tag = "DbHelper";
    public static int VERSION = 1;
    public static String DATA_BASE_NAME = "DbLexico";

    public DbHelper(Context con){
        super(con, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela que armazena o arquivo lexico_unificado.txt
        String sqlUnif = "CREATE TABLE IF NOT EXISTS "+ Palavras.TB_PALAVRAS +"(sentenca TEXT PRIMARY KEY, peso INTEGER);";
        // Cria a tabela que armazena o arquivo frases.txt
        String sqlSenteca = "CREATE TABLE IF NOT EXISTS "+ Frases.TB_FRASES +"(frase TEXT PRIMARY KEY, peso INTEGER);";
        // Cria a tabela LexicoResult

        try {
            db.execSQL(sqlUnif);
            db.execSQL(sqlSenteca);
            db.execSQL(LexicoResult.SQL_CREATE_TABLE_LR);
            db.execSQL(NetworkUsage.SQL_TB_CREATE);
            db.execSQL(UsrMsg.SQL_CREATE_TB_USR);
            db.execSQL("CREATE TABLE tb_result_final (dt_inicio TEXT, dt_fim TEXT, wifi INTEGER, mobile INTEGER, sentimento TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
            Log.d(tag, "Sql executadas com sucesso! Base de dados criadas. ");
        }catch (Exception e ){
            Log.d(tag, "Erro ao executar onCreate. ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String unificad = "DROP TABLE IF EXISTS " + Palavras.TB_PALAVRAS + ";";
        String sentenca = "DROP TABLE IF EXISTS "+ Frases.TB_FRASES +";";
        String lexicoResult = "DROP TABLE IF EXISTS " + LexicoResult.TB_LEXICO_RESULT + ";";
        String netUsage = "DROP TABLE IF EXISTS " + NetworkUsage.TB_NET_USAGE +";";
        String sqlUsr =  "DROP TABLE IF EXISTS " + UsrMsg.TB_USR_MSG +";";
        try {
            db.execSQL(unificad);
            db.execSQL(sentenca);
            db.execSQL(lexicoResult);
            db.execSQL(netUsage);
            db.execSQL(sqlUsr);
            onCreate(db);
        }catch (Exception e){
            Log.d(tag, "Erro ao executar onUpgrade. ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
