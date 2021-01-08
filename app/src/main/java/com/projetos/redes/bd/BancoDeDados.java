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
        Cursor c = select.rawQuery("SELECT dt_fim, dt_inicio, sentimento, wifi, mobile FROM tb_result_final;", null);
        if (c != null && c.moveToFirst()) {
            while (c.moveToNext()) {
                Data fim = new Data(c.getString(0));
                Data inicio = new Data(c.getString(1));
                String sent = c.getString(2);
                Sentimento s = (sent.equals(Sentimento.POSITIVO.toString()) ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
                UsoDeInternet.Consumo co = new UsoDeInternet.Consumo(c.getLong(2), c.getLong(4));
                ResultadoFinalLexico rf = new ResultadoFinalLexico(inicio, fim, new UsoDeInternet(co, inicio, fim), s);
                ls.add(rf);
            }
        }
        c.close();
        return ls;
    }

    public Cursor getTableSaldoSentenca(String msg) {
        return select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.frase", TABELA_FRASES, msg), null);
    }

    public Cursor getSaldoPalavra(String p) {
        return select.rawQuery(String.format("SELECT peso FROM %s AS tb WHERE '%s' = tb.sentenca", TABELA_PALAVRAS, p), null);
    }

    public boolean insereResultadoLexicoProcessado(ResultadoLexicoProcessado r) {
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
            String sql = String.format("INSERT INTO tb_result_final (dt_fim, dt_inicio, sentimento, wifi, mobile) VALUES (\"%s\", \"%s\", \"%s\", %d, %d);", rf.getFim(), rf.getInicio(), rf.getSentimento().toString(), rf.getUsoDeInternet().getConsumo().getWifi(), rf.getUsoDeInternet().getConsumo().getMobile());
            insert.execSQL(sql);
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
}
