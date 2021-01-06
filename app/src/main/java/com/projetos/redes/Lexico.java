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

import java.util.List;

public class Lexico {
    private static final String tag = "AnalisadorLexico";
    private Context context;
    public BancoDeDados banco;
    private BuscadorConsumoInternet nus;
    private List<MensagemUsuario> mensagens;
    private int mIntervalo;

    public Lexico(Context c, List<MensagemUsuario> msgs, int intervalo) {
        this.context = c;
        this.banco = new BancoDeDados(c);
        this.nus = new BuscadorConsumoInternet(this.banco);
        this.mensagens = msgs;
        this.mIntervalo = intervalo * 60000;
    }

    public void executarLexico() {
        int saldoSomaPalavras, saldoFraseTotal;
        limparTabelaResultado();
        for(MensagemUsuario mu : mensagens) {
            saldoSomaPalavras = pegarSaldoDaSomaDasPalavrasDaFrase(mu.getMensagem().toLowerCase());
            saldoFraseTotal = pegarSaldoFrase(mu.getMensagem().toLowerCase());
            Sentimento s = null;
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
        int positivos = 0, negativos = 0;
        List<ResultadoLexicoProcessado> resultados = banco.pegarResultadoLexico();
        ResultadoLexicoProcessado ultimoContado = resultados.get(0);
        final BuscadorConsumoInternet buscadorConsumoInternet = new BuscadorConsumoInternet(context);
        for(ResultadoLexicoProcessado rp : resultados){
            if(intervaloAcabou(rp, ultimoContado)){
                classificaIntervalo(positivos, negativos, rp, ultimoContado, buscadorConsumoInternet);
                positivos = 0;
                negativos = 0;
                ultimoContado = rp;
            }
            if(rp.getSentimento().equals(Sentimento.POSITIVO))
                positivos++;
            else
                negativos++;
        }
    }

    private boolean intervaloAcabou(ResultadoLexicoProcessado rp, ResultadoLexicoProcessado ultimoContado){
        return rp.getDate().dataEmMilisegundos() > ultimoContado.getDate().dataEmMilisegundos() + mIntervalo;
    }

    private void classificaIntervalo(int positivos, int negativos, ResultadoLexicoProcessado primeiroResultado, ResultadoLexicoProcessado segundoResultado, BuscadorConsumoInternet buscadorUsoInternet){
        Sentimento s = (positivos >= negativos ? Sentimento.POSITIVO : Sentimento.NEGATIVO);
        UsoDeInternet uso = buscadorUsoInternet.pegarConsumoNoIntervalo(segundoResultado.getDate(), primeiroResultado.getDate(), mIntervalo);
        ResultadoFinalLexico rf = new ResultadoFinalLexico(segundoResultado.getDate(), primeiroResultado.getDate(), uso, s);
        banco.insereResultadoFinalLexico(rf);
        banco.insereDadosDeRede(uso);
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
