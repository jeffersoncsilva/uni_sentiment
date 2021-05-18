package com.projetos.redes.task;

import android.content.Context;
import android.util.Log;

import com.projetos.redes.Utils;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.enums.MinutosCapturaDados;
import com.projetos.redes.modelos.ConsumoInternet;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.services.BuscaConsumoInternet;
import com.projetos.redes.utilidades.UtilidadeData;

import java.util.Date;
import java.util.List;

public class CapturaDadosRede {
    private BancoDeDados banco;
    private Context mContext;
    private BuscaConsumoInternet buscador;

    public CapturaDadosRede(Context context){
        banco = new BancoDeDados(context);
        buscador = new BuscaConsumoInternet(context);
        this.mContext = context;
    }

    public void executa(List<MensagemUsuario> msgs) {
        MinutosCapturaDados intervaloEmMinutos = MinutosCapturaDados.factory(mContext.getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE).getInt(Utils.TEMPO_CAPTURA_REDE, 15));
        long intervaloEmMilissegundos = intervaloEmMinutos.getValor() * 60000;
        Log.d("CapturaIntervaloRede", "Qtd mensagens: " + msgs.size());
        for (MensagemUsuario mu : msgs) {
            long inicio = mu.getUtilidadeData().dataEmMilisegundos();
            long fim = inicio + intervaloEmMilissegundos;
            ConsumoInternet consumo = banco.temInconsumoNesseIntervalo(mu.getUtilidadeData().dataEmMilisegundos());
            if (consumo == null) {
                consumo = new ConsumoInternet(buscador.pegarConsumoWiFi(inicio, fim), buscador.pegarConsumoMobile(inicio, fim), inicio, fim);
                banco.insereNovoConsumo(consumo);
            } else {
                consumo.setWifi(buscador.pegarConsumoWiFi(inicio, fim));
                consumo.setMobile(buscador.pegarConsumoMobile(inicio, fim));
                banco.atualizaConsumo(consumo);
            }
        }
    }
}
