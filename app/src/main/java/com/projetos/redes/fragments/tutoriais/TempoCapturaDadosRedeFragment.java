package com.projetos.redes.fragments.tutoriais;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.enums.MinutosCapturaDados;

public class TempoCapturaDadosRedeFragment extends Fragment {

    private RadioGroup opcoesTempoExecucao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tempo_captura_dados_rede, container, false);
        opcoesTempoExecucao = v.findViewById(R.id.tempoExecucao);
        preencheOpcoesRadios();
        configuraSelecaoItemOpcoes();
        return v;
    }

    private void preencheOpcoesRadios(){
        final Context con = getContext();
        String[] opcoes =  getResources().getStringArray(R.array.intervalos_captura_dados_rede);
        for(int i = 0; i < opcoes.length; i++){
            RadioButton radioButton = new RadioButton(con);
            radioButton.setText(opcoes[i]);
            radioButton.setId(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            opcoesTempoExecucao.addView(radioButton, params);
        }
    }

    private void configuraSelecaoItemOpcoes(){
        opcoesTempoExecucao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                MinutosCapturaDados min = MinutosCapturaDados.factory(i);
                gravaOpcaoTempoEscolhida(min);
            }
        });
    }

    private void gravaOpcaoTempoEscolhida(MinutosCapturaDados min){
        SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(Utils.TEMPO_CAPTURA_REDE, min.getId());
        edit.commit();
    }
}