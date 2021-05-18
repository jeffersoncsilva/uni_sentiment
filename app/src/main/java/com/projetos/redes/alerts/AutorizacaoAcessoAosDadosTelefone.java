package com.projetos.redes.alerts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.projetos.redes.R;
import com.projetos.redes.activities.MainActivity;

public class AutorizacaoAcessoAosDadosTelefone extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.autorizacao_acesso_dados_de_uso));
        alert.setMessage(getString(R.string.autorizacao_acesso_dados_de_uso_descricao));
        alert.setPositiveButton(getString(R.string.permission_authorize), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        });
        return alert.create();
    }
}
