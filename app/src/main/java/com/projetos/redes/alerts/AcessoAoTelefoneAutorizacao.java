package com.projetos.redes.alerts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.R;

public class AcessoAoTelefoneAutorizacao extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.permisao_acesso_telefone));
        alert.setMessage(getString(R.string.permisao_acesso_telefone_descricao));
        alert.setPositiveButton(getString(R.string.permission_authorize), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.PERMISSION_READ_STATE);
            }
        });
       return alert.create();
    }
}
