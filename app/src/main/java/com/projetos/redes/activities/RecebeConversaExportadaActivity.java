package com.projetos.redes.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.Utils;
import com.projetos.redes.alerts.AcessoAoTelefoneAutorizacao;
import com.projetos.redes.alerts.AutorizacaoAcessoAosDadosTelefone;
import com.projetos.redes.services.BuscadorConsumoInternet;
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
    private Button iniciaAnalise;
    private MostraMensagensAdapter mensagensUsuarioAdapter;
    private boolean podeIniciarLexico = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebe_conversa_exportada_whatsapp);
        carregando = findViewById(R.id.carregarResultado);
        mensagensUsuarioRecycler = findViewById(R.id.mensagensUsuario);
        mensagensUsuarioRecycler.setLayoutManager(new LinearLayoutManager(this));
        mensagensUsuarioAdapter = new MostraMensagensAdapter(this);
        mensagensUsuarioRecycler.setAdapter(mensagensUsuarioAdapter);
        iniciaAnalise = findViewById(R.id.btIniciaAnalise);
        iniciaAnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                podeIniciarLexico = true;
                realizaPreparativosLexico();
            }
        });

        Intent in = getIntent();
        new CarregaMensagensIntentTask(in, this, carregando, iniciaAnalise, mensagensUsuarioRecycler, mensagensUsuarioAdapter).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "podeIniciarLexico: "+podeIniciarLexico);
        if(podeIniciarLexico){
            realizaPreparativosLexico();
        }
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
        }else if(item.getItemId() == R.id.tutorial){
            Intent in = new Intent(getApplicationContext(), TutorialActivity.class);
            Utils.DesativarPularTutorial(getApplicationContext());
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Recebido resultado pedido autorização.");
        if(podeIniciarLexico) {
            realizaPreparativosLexico();
        }
    }

    private void realizaPreparativosLexico(){
        Log.d(TAG, "Realizando preparativos do lexico!");
        if(!temPermissaoDeAcessoAosDadosCelular()){
            AutorizacaoAcessoAosDadosTelefone aut = new AutorizacaoAcessoAosDadosTelefone();
            aut.show(getSupportFragmentManager(), "Autorização acesso aos dados.");
            return;
        }

        if(!temAutorizacaoDeAcessoAoTelefone()){
            AcessoAoTelefoneAutorizacao acesso = new AcessoAoTelefoneAutorizacao();
            acesso.show(getSupportFragmentManager(), "Autorizacao de acesso ao telefone.");
            return;
        }

        Log.d(TAG, "OK, autorizações obitida para o lexico.");

        mostraOpcaoEscolhaAutor();

    }

    private boolean temPermissaoDeAcessoAosDadosCelular(){
        return Utils.verificaPermissaoAcessoAosDadosTelefone(getApplicationContext());
    }

    private boolean temAutorizacaoDeAcessoAoTelefone(){
        return Utils.verificaSeTemPermisaoReadPhoneState(getApplicationContext());
    }

    private class IniciaAnaliseLexico extends AsyncTask<Void, Void, Void>{

        private final MensagemUsuario usuario;
        private final int mIntervalo;
        private final Context mContext;

        public IniciaAnaliseLexico(MensagemUsuario user, int intervalo, Context context) {
            this.usuario = user;
            mIntervalo = intervalo;
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            iniciaAnalise.setVisibility(View.GONE);
            mensagensUsuarioRecycler.setVisibility(View.GONE);
            carregando.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MensagemUsuario primeiraMensagemEnviada = mensagensUsuarioAdapter.getMensagens().get(0);
            List<MensagemUsuario> mensagensFiltradas = filtraMensagensAutor(usuario.getAutor());
            Lexico lx = new Lexico(getApplicationContext(), mensagensFiltradas, mIntervalo);
            lx.executarLexico();

            BuscadorConsumoInternet consumoInternet = new BuscadorConsumoInternet(mContext);
            consumoInternet.salvarConsumoDataInicialAteAtualNoIntervalo(primeiraMensagemEnviada.getData(), mIntervalo);

            lx.montaResultadoFinal();

            Log.d(TAG, "processamendo concluido.");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
            finish();
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

    private void mostraOpcaoEscolhaAutor(){
        String[] autores = getAutoresMensagens();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.escolha_nome));
        builder.setItems(autores, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MensagemUsuario escolhido = mensagensUsuarioAdapter.getAutores().get(which);
                mostraOpcoesIntervaloCapturaDados(escolhido);
                Toast.makeText(getBaseContext(), "Escolhido " + escolhido.getAutor(), Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostraOpcoesIntervaloCapturaDados(final MensagemUsuario mu){
        String[] tempos = getResources().getStringArray(R.array.intervalos_captura_dados_rede);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tempo_execucao);
        builder.setItems(tempos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                int intervalo = (i*15) + 15;
                Toast.makeText(getApplicationContext(), "Intervalo de capura de mensagem: " + intervalo + " minutos.", Toast.LENGTH_SHORT).show();
                IniciaAnaliseLexico analise = new IniciaAnaliseLexico(mu, intervalo, getApplicationContext());
                analise.execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String[] getAutoresMensagens(){
        String[] aut = new String[mensagensUsuarioAdapter.getAutores().size()];
        for(int i = 0 ; i < mensagensUsuarioAdapter.getAutores().size(); i++)
            aut[i] = mensagensUsuarioAdapter.getAutores().get(i).getAutor();
        return aut;
    }
}