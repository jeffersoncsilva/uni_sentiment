package com.projetos.redes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.modelos.ResultadoFinalLexico;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;
import com.projetos.redes.modelos.UsoDeInternet;
import com.projetos.redes.services.BuscadorConsumoInternet;
import com.projetos.redes.utilidades.Data;

import java.util.List;

public class Lexico {
    private static final String tag = "AnalisadorLexico";
    private final Context context;
    public BancoDeDados banco;
    private final BuscadorConsumoInternet nus;
    private final List<MensagemUsuario> mensagens;
    private final int mIntervalo;

    public Lexico(Context c, List<MensagemUsuario> msgs, int intervalo) {
        this.context = c;
        this.banco = new BancoDeDados(c);
        this.nus = new BuscadorConsumoInternet(this.banco);
        this.mensagens = msgs;
        this.mIntervalo = intervalo * 60000;
    }

    public void executarLexico() {
        int saldoSomaPalavras, saldoFraseTotal;
        // limparTabelaResultado();
        for(MensagemUsuario mu : mensagens) {
            saldoSomaPalavras = pegarSaldoDaSomaDasPalavrasDaFrase(mu.getMensagem().toLowerCase());
            saldoFraseTotal = pegarSaldoFrase(mu.getMensagem().toLowerCase());
            Sentimento s;
            if (saldoFraseTotal == 1) {
                s = Sentimento.POSITIVO;
            } else if (saldoFraseTotal == -1) {
                s = Sentimento.NEGATIVO;
            } else if (saldoSomaPalavras >= 0) {
                s = Sentimento.POSITIVO;
            } else {
                s = Sentimento.NEGATIVO;
            }
            ResultadoLexicoProcessado lr = new ResultadoLexicoProcessado(mu.getData(), mu.getMensagem(), s);
            banco.insereResultadoLexicoProcessado(lr);
        }
    }

    private void limparTabelaResultado() {
        banco.limparTabelaLexicoProcessado();
    }

    public void montaResultadoFinal(){
        List<UsoDeInternet> usoInternet = banco.pegarDadosUsoInternet();
        List<ResultadoLexicoProcessado> lp = banco.pegarResultadoLexico();
        for(UsoDeInternet uso : usoInternet){
            Data inicio = uso.getInicio();
            Data fim = uso.getFim();
            int positivo = 0, negativo = 0;
            boolean temMsg = false;
            for(int i = 0;i < lp.size(); i++){
                // Verifica se o resultado esta dentro do intervalo especificado,
                // caso esteja, tem mensagem nesse intervalo de coleta.
                if(lp.get(i).getDate().dataEmMilisegundos() > inicio.dataEmMilisegundos()
                   && lp.get(i).getDate().dataEmMilisegundos() < fim.dataEmMilisegundos()){
                    if(lp.get(i).getSentimento().equals(Sentimento.POSITIVO))
                        positivo++;
                    else
                        negativo++;
                    lp.remove(i);
                    temMsg = true;
                }
            }
            if(temMsg) {
                Sentimento s = (positivo > negativo ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
                ResultadoFinalLexico rf = new ResultadoFinalLexico(inicio, fim, uso, s, mIntervalo);
                banco.insereResultadoFinalLexico(rf);
            }
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
