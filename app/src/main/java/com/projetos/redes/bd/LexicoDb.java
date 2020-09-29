package com.projetos.redes.bd;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.models.Data;
import com.projetos.redes.models.LexicoResult;
import com.projetos.redes.models.LexicoUnificado;
import com.projetos.redes.models.NetworkUsage;
import com.projetos.redes.models.Sentenca;
import com.projetos.redes.models.UsrMsg;
import java.util.ArrayList;
import java.util.List;

public class LexicoDb {
    private static final String tag = "LexicoDb";
    private SQLiteDatabase select;
    private SQLiteDatabase insert;
    private Context mContext;

    @SuppressLint("StaticFieldLeak")
    private static LexicoDb instance = null;

    private static void createInstance(Context c){
        instance = new LexicoDb(c);
    }

    public static LexicoDb getInstance(Context c){
        if(instance==null)
            createInstance(c);
        return instance;
    }

    public LexicoDb(Context c){
        this.mContext = c;
        DbHelper db = new DbHelper(c);
        select = db.getReadableDatabase();
        insert = db.getWritableDatabase();

    }

    public static boolean insertDb(Data dados, Context c){

        if(instance == null)
            createInstance(c);

        if(dados instanceof LexicoUnificado)
            return instance.insertLexicoTable((LexicoUnificado)dados);
        else if(dados instanceof Sentenca)
            return instance.inserSentenca((Sentenca)dados);
        else if(dados instanceof LexicoResult)
            return instance.insertResult((LexicoResult)dados);
        else if(dados instanceof NetworkUsage)
            return instance.insertNetorkData((NetworkUsage) dados);
        else if(dados instanceof UsrMsg)
            return instance.insertUsrMsg((UsrMsg) dados);
        return false;
    }

    public Cursor getUserMessages(){
        if(instance == null)
            createInstance(this.mContext);
        return instance.select.rawQuery("SELECT "+ UsrMsg.CL_MSG +" FROM " + UsrMsg.TB_USR_MSG + ";", null);
   }

   public List<NetworkUsage> getNetworkUsage(){
        List<NetworkUsage> lst = new ArrayList<>();
        Cursor c = instance.select.rawQuery(NetworkUsage.SQL_SELECT , null);
        if(c==null)
            return lst;
        if(c.moveToFirst()){
            while(c.moveToNext()){
                NetworkUsage nu  = new NetworkUsage();
                nu.setDt_inicio(c.getString(0));
                nu.setDt_fim(c.getString(1));
                nu.setBytes_wifi(c.getInt(2));
                nu.setBytes_mobile(c.getInt(3));
                nu.setTotal(nu.getBytes_mobile()+nu.getBytes_wifi());
                lst.add(nu);
            }
        }
        return lst;
   }

   public List<LexicoResult> getLexicoResult(){
        List<LexicoResult> lst = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s FROM %s;", LexicoResult._DATA, LexicoResult._FRASE, LexicoResult._SENTIMENTO, LexicoResult.TB_LEXICO_RESULT);;
        Cursor c = instance.select.rawQuery(sql, null);
        if(c !=null && c.moveToFirst()){
            while (c.moveToNext()){
                LexicoResult lr  = new LexicoResult();
                lr.setData(c.getString(0));
                lr.setFrase(c.getString(1));
                lr.setSentimento(c.getString(2) == Sentimento.POSITIVO.toString() ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
                lst.add(lr);
            }
            c.close();
        }
        return lst;
   }

   public List<UsrMsg> getUserMsgs(){
       List<UsrMsg> msgs = new ArrayList<>();
       Cursor c = instance.select.rawQuery(UsrMsg.SQL_SELECT_MGS, null);
       if(c.moveToFirst()){
           while(c.moveToNext()){
               UsrMsg u = new UsrMsg();
               u.setMsg(c.getString(0));
               u.setDate(c.getString(1));
               msgs.add(u);
           }
       }else{
           Log.d(tag, "Não foi possivel carregar as menssagens do usuario.");
       }
       return msgs;
   }

   public Cursor getTableSaldoSentenca(String msg){
        if(instance == null)
            createInstance(this.mContext);
        return instance.select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.frase", Sentenca.TB_SENTENCA, msg), null);
   }

   public Cursor getSaldoPalavra(String p){
        if(instance == null)
            createInstance(this.mContext);

       return instance.select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.sentenca", LexicoUnificado.TB_LEXICO_UNIFICADO, p), null);
   }

   public void apagarTbUsrMsg(){
        instance.insert.execSQL("DELETE FROM " + UsrMsg.TB_USR_MSG);
   }

   public boolean hasSentencaDatabase(){
        return getSizeTableSentenca() > 0;
   }

   public int getSizeTableSentenca(){
       Cursor c = select.rawQuery("SELECT COUNT(*) FROM "+Sentenca.TB_SENTENCA + ";", null);
       if(c.moveToFirst()) {
           int s = c.getInt(0);
           c.close();
           return s;
       }
       else {
           c.close();
           return -1;
       }
   }

   public boolean hasLexicoUnificado(){
        return getSizeTableLexicoUnificado() > 0;
    }

   public int getSizeTableLexicoUnificado(){
       Cursor c = select.rawQuery("SELECT COUNT(*) FROM "+LexicoUnificado.TB_LEXICO_UNIFICADO +";",null);
       if(c.moveToFirst()) {
           int b =  c.getInt(0);
           c.close();
           return b;
       }
       c.close();
       return -1;
   }

    private boolean insertLexicoTable(LexicoUnificado lx){
        ContentValues cv = new ContentValues();
        cv.put("sentenca", lx.getSentenca());
        cv.put("peso", lx.getPeso());
        try{
            insert.insert(LexicoUnificado.TB_LEXICO_UNIFICADO, null, cv);
            return true;
        }catch (Exception e){
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean inserSentenca(Sentenca s){
        ContentValues cv = new ContentValues();
        cv.put("frase", s.getFrase());
        cv.put("peso", s.getPeso());
        try{
            insert.insert(Sentenca.TB_SENTENCA, null, cv);
            return true;
        }catch (Exception e){
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertResult(LexicoResult r){
        try{
            String sql  = r.getSqlInsert();
            Log.d(tag, "SQL_INSERT_LEXICO_RESULT: " + sql);
            insert.execSQL(sql);
            return true;
        }catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertNetorkData(NetworkUsage ns){
        ContentValues cv = new ContentValues();
        cv.put(NetworkUsage.COLUMN_DT_INICIO, ns.getDt_inicio());
        cv.put(NetworkUsage.COLUMN_DT_FIM, ns.getDt_fim());
        cv.put(NetworkUsage.COLUMN_BYTES_WIFI, ns.getBytes_wifi());
        cv.put(NetworkUsage.COLUMN_BYTES_MOBILE, ns.getBytes_mobile());
        try{
            insert.insert(NetworkUsage.TB_NET_USAGE, null, cv);
            return true;
        }catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertUsrMsg(UsrMsg msg){
        ContentValues cv = new ContentValues();
        if(msg.getMsg().equals("Digite uma mensagem") || msg.getMsg().length() < 2)
            return true;
        cv.put("date", msg.getDate());
        cv.put(UsrMsg.CL_MSG, msg.getMsg());
        try{
            String sql = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s');", UsrMsg.TB_USR_MSG, UsrMsg.CL_DATE, UsrMsg.CL_MSG, msg.getDate(), msg.getMsg());
            insert.execSQL(sql);
            Log.d(tag, "inserindo msg usuario. SQL: " + sql);
            return true;
        }catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
