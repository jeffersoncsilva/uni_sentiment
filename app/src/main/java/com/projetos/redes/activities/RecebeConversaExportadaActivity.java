package com.projetos.redes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.task.CarregaMensagensIntentTask;
import com.projetos.redes.Lexico;
import com.projetos.redes.R;
import com.projetos.redes.adapters.MostraMensagensAdapter;
import com.projetos.redes.modelos.MensagemUsuario;

import java.util.ArrayList;
import java.util.List;

public class RecebeConversaExportadaActivity extends AppCompatActivity {
    protected static final String TAG = "ConversaExportadaAct";
    private ProgressBar carregando;
    private RecyclerView mensagensUsuarioRecycler;
    private Button autor1, autor2;
    private MostraMensagensAdapter mensagensUsuarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebe_conversa_exportada_whatsapp);
        carregando = findViewById(R.id.carregarResultado);
        mensagensUsuarioRecycler = findViewById(R.id.mensagensUsuario);
        mensagensUsuarioRecycler.setLayoutManager(new LinearLayoutManager(this));
        mensagensUsuarioAdapter = new MostraMensagensAdapter(this);
        mensagensUsuarioRecycler.setAdapter(mensagensUsuarioAdapter);
        autor1 = findViewById(R.id.btAutor1);
        autor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IniciaAnaliseLexico lexico = new IniciaAnaliseLexico(autor1.getText().toString(),15);
                lexico.execute();
            }
        });
        autor2 = findViewById(R.id.btAutor2);
        autor2.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new IniciaAnaliseLexico(autor2.getText().toString(),15).execute();
            }
        });

        Intent in = getIntent();
        new CarregaMensagensIntentTask(in, this, carregando, autor1, autor2, mensagensUsuarioRecycler, mensagensUsuarioAdapter).execute();
    }


    private class IniciaAnaliseLexico extends AsyncTask<Void, Void, Void>{

        private String autorMensagens;
        private int mIntervalo;

        public IniciaAnaliseLexico(String nomeUsuario, int intervalo) {
            this.autorMensagens = nomeUsuario;
            mIntervalo = intervalo;
        }

        @Override
        protected void onPreExecute() {
            autor1.setVisibility(View.GONE);
            autor2.setVisibility(View.GONE);
            mensagensUsuarioRecycler.setVisibility(View.GONE);
            carregando.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<MensagemUsuario> mensagensFiltradas = filtraMensagensAutor(autorMensagens);
            Lexico lx = new Lexico(getApplicationContext(), mensagensFiltradas, mIntervalo);

            lx.executarLexico();
            lx.montaResultadoFinal();

            Log.d(TAG, "processamendo concluido.");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        }

        private List<MensagemUsuario> filtraMensagensAutor(String autor){
            List<MensagemUsuario> msg = new ArrayList<>();
            for(MensagemUsuario mu : mensagensUsuarioAdapter.getMensagens()){
                if(mu.getAutor().equals(autor)){
                    msg.add(mu);
                }
            }
            return msg;
        }
    }
}