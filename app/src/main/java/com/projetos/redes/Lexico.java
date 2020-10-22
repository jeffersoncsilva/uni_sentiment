package com.projetos.redes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.models.LexicoResult;
import com.projetos.redes.models.ResultadoFinal;
import com.projetos.redes.models.UsrMsg;
import com.projetos.redes.services.NetworkUsageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Lexico {
    private static final String tag = "AnalisadorLexico";
    private Context context;
    public LexicoDb db;
    private NetworkUsageService nus;

    public Lexico(Context c) {
        this.context = c;
        this.db = new LexicoDb(c);
        this.nus = new NetworkUsageService(this.db);
    }

    /**
     * @return SOMENTE USADO PARA TESTAR A LEITURA DOS ARQUIVOS DENTRO DE ASSETS. NÃO SERA UTILIZADO NO FINAL.
     * Essa função pega as menssagens que o usuario enviou que estão salvas em arquivo, realiza o processamento das menssagens
     * e salva o restultado compilando em intervalos de tempo de 1 hora o resultado do sentimento do usuario.
     * @author Jefferson C. Silva
     */
    public String executarLexico() {
        Cursor cur_msg = db.getUserMessages();  // Pega as msgs do usuario
        //cur_msg.moveToFirst();
        int saldo, saldoSentenca;
        if(cur_msg != null && cur_msg.moveToFirst()) {
            do {
                saldo = 0;
                String msg = cur_msg.getString(cur_msg.getColumnIndexOrThrow(UsrMsg.CL_MSG));
                msg = msg.toLowerCase();
                saldoSentenca = getSaldoSentenca(msg);
                String[] s = msg.split(" ");
                for (String i : s) {
                    if(i.isEmpty()) {
                        Log.d(tag, "string vazia.");
                        continue;
                    }
                    if(i.equals("?")){
                        Log.d(tag, "string com interrogaçao: " + i);
                        continue;
                    }
                    if(i.equals(" ")) {
                        Log.d(tag, "string com espao somente: " + i);
                        continue;
                    }
                    saldo += getSaldoPalavra(i);
                }
                LexicoResult lr = new LexicoResult();
                lr.setFrase(msg);
                lr.setData(formatData(System.currentTimeMillis()));
                if (saldoSentenca == 1) {
                    lr.setSentimento(Sentimento.POSITIVO);
                } else if (saldoSentenca == -1) {
                    lr.setSentimento(Sentimento.NEGATIVO);
                } else if (saldo >= 0) {
                    lr.setSentimento(Sentimento.POSITIVO);
                } else {
                    lr.setSentimento(Sentimento.NEGATIVO);
                }
                LexicoDb.insertDb(lr, this.context);
            }while (cur_msg.moveToNext());
        }
        cur_msg.close();
        db.apagarTbUsrMsg();

        // Anaiza o resultado do lexico no intervalo de tempo definido para que seja classificado como
        // positivo ou negativo. Essa analize apenas considera a quantidade de sentenças positivas e negativas
        // sendo que mais sentenças positivas no intervalo de tempo que foi analizada considera-se o sentimento como
        // positivo. Caso contrario negativo.
        /*Sentimento s = null;
        if(positivo >= negativo){
            s = Sentimento.POSITIVO;
        }else
            s = Sentimento.NEGATIVO;
        // Realiza a medida de consumo de redes
        //long bytes = this.nus.getTotalNetUsage(context);
        // Pega o intervalo de tempo no formato de string para armazenar o resultado do processamento.
        // Armazena intervalo de tempo o sentimento do usuario, a quantidade de bytes consumidos e a quantidade de dados utilizados no
        // intervalo.
        String last = this.db.getLastTimeNetUsage();
        String dateFormater = "dd-MM-yyyy hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormater);
        String now = sdf.format(System.currentTimeMillis());
        ResultadoFinal rf = new ResultadoFinal();
        rf.setDt_fim(now);
        rf.setDt_inicio(last);
        rf.setSentimento(s);
        rf.setBytes(bytes);
        db.insertDb(rf, context);
        */

        return "Lexico processado!";
    }

    /**
     * @param sentenca sentença a ser pesquisada no arquivo base.
     * @return saldo da sentença. Retorna 0 caso ela não tenha sido encotrada.
     * @author Leonardo Pereira - Adapdato para java/android por Jefferson C. Silva
     */
    private int getSaldoSentenca(String sentenca) {
        Cursor c = db.getTableSaldoSentenca(sentenca);
        if (c != null && c.moveToFirst()) {
            int i = c.getInt(0);
            c.close();
            return i;
        }

        if (c != null) {
            c.close();
        }
        return 0;
    }

    /**
     * Retorna um saldo positivo ou negativo para uma palavra seguindo a base de dados.
     * @param p - palavra que deve ser buscada.
     * @return 1 se palavra for positiva; -1 se a palavra for negativa; 0 se a palavra não for encotrada.
     */
    private int getSaldoPalavra(String p) {
        Cursor c = db.getSaldoPalavra(p);
        if (c != null && c.moveToFirst()) {
            int i = c.getInt(0);
            c.close();
            return i;
        }
        if (c != null) {
            c.close();
        }
        return 0;
    }

    /**
     * Chama o método dque retorna um vetor com os dados de consumo de rede.
     * @return vetor com tres posicoes onde: [0] retorna o tempo inicial; [1] retorna o tempo final; [2] retorna o somatorio dos dados (wifi + mobile)
     */
    public long[] pegarDadosRede() {
        return this.nus.getTotalNetUsage(context);
    }

    /**
     * Efetua a classificação do sentimento seguindo a seguinte regra: se tiver mais sentimentos posivos no intervalo dito, classifica como
     * positivo o intervalo de tempo. Se tiver mais negativos, classifica como negativo. Em seguida, salva esse resultado no banco de dados
     * (pois ja está implementado para ler do banco para mostrar na tela do app) e salva em arquivo txt em uma pasta a ser definida.
     * @param dados vetor que consiste em: dados[0] o tempo inicial do intervalo; dados[1] o tempo final do intervalo e dados[2] a quantidade
     *              total em bytes de dados consumidos pela rede.
     */
    public void classificaSentimentos(long[] dados){
        int positivos = db.getTotalSentimentos("Positivo");
        int negativos = db.getTotalSentimentos("Negativo");
        Log.d(tag, "Positivo: " + positivos + " -- Negativos: " + negativos);
        Sentimento s = (positivos >= negativos ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
        ResultadoFinal rf = new ResultadoFinal(dados[0], dados[1], dados[2],dados[3], s);
        this.db.insertDb(rf, this.context);
        Log.d(tag, "Sentimento classificado. RF: " + rf.toString());
    }

    /**
     * Formata a data dada em long para string seguindo o padrão.
     * @param d data em long seguindo o padrão Unix para data.
     * @return string convertida em formato legivel de data.
     */
    private String formatData(long d){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        return sdf.format(d);
    }

}
