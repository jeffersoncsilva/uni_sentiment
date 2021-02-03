package com.projetos.redes.fragments.tutoriais;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.projetos.redes.R;
import com.projetos.redes.Utils;

public class ConfiguraDiasAnterioresParaAnalisarFragment extends Fragment {

    private RadioGroup opcoesTempoExecucao;
    private String[] dias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tempo_minimo_execucao, container, false);
        opcoesTempoExecucao = v.findViewById(R.id.diasExecucao);
        dias = getResources().getStringArray(R.array.diasMinimosParaAnalisar);
        final Context con = getContext();
        for(int i = 0; i < dias.length; i++){
            RadioButton radioButton = new RadioButton(con);
            radioButton.setText(dias[i]);
            radioButton.setId(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            opcoesTempoExecucao.addView(radioButton, params);
        }

        opcoesTempoExecucao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                int qtdDia = pegarQuantidadeDiasEscolhido(i);
                edit.putInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, qtdDia);
                edit.commit();
            }
        });

        return v;
    }

    private int pegarQuantidadeDiasEscolhido(int i){
        switch (i){
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 7;
            case 4:
                return 14;
            default:
                return 1;
        }
    }
}
