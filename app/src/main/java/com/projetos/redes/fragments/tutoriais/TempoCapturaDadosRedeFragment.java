package com.projetos.redes.fragments.tutoriais;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.alerts.AcessoAoTelefoneAutorizacao;
import com.projetos.redes.alerts.AutorizacaoAcessoAosDadosTelefone;

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
                String opcao = pegarOpcaoDeTempo(i);
                Toast.makeText(getContext(), opcao, Toast.LENGTH_SHORT).show();
                gravaOpcaoTempoEscolhida(i);
            }
        });
    }

    private String pegarOpcaoDeTempo(int i){
        switch (i){
            case 0:
                return " 15 Minutos.";
            case 1:
                return " 30 Minutos.";
            case 2:
                return " 45 Minutos.";
            case 3:
                return " 1 Hora.";
            case 4:
                return " 2 Horas.";
            case 5:
                return " 4 Horas.";
            case 6:
                return " 8 Horas.";
            default:
                return "";
        }
    }

    private void gravaOpcaoTempoEscolhida(int tempo){
        SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        int gravar;
        switch (tempo){
            case 0:
                gravar = 15;
                break;
            case 1:
                gravar = 30;
                break;
            case 2:
                gravar = 45;
                break;
            case 3:
                gravar = 60;
                break;
            case 4:
                gravar = 120;
                break;
            case 5:
                gravar = 240;
                break;
            case 6:
                gravar = 480;
                break;
            default:
                gravar = 1000;
        }
        edit.putInt(Utils.TEMPO_CAPTURA_REDE, gravar);
        edit.commit();
    }
}