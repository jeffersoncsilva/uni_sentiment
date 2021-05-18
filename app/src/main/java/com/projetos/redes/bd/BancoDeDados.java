package com.projetos.redes.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.modelos.ConsumoInternet;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;
import com.projetos.redes.utilidades.UtilidadeData;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados {
    private final String TABELA_PALAVRAS = "tb_palavras";
    private final String TABELA_FRASES = "tb_frases";
    private final String TB_LEXICO_PROCESSADO = "tb_lexico_processado";

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
     * Lista o resultado da analise das mensagens após processado pelo algoritmo léxico junto com o
     * sentimento definido.
     * @return lista com o resultado do lexico.
     */
    public List<ResultadoLexicoProcessado> pegarResultadoLexico() {
        List<ResultadoLexicoProcessado> lst = new ArrayList<>();
        String sql ="select frase, sentimento, data from "+TB_LEXICO_PROCESSADO+";";
        Cursor c = select.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            do {
                lst.add(new ResultadoLexicoProcessado(c.getString(0),                  // frase
                                                      Sentimento.factory(c.getInt(1)), // sentimento
                                                      new UtilidadeData(c.getLong(2)))  // data
                );
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
        return c;
    }

    public boolean insereResultadoLexicoProcessado(ResultadoLexicoProcessado r) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("frase", r.getFrase());
            cv.put("data", r.getData().dataEmMilisegundos());
            cv.put("sentimento", r.getSentimento().getId());
            insert.insert(TB_LEXICO_PROCESSADO, null, cv);
            return true;
        } catch (Exception e) {
            Log.d(tag, "Erro ao inserir dados na base de dados. ERRO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void limparTabelaLexicoProcessado() {
        insert.rawQuery("delete from "+TB_LEXICO_PROCESSADO,null);
    }

    public void limparDadosDeConsumo(){
        insert.rawQuery("delete from uso_de_internet",null);
    }

    public int[] pegaResultadoSentimento(String sql){
        int[] res = new int[2];
        try{
            Cursor c = select.rawQuery(sql, null);
            if(c != null && c.moveToFirst()){
                res[0] = c.getInt(0);
                if(c.moveToNext())
                    res[1] = c.getInt(0);
                else
                    res[1] = 0;
            }
        }catch (Exception e){
            Log.e(tag, "erro ao obter os dados da internet.");
            e.printStackTrace();
        }
        return res;
    }

    /********** CONSUMO DE INTERNET *****************/

    /*public void insereOuAtualizaConsumoInternet(ConsumoInternet consumo){
        if(temInconsumoNoBanco(consumo))
            atualizaConsumo(consumo);
        else
            insereConsumo(consumo);
    }*/

    public void atualizaConsumo(ConsumoInternet con){
        ContentValues cv = new ContentValues();
        cv.put("wifi", con.getWifi());
        cv.put("mobile", con.getMobile());
        cv.put("data_inicio", con.getDataInicio());
        cv.put("data_fim", con.getDataFim());
        try{
            insert.update("uso_de_internet", cv, "data_inicio == ? AND data_fim == ?", new String[] { ""+con.getDataInicio(), ""+con.getDataFim() });
            Log.d(tag, "Atualizado consumo!");
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
    }

    public void insereNovoConsumo(ConsumoInternet con){
        ContentValues cv = new ContentValues();
        cv.put("wifi", con.getWifi());
        cv.put("mobile", con.getMobile());
        cv.put("data_inicio", con.getDataInicio());
        cv.put("data_fim", con.getDataFim());
        try{
            insert.insert("uso_de_internet", null, cv);
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
    }

    public ConsumoInternet temInconsumoNesseIntervalo(long tempo){
        ConsumoInternet consumo = null;
        String sql = String.format("select wifi, mobile, data_inicio, data_fim from uso_de_internet where data_inicio <= %d AND data_fim >= %d;",tempo, tempo);
        Cursor cursor = select.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()){
            consumo = new ConsumoInternet(cursor.getLong(0),cursor.getLong(1), cursor.getLong(2), cursor.getLong(3));
            cursor.close();
        }
        return consumo;
    }

    public List<ConsumoInternet> pegarDadosUsoInternet() {
        List<ConsumoInternet> lst = new ArrayList<>();
        Cursor cursor = select.rawQuery("select wifi, mobile, data_inicio, data_fim from uso_de_internet;", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                lst.add(new ConsumoInternet(cursor.getLong(0),cursor.getLong(1), cursor.getLong(2), cursor.getLong(3)));
            }while (cursor.moveToNext());
            cursor.close();
        }
        return lst;
    }
    /**
     * Pega o resultado de dados de consumo de internet salvo no banco de dados.
     * @return lista contendo os dados separados por intervalo de tempo.
     */
    /*

    public ConsumoInternet pegaIntervaloDoBanco(UtilidadeData d, int minuto){
        ConsumoInternet consumo = null;
        String sql = "select dia, mes, ano, hora, minuto_inicial, minuto_final, wifi, mobile, id from uso_de_internet where hora == " + d.pegarHorasDaData() + " AND minuto_inicial <= "
                        + minuto  + " AND minuto_final >= " + minuto+" AND dia == "+d.dia()+" AND mes == "+ d.mes()+" AND ano == "+d.ano()+";";
        Cursor c = select.rawQuery(sql, null);
        if(c != null && c.moveToFirst()){
                consumo = new ConsumoInternet(new UtilidadeData(c.getInt(0), c.getInt(1), c.getInt(2)),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getInt(6),
                        c.getInt(7),
                        c.getInt(8));
        }
        return consumo;
    }

    public void insereIntervaloConsumoInternet(ConsumoInternet con){
        ContentValues cv = new ContentValues();
        cv.put("hora", con.getData().getHora());
        cv.put("minuto_inicial", con.getMinuto_inicial());
        cv.put("minuto_final", con.getMinuto_final());
        cv.put("wifi", con.getWifi());
        cv.put("mobile", con.getMobile());
        cv.put("dia", con.getData().dia());
        cv.put("ano", con.getData().ano());
        cv.put("mes", con.getData().mes());
        cv.put("data", con.getData().dataEmMilisegundos());
        try{
            insert.insert("uso_de_internet", null, cv);
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
    }

    public void atualizaIntervaloConsumoInternet(ConsumoInternet  con){
        ContentValues cv = new ContentValues();
        cv.put("minuto_inicial", con.getMinuto_inicial());
        cv.put("minuto_final", con.getMinuto_final());
        cv.put("wifi", con.getWifi());
        cv.put("mobile", con.getMobile());
        cv.put("data", con.getData().dataEmMilisegundos());
        try{
            insert.update("uso_de_internet", cv, "id = ?", new String[] { con.getId() });
        }catch (Exception e){
            Log.e(tag, "Erro ao inserir o consumo.");
            e.printStackTrace();
        }
     */
}
