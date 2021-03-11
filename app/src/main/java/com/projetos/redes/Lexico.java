package com.projetos.redes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;

import java.util.List;

public class Lexico {
    private static final String tag = "AnalisadorLexico";
    private Context context;
    public BancoDeDados banco;
    private List<MensagemUsuario> mensagens;

    public Lexico(Context c, List<MensagemUsuario> msgs){
        this.context = c;
        this.mensagens = msgs;
        this.banco = new BancoDeDados(c);
    }

    public void executarLexico() {
        int saldoSomaPalavras, saldoFraseTotal;
        // limparTabelaResultado();
        for(MensagemUsuario mu : mensagens) {
            saldoSomaPalavras = pegarSaldoDaSomaDasPalavrasDaFrase(mu.getMensagem().toLowerCase());
            saldoFraseTotal = pegarSaldoFrase(mu.getMensagem().toLowerCase());
            int s = 0;
            if (saldoFraseTotal == 1) {
                s = 1;
            } else if (saldoFraseTotal == -1) {
                s = 2;
            } else if (saldoSomaPalavras >= 0) {
                s = 1;
            } else {
                s = 2;
            }
            ResultadoLexicoProcessado lr = new ResultadoLexicoProcessado(mu.getMensagem(), s, mu.getUtilidadeData().getHora(), mu.getUtilidadeData().getMinutos(), mu.getUtilidadeData().pegarDataSemHoras(), mu.getUtilidadeData());
            banco.insereResultadoLexicoProcessado(lr);
        }
    }

    /**
     * @param sentenca sentença a ser pesquisada no arquivo base.
     * @return saldo da sentença. Retorna 0 caso ela não tenha sido encotrada.
     * @author Leonardo Pereira - Adapdato para java/android por Jefferson C. Silva
     */
    private int pegarSaldoFrase(String sentenca) {
        Cursor c = banco.getTableSaldoSentenca(sentenca);
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

    private int pegarSaldoDaSomaDasPalavrasDaFrase(String frase){
        int saldoSomaPalavras = 0;
        if(frase == null || frase.isEmpty() || frase.equals(" ")) return 0;

        String[] s = frase.split(" ");
        for (String i : s) {
            if (i.isEmpty() || i.equals(" ") || i.equals("?")) {
                Log.d(tag, "string invalida." + i);
                continue;
            }
            saldoSomaPalavras += pegarSaldoPalavra(i);
        }
        return saldoSomaPalavras;
    }

    /**
     * Retorna um saldo positivo ou negativo para uma palavra seguindo a base de dados.
     * @param p - palavra que deve ser buscada.
     * @return 1 se palavra for positiva; -1 se a palavra for negativa; 0 se a palavra não for encotrada.
     */
    private int pegarSaldoPalavra(String p) {
        Cursor c = banco.getSaldoPalavra(p);
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
}