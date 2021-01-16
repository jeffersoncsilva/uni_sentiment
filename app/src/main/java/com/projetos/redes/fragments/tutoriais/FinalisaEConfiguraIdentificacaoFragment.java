package com.projetos.redes.fragments.tutoriais;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.activities.MainActivity;

public class FinalisaEConfiguraIdentificacaoFragment  extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finalisa_e_configura_identificacao, container, false);
        Button finaliza = v.findViewById(R.id.btFinaliza);
        finaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravaConfiguracaoEIniciaIntent();
            }
        });
        return v;
    }

    private void gravaConfiguracaoEIniciaIntent(){
        SharedPreferences prefs = getContext().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Utils.JA_VIU_TUTORIAL, true);
        edit.commit();
        Intent in = new Intent(getContext(), MainActivity.class);
        startActivity(in);
    }

}
