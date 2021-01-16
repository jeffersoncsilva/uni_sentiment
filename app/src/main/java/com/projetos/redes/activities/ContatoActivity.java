package com.projetos.redes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.projetos.redes.R;

public class ContatoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        findViewById(R.id.btContatoEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("UniSentiment - Contato");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void enviarEmail(){
        Intent in = new Intent(Intent.ACTION_SENDTO);
        in.setData(Uri.parse("mailto:"));
        in.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_padrao));
        in.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contato_participar));
        if(in.resolveActivity(getPackageManager()) != null)
            startActivity(in);
        else
            Toast.makeText(getApplicationContext(), getString(R.string.aplicativoEmailNaoInstalado), Toast.LENGTH_LONG).show();
    }
}