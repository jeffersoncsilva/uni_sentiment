package com.projetos.redes.activities;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.projetos.redes.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ActivityRecebeConversaExportadaWhatsapp extends AppCompatActivity {

    private TextView ola;
    private ProgressBar carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebe_conversa_exportada_whatsapp);
        ola = findViewById(R.id.conteudo);
        carregando = findViewById(R.id.carregarResultado);
        Intent in = getIntent();
        new CarregaMensagensTask(ola, carregando, in).execute();
        ola.setMovementMethod(new ScrollingMovementMethod());
    }

    private class CarregaMensagensTask extends AsyncTask<Void, Void, Void>{
        private TextView ola;
        private ProgressBar carregar;
        private Intent intent;
        private StringBuilder sb = new StringBuilder();

        public CarregaMensagensTask(TextView _ola, ProgressBar _carregar, Intent in){
            ola = _ola;
            carregar = _carregar;
            intent = in;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ola.setVisibility(View.GONE);
            carregando.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ClipData data = intent.getClipData();
                for(int i = 0; i < data.getItemCount(); i++){
                    Uri uri = data.getItemAt(i).getUri();
                    InputStream stream = getContentResolver().openInputStream(uri);
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    BufferedReader bReader = new BufferedReader(reader);
                    int c=0;
                    while((c=bReader.read()) != -1){
                        sb.append((char)c);
                    }
                }
            }catch (Exception e){
                sb.append("Ocorreu um erro ao ler as mensagens.\n");
                sb.append("ERRO: " + e.getMessage()+"\n");
                sb.append("toString(): "+e.toString()+"\n");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //ola.setText(sb.toString());
            ola.setText("processamento concluido.");
            ola.setVisibility(View.VISIBLE);
            carregando.setVisibility(View.GONE);
        }
    }
}