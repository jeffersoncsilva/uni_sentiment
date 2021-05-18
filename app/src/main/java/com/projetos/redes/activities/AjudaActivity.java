package com.projetos.redes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.projetos.redes.R;

public class AjudaActivity extends AppCompatActivity {
    public static final String AJUDA_KEY = "ajuda_nome";
    private TextView txAjuda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        txAjuda = findViewById(R.id.txAjuda);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String ajuda = b.getString(AJUDA_KEY);
        txAjuda.setText(ajuda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("UniSentiment - Ajuda");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}