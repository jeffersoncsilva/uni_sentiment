package com.projetos.redes.bd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetos.redes.Utils;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.models.Data;
import com.projetos.redes.models.LexicoResult;
import com.projetos.redes.models.Palavras;
import com.projetos.redes.models.NetworkUsage;
import com.projetos.redes.models.ResultadoFinal;
import com.projetos.redes.models.Frases;
import com.projetos.redes.models.UsrMsg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LexicoDb {
    private static final String tag = "LexicoDb";
    private SQLiteDatabase select;
    private SQLiteDatabase insert;
    private Context mContext;

    @SuppressLint("StaticFieldLeak")
    private static LexicoDb instance = null;

    private static void createInstance(Context c) {
        instance = new LexicoDb(c);
    }

    public static LexicoDb getInstance(Context c) {
        if (instance == null)
            createInstance(c);
        return instance;
    }

    public LexicoDb(Context c) {
        this.mContext = c;
        DbHelper db = new DbHelper(c);
        select = db.getReadableDatabase();
        insert = db.getWritableDatabase();

    }

    public static boolean insertDb(Data dados, Context c) {

        if (instance == null)
            createInstance(c);

        if (dados instanceof Palavras)
            return instance.insertLexicoTable((Palavras) dados);
        else if (dados instanceof Frases)
            return instance.inserSentenca((Frases) dados);
        else if (dados instanceof LexicoResult)
            return instance.insertResult((LexicoResult) dados);
        else if (dados instanceof NetworkUsage)
            return instance.insertNetorkData((NetworkUsage) dados);
        else if (dados instanceof UsrMsg)
            return instance.insertUsrMsg((UsrMsg) dados);
        else if (dados instanceof ResultadoFinal)
            return instance.insertLexicoResultFinal((ResultadoFinal) dados);
        return false;
    }

    public Cursor getUserMessages() {
        if (instance == null)
            createInstance(this.mContext);
        return instance.select.rawQuery("SELECT " + UsrMsg.CL_MSG + " FROM " + UsrMsg.TB_USR_MSG + ";", null);
    }

    public List<NetworkUsage> getNetworkUsage() {
        List<NetworkUsage> lst = new ArrayList<>();
        Cursor c = instance.select.rawQuery(NetworkUsage.SQL_SELECT, null);
        if (c == null)
            return lst;
        if (c.moveToFirst()) {
            do {
                NetworkUsage nu = new NetworkUsage();
                nu.setDt_inicio(c.getString(0));
                nu.setDt_fim(c.getString(1));
                nu.setBytes_wifi(c.getLong(2));
                nu.setBytes_mobile(c.getLong(3));
                lst.add(nu);
            }while (c.moveToNext());
        }
        return lst;
    }

    public List<LexicoResult> getLexicoResult() {
        List<LexicoResult> lst = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s FROM %s;", LexicoResult._DATA, LexicoResult._FRASE, LexicoResult._SENTIMENTO, LexicoResult.TB_LEXICO_RESULT);
        Cursor c = instance.select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            do {
                LexicoResult lr = new LexicoResult();
                lr.setData(c.getString(0));
                lr.setFrase(c.getString(1));
                String sentimento = c.getString(2);
                if(sentimento.equals(Sentimento.POSITIVO.toString()))
                    lr.setSentimento(Sentimento.POSITIVO);
                else
                    lr.setSentimento(Sentimento.NEGATIVO);
                lst.add(lr);
            }while (c.moveToNext());
            c.close();
        }
        return lst;
    }

    public List<UsrMsg> getUserMsgs() {
        List<UsrMsg> msgs = new ArrayList<>();
        Cursor c = instance.select.rawQuery(UsrMsg.SQL_SELECT_MGS, null);
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                UsrMsg u = new UsrMsg();
                u.setMsg(c.getString(0));
                u.setDate(c.getString(1));
                msgs.add(u);
            }
        } else {
            Log.d(tag, "Não foi possivel carregar as menssagens do usuario.");
        }
        return msgs;
    }

    public List<ResultadoFinal> getResultadoFinal() {
        List<ResultadoFinal> ls = new ArrayList<>();
        Cursor c = instance.select.rawQuery("SELECT dt_fim, dt_inicio, sentimento, wifi, mobile FROM tb_result_final;", null);
        if (c != null && c.moveToFirst()) {
            while (c.moveToNext()) {
                ResultadoFinal rf = new ResultadoFinal();
                rf.setDtFim(c.getString(0));
                rf.setDtInicio(c.getString(1));
                String sent = c.getString(2);
                rf.setSentimento(sent.equals(Sentimento.POSITIVO.toString()) ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
                rf.setWifi(c.getLong(3));
                rf.setMobile(c.getLong(4));
                rf.final_res = getResultFim(rf);
                ls.add(rf);
            }
        }

        return ls;
    }

    private String getResultFim(ResultadoFinal rf){
        DateFormat df = Utils.getDateFormatter();
        String str = "";
        try {
            Date d = df.parse(rf.getDtInicio());
            Date d2 = df.parse(rf.getDtFim());
            long min = ((d2.getTime() - d.getTime())/1000)/60; // obtém minutos
            df = new SimpleDateFormat("dd/MM/yyyy");
            //Data;hora;consumo_wifi(kb);consumo_mobile(kb);sentimento;intervalo_minutos
            str = String.format("%s;%d:00;%d;%s;%d", df.format(d), d.getHours(), rf.getTotalBytes(), rf.getSentimento(), min);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public Cursor getTableSaldoSentenca(String msg) {
        if (instance == null)
            createInstance(this.mContext);
        return instance.select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.frase", Frases.TB_FRASES, msg), null);
    }

    public Cursor getSaldoPalavra(String p) {
        if (instance == null)
            createInstance(this.mContext);

        return instance.select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.sentenca", Palavras.TB_PALAVRAS, p), null);
    }

    public NetworkUsage getLastNetUsage() {
        if (instance == null)
            createInstance(mContext);
        String sql = "SELECT id, dt_fim, bytes_mobile, bytes_wifi, dt_inicio FROM tb_net_usage WHERE id = (SELECT max(id) FROM tb_net_usage);";
        NetworkUsage nu = new NetworkUsage();
        Cursor c = instance.select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            nu.setId(c.getLong(0));
            nu.setDt_fim(c.getString(1));
            nu.setBytes_mobile(c.getLong(2));
            nu.setBytes_wifi(c.getLong(3));
            nu.setDt_inicio(c.getString(4));
        }
        return nu;
    }

    public NetworkUsage getNetUsage(String sql){
        if (instance == null)
            createInstance(mContext);
        NetworkUsage nu = new NetworkUsage();
        Cursor c = instance.select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            nu.setId(c.getLong(0));
            nu.setDt_fim(c.getString(1));
            nu.setBytes_mobile(c.getLong(2));
            nu.setBytes_wifi(c.getLong(3));
            nu.setDt_inicio(c.getString(4));
        }
        return nu;
    }

    public long getNetSoma(String sql){
        if (instance == null)
            createInstance(mContext);
        Cursor c = instance.select.rawQuery(sql, null);
        if(c != null && c.moveToFirst())
            return c.getLong(0);
        else
            return 0;
    }

    public int getTotalSentimentos(String sentimento){
        int qtd = 0;
        try{
            String sql = "SELECT count(sentimento) from tb_lexico_result WHERE sentimento = '" + sentimento+"';";
            Cursor c = instance.select.rawQuery(sql, null);
            if(c != null && c.moveToNext()){
                qtd = c.getInt(0);
            }
        }catch (Exception e){
            Log.d(tag, "Erro ao contar os sentimentos. ERRO: " + e.getMessage());
            e.printStackTrace();
        }
        return qtd;
    }

    public void apagarTbUsrMsg() {
        if (instance == null)
            createInstance(mContext);
        instance.insert.execSQL("DELETE FROM " + UsrMsg.TB_USR_MSG);
    }

    public boolean hasSentencaDatabase() {
        return getSizeTableSentenca() > 0;
    }

    public int getSizeTableSentenca() {
        Cursor c = select.rawQuery("SELECT COUNT(*) FROM " + Frases.TB_FRASES + ";", null);
        if (c.moveToFirst()) {
            int s = c.getInt(0);
            c.close();
            return s;
        } else {
            c.close();
            return -1;
        }
    }

    public boolean hasLexicoUnificado() {
        return getSizeTableLexicoUnificado() > 0;
    }

    // Método criado para teste, remover ao final.
    public boolean execSql(String sql) {
        try {
            if (instance == null)
                createInstance(mContext);

            instance.insert.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados de teste. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getSizeTableLexicoUnificado() {
        Cursor c = select.rawQuery("SELECT COUNT(*) FROM " + Palavras.TB_PALAVRAS + ";", null);
        if (c.moveToFirst()) {
            int b = c.getInt(0);
            c.close();
            return b;
        }
        c.close();
        return -1;
    }

    private boolean insertLexicoTable(Palavras lx) {
        ContentValues cv = new ContentValues();
        cv.put("sentenca", lx.getPalavra());
        cv.put("peso", lx.getPeso());
        try {
            insert.insert(Palavras.TB_PALAVRAS, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean inserSentenca(Frases s) {
        ContentValues cv = new ContentValues();
        cv.put("frase", s.getFrase());
        cv.put("peso", s.getPeso());
        try {
            insert.insert(Frases.TB_FRASES, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertResult(LexicoResult r) {
        try {
            String sql = r.getSqlInsert();
            insert.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertNetorkData(NetworkUsage ns) {
        ContentValues cv = new ContentValues();
        cv.put(NetworkUsage.COLUMN_DT_INICIO, ns.getDt_inicio());
        cv.put(NetworkUsage.COLUMN_DT_FIM, ns.getDt_fim());
        cv.put(NetworkUsage.COLUMN_BYTES_WIFI, ns.getBytes_wifi());
        cv.put(NetworkUsage.COLUMN_BYTES_MOBILE, ns.getBytes_mobile());
        try {
            insert.insert(NetworkUsage.TB_NET_USAGE, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertUsrMsg(UsrMsg msg) {
        ContentValues cv = new ContentValues();
        if (msg.getMsg().equals("Digite uma mensagem") || msg.getMsg().length() < 2)
            return true;
        cv.put("date", msg.getDate());
        cv.put(UsrMsg.CL_MSG, msg.getMsg());
        try {
            String sql = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s');", UsrMsg.TB_USR_MSG, UsrMsg.CL_DATE, UsrMsg.CL_MSG, msg.getDate(), msg.getMsg());
            insert.execSQL(sql);
            Log.d(tag, "inserindo msg usuario. SQL: " + sql);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertLexicoResultFinal(ResultadoFinal rf) {
        try {
            String sql = String.format("INSERT INTO tb_result_final (dt_fim, dt_inicio, sentimento, wifi, mobile) VALUES (\"%s\", \"%s\", \"%s\", %d, %d);", rf.getDtFim(), rf.getDtInicio(), rf.getSentimento().toString(), rf.getWifi(), rf.getMobile());
            insert.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados finais na tabela resultado final: ERRO:  " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
