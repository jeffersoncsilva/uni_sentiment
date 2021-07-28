package com.projetos.redes.fragments.tutoriais;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.activities.MainActivity;

public class FinalisaEConfiguraIdentificacaoFragment  extends Fragment {
    private boolean mainActivityAtiva = false;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finalisa_e_configura_identificacao, container, false);
        Button finaliza = v.findViewById(R.id.btFinaliza);
        final TextInputEditText identification = v.findViewById(R.id.identificacao);
        identification.setText(pegarCodigoIdentificacao());
        finaliza.setOnClickListener(view -> gravaConfiguracaoEIniciaIntent(identification));
        return v;
    }

    private String pegarCodigoIdentificacao(){
         String str = "";
         try{
             SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
             str = prefs.getString(Utils.ID_USUARIO, "");
         }catch (NullPointerException ex){
             ex.printStackTrace();
         }
         return str;
    }

    private void gravaConfiguracaoEIniciaIntent(TextInputEditText id){
        try{
            if(!id.getText().toString().isEmpty()) {
                String idS = id.getText().toString();
                SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Utils.JA_VIU_TUTORIAL, true);
                edit.putString(Utils.ID_USUARIO, idS);
                edit.apply();
                if(!mainActivityAtiva) {
                    Intent in = new Intent(getContext(), MainActivity.class);
                    startActivity(in);
                }
                getActivity().finish();
            }else{
                Toast.makeText(getContext(),"Insira o identificador para poder prosseguir!", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void setMainActivityEnabled(boolean b){
         mainActivityAtiva = b;
    }
}