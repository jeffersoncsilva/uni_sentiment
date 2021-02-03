package com.projetos.redes.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.modelos.ResultadoFinalLexico;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;
import com.projetos.redes.modelos.UsoDeInternet;
import com.projetos.redes.utilidades.Data;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados {
    private final String TABELA_PALAVRAS = "tb_palavras";
    private final String TABELA_FRASES = "tb_frases";

    private static final String tag = "LexicoDb";
    private final SQLiteDatabase select;
    private final SQLiteDatabase insert;
    private final Context mContext;

    public BancoDeDados(Context c) {
        this.mContext = c;
        DbHelper db = new DbHelper(c);
        select = db.getReadableDatabase();
        insert = db.getWritableDatabase();
    }

    /**
     * Pega o resultado de dados de consumo de internet salvo no banco de dados.
     * @return lista contendo os dados separados por intervalo de tempo.
     */
    public List<UsoDeInternet> pegarDadosUsoInternet() {
        List<UsoDeInternet> lst = new ArrayList<>();
        Cursor c = select.rawQuery(UsoDeInternet.SQL_SELECT, null);
        if (c == null)
            return lst;
        if (c.moveToFirst()) {
            do {
                String dti = c.getString(0);
                String dtf = c.getString(1);
                Data inicio = new Data(dti);
                Data fim = new Data(dtf);
                UsoDeInternet.Consumo co = new UsoDeInternet.Consumo(c.getLong(2), c.getLong(3));
                lst.add(new UsoDeInternet(co, inicio, fim));
            }while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    /**
     * Lista o resultado da analise das mensagens após processado pelo algoritmo léxico junto com o
     * sentimento definido.
     * @return lista com o resultado do lexico.
     */
    public List<ResultadoLexicoProcessado> pegarResultadoLexico() {
        List<ResultadoLexicoProcessado> lst = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s FROM %s;", ResultadoLexicoProcessado._DATA, ResultadoLexicoProcessado._FRASE, ResultadoLexicoProcessado._SENTIMENTO, ResultadoLexicoProcessado.TB_LEXICO_RESULT);
        Cursor c = select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            do {
                Data inicio = new Data(c.getString(0));
                String frase = c.getString(1);
                Sentimento s = c.getString(2).equals(Sentimento.POSITIVO.toString()) ? Sentimento.POSITIVO : Sentimento.NEGATIVO;
                ResultadoLexicoProcessado lr = new ResultadoLexicoProcessado(inicio, frase, s);
                lst.add(lr);
            }while (c.moveToNext());
            c.close();
        }
        return lst;
    }

    public List<ResultadoFinalLexico> pegarResultadoFinal() {
        List<ResultadoFinalLexico> ls = new ArrayList<>();
        Cursor c = select.rawQuery("SELECT dt_fim, dt_inicio, sentimento, wifi, mobile, intervalo FROM tb_result_final;", null);
        if (c != null && c.moveToFirst()) {
           do {
                Data fim = new Data(c.getString(0));
                Data inicio = new Data(c.getString(1));
                String sent = c.getString(2);
                Sentimento s = (Sentimento.POSITIVO.toString().equals(sent) ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
                UsoDeInternet.Consumo co = new UsoDeInternet.Consumo(c.getLong(3), c.getLong(4));
                ResultadoFinalLexico rf = new ResultadoFinalLexico(inicio, fim, new UsoDeInternet(co, inicio, fim), s, c.getInt(5));
                ls.add(rf);
            } while (c.moveToNext());
        }
        c.close();
        return ls;
    }

    public Cursor getTableSaldoSentenca(String msg) {
        Cursor c = select.query(TABELA_FRASES, new String[] { "peso"}, "frase = ?", new String[] { msg }, null, null,null );
        //return select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.frase", TABELA_FRASES, msg), null);
        return c;
    }

    public Cursor getSaldoPalavra(String p) {
        Cursor c = select.query(TABELA_PALAVRAS, new String[] { "peso"}, "sentenca = ?", new String[] { p }, null, null, null);
        //return select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.sentenca", TABELA_PALAVRAS, p), null);
        return c;
    }

    public boolean insereResultadoLexicoProcessado(ResultadoLexicoProcessado r) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(ResultadoLexicoProcessado._FRASE, r.getFrase());
            cv.put(ResultadoLexicoProcessado._DATA, r.getDate().toString());
            cv.put(ResultadoLexicoProcessado._SENTIMENTO, r.getSentimento().toString());
            insert.insert(ResultadoLexicoProcessado.TB_LEXICO_RESULT, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insereDadosDeRede(UsoDeInternet ns) {
        ContentValues cv = new ContentValues();
        cv.put(UsoDeInternet.COLUMN_DT_INICIO, ns.getInicio().toString());
        cv.put(UsoDeInternet.COLUMN_DT_FIM, ns.getFim().toString());
        cv.put(UsoDeInternet.COLUMN_BYTES_WIFI, ns.getConsumo().getWifi());
        cv.put(UsoDeInternet.COLUMN_BYTES_MOBILE, ns.getConsumo().getMobile());
        try {
            insert.insert(UsoDeInternet.TB_NET_USAGE, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insereResultadoFinalLexico(ResultadoFinalLexico rf) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("dt_fim", rf.getFim().toString());
            cv.put("dt_inicio", rf.getInicio().toString());
            cv.put("wifi", rf.getUsoDeInternet().getConsumo().getWifi());
            cv.put("mobile", rf.getUsoDeInternet().getConsumo().getMobile());
            cv.put("intervalo", rf.getIntervalo());
            cv.put("sentimento", rf.getSentimento().toString());
            insert.insert("tb_result_final", null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados finais na tabela resultado final: ERRO:  " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void limparTabelaLexicoProcessado() {
        insert.rawQuery("delete from tb_lexico_result",null);
    }

    public void limparDadosDeConsumo(){
        insert.rawQuery("delete from tb_net_usage",null);
    }
}
