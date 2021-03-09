package com.projetos.redes.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetos.redes.modelos.ConsumoInternet;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;

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
    public List<ConsumoInternet> pegarDadosUsoInternet() {
        List<ConsumoInternet> lst = new ArrayList<>();
        Cursor c = select.rawQuery("select dia, hora, minuto_inicial, minuto_final, wi_fi, mobile from uso_de_internet;", null);
        if (c != null && c.moveToFirst()) {
            do {
                // int hora, int minuto_inicial, int minuto_final, long wifi, long mobile
                ConsumoInternet co = new ConsumoInternet(
                        c.getString(0),
                        c.getInt(1),
                        c.getInt(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5));
                lst.add(co);
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
        String sql ="select frase, sentimento, hora, minuto, dia from tb_lexico_result;";// String.format("SELECT %s, %s, %s FROM %s;", ResultadoLexicoProcessado._DATA, ResultadoLexicoProcessado._FRASE, ResultadoLexicoProcessado._SENTIMENTO, ResultadoLexicoProcessado.TB_LEXICO_RESULT);
        Cursor c = select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            do {
                ResultadoLexicoProcessado lr = new ResultadoLexicoProcessado(c.getString(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getString(4));
                lst.add(lr);
            }while (c.moveToNext());
            c.close();
        }
        return lst;
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
            cv.put("frase", r.getFrase());
            cv.put("hora", r.getHora());
            cv.put("minuto", r.getMinuto());
            cv.put("sentimento", r.getSentimento().getId());
            cv.put("dia", r.getDia());
            insert.insert("tb_lexico_result", null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
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

    public int[] pegaResultadoSentimento(String sql){
        int[] res = new int[2];
        try{
            Cursor c = select.rawQuery(sql, null);
            if(c != null && c.moveToFirst()){
                res[0] = c.getInt(0);
                c.moveToNext();
                res[1] = c.getInt(0);
            }
        }catch (Exception e){
            Log.e(tag, "erro ao obter os dados da internet.");
            e.printStackTrace();
        }
        return res;
    }

    public ConsumoInternet pegaIntervaloDoBanco(int hora, int minuto){
        ConsumoInternet consumo = null;
        String sql = "select dia, hora, minuto_inicial, minuto_final, wi_fi, mobile from uso_de_internet where hora == " + hora + " AND minuto_inicial <= "
                        + minuto  + " AND minuto_final >= " + minuto+";";
        Cursor c = select.rawQuery(sql, null);
        if(c != null && c.moveToFirst()){
                consumo = new ConsumoInternet(c.getString(0),c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
        }
        return consumo;
    }

    public void insereIntervaloConsumoInternet(ConsumoInternet con){
        ContentValues cv = new ContentValues();
        cv.put("hora", con.getHora());
        cv.put("minuto_inicial", con.getMinuto_inicial());
        cv.put("minuto_final", con.getMinuto_final());
        cv.put("wi_fi", con.getWifi());
        cv.put("mobile", con.getMobile());
        cv.put("dia", con.getDia());
        try{
            insert.insert("uso_de_internet", null, cv);
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
    }

    public void atualizaIntervaloConsumoInternet(ConsumoInternet  con){
        ContentValues cv = new ContentValues();
        cv.put("hora", con.getHora());
        cv.put("minuto_inicial", con.getMinuto_inicial());
        cv.put("minuto_final", con.getMinuto_final());
        cv.put("wifi", con.getWifi());
        cv.put("mobile", con.getMobile());
        try{
            insert.update("uso_de_internet", cv, "id = ?", new String[] { con.getId() });
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
    }
}
