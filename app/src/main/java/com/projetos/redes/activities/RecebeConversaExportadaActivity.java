package com.projetos.redes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.Utils;
import com.projetos.redes.alerts.AcessoAoTelefoneAutorizacao;
import com.projetos.redes.alerts.AutorizacaoAcessoAosDadosTelefone;
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
                iniciaLexico(autor1.getText().toString());
            }
        });
        autor2 = findViewById(R.id.btAutor2);
        autor2.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iniciaLexico(autor2.getText().toString());
            }
        });

        Intent in = getIntent();
        new CarregaMensagensIntentTask(in, this, carregando, autor1, autor2, mensagensUsuarioRecycler, mensagensUsuarioAdapter).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.ic_help){
            Intent in = new Intent(getApplicationContext(), AjudaActivity.class);
            Bundle extra = new Bundle();
            extra.putString(AjudaActivity.AJUDA_KEY, getString(R.string.ajuda_tela_mensagens_exportada));
            in.putExtras(extra);
            startActivity(in);
        }else if(item.getItemId() == R.id.ic_contato){
            Intent contato = new Intent(getApplicationContext(), ContatoActivity.class);
            startActivity(contato);
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciaLexico(String nomeUsuario){
        if(temPermisoesNecessarias()){
            IniciaAnaliseLexico lexico = new IniciaAnaliseLexico(nomeUsuario,15);
            lexico.execute();
        }
    }

    private boolean temPermisoesNecessarias(){
        if(!Utils.verificaSeTemPermisaoReadPhoneState(getApplicationContext())){
            AcessoAoTelefoneAutorizacao acesso = new AcessoAoTelefoneAutorizacao();
            acesso.show(getSupportFragmentManager(), "Autorizacao de acesso ao telefone.");
            return false;
        }
        else if(!Utils.verificaPermissaoAcessoAosDadosTelefone(getApplicationContext())){
            AutorizacaoAcessoAosDadosTelefone aut = new AutorizacaoAcessoAosDadosTelefone();
            aut.show(getSupportFragmentManager(), "Autorização acesso aos dados.");
            return false;
        }
        return true;
    }

    private class IniciaAnaliseLexico extends AsyncTask<Void, Void, Void>{

        private final String autorMensagens;
        private final int mIntervalo;

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