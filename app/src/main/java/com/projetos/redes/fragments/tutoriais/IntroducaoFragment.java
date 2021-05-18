package com.projetos.redes.fragments.tutoriais;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projetos.redes.R;

public class IntroducaoFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_introducao, container, false);
        v.findViewById(R.id.bt_entrar_em_contato).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void enviarEmail(){
        Intent in = new Intent(Intent.ACTION_SENDTO);
        in.setData(Uri.parse("mailto:"));
        in.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_padrao));
        in.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contato_participar));
        if(in.resolveActivity(getContext().getPackageManager()) != null)
            startActivity(in);
        else
            Toast.makeText(getContext(), getString(R.string.aplicativoEmailNaoInstalado), Toast.LENGTH_LONG).show();
    }
}
