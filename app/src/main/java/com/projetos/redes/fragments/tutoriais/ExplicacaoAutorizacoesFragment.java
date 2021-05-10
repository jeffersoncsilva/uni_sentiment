package com.projetos.redes.fragments.tutoriais;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.activities.MainActivity;

public class ExplicacaoAutorizacoesFragment extends Fragment implements View.OnClickListener {
    Button wifi, mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_autorizacoes, container, false);
        wifi = v.findViewById(R.id.autorizarWiFi);
        mobile = v.findViewById(R.id.autorizarDadosMoveis);
        wifi.setOnClickListener(this);
        mobile.setOnClickListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Utils.verificaPermissaoAcessoAosDadosTelefone(getContext())){
            wifi.setEnabled(false);
            wifi.setBackground(getContext().getDrawable(R.drawable.botao_padrao_desabilitado));
        }
        if(Utils.verificaSeTemPermisaoReadPhoneState(getContext())){
            mobile.setBackground(getContext().getDrawable(R.drawable.botao_padrao_desabilitado));
            mobile.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.autorizarWiFi:
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                break;
            case R.id.autorizarDadosMoveis:
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.PERMISSION_READ_STATE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(Utils.verificaSeTemPermisaoReadPhoneState(getContext())){
            mobile.setEnabled(false);
            mobile.setBackground(getContext().getDrawable(R.drawable.botao_padrao_desabilitado));
        }
    }
}