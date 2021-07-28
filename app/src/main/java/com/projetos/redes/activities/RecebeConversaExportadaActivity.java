package com.projetos.redes.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.Lexico;
import com.projetos.redes.Utils;
import com.projetos.redes.alerts.AcessoAoTelefoneAutorizacao;
import com.projetos.redes.alerts.AutorizacaoAcessoAosDadosTelefone;
import com.projetos.redes.enums.DiasAnterioresParaAnalise;
import com.projetos.redes.enums.MinutosCapturaDados;
import com.projetos.redes.task.CapturaDadosRede;
import com.projetos.redes.task.CarregaMensagensIntentTask;
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
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utils.identificadorUsuarioSalvo(this)) {
            setContentView(R.layout.activity_recebe_conversa_exportada_whatsapp);
            preferences = getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
            carregando = findViewById(R.id.carregarResultado);
            mensagensUsuarioRecycler = findViewById(R.id.mensagensUsuario);
            mensagensUsuarioRecycler.setLayoutManager(new LinearLayoutManager(this));
            mensagensUsuarioAdapter = new MostraMensagensAdapter(this);
            mensagensUsuarioRecycler.setAdapter(mensagensUsuarioAdapter);
            iniciaAnalise = findViewById(R.id.btIniciaAnalise);
            iniciaAnalise.setOnClickListener(view -> {
                podeIniciarLexico = true;
                realizaPreparativosLexico();
            });
            Intent in = getIntent();
            new CarregaMensagensIntentTask(in, this, carregando, iniciaAnalise, mensagensUsuarioRecycler, mensagensUsuarioAdapter).execute();
        }else{
            Toast.makeText(this, "Configure o identificador do usuario.", Toast.LENGTH_LONG).show();
            Intent start = new Intent(this, TutorialActivity.class);
            startActivity(start);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Recebido resultado pedido autorização.");
        if(podeIniciarLexico) {
            realizaPreparativosLexico();
        }
    }

    private void realizaPreparativosLexico(){
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
        mostraOpcaoEscolhaAutor();
    }

    private boolean temPermissaoDeAcessoAosDadosCelular(){
        return Utils.verificaPermissaoAcessoAosDadosTelefone(getApplicationContext());
    }

    private boolean temAutorizacaoDeAcessoAoTelefone(){
        return Utils.verificaSeTemPermisaoReadPhoneState(getApplicationContext());
    }

    @SuppressLint("StaticFieldLeak")
    private class IniciaAnaliseLexico extends AsyncTask<Void, Void, Void>{

        private final MensagemUsuario usuario;

        private final DiasAnterioresParaAnalise diasAnalise;
        private final Context mContext;

        public IniciaAnaliseLexico(MensagemUsuario user, MinutosCapturaDados min, DiasAnterioresParaAnalise dias, Context context) {
            this.usuario = user;
            this.mContext = context;
            diasAnalise = dias;
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
            // primeiraMensagemEnviada
            List<MensagemUsuario> mensagensFiltradas = filtraMensagensAutor(usuario.getAutor(), diasAnalise.getValor());
            Lexico lx = new Lexico(mContext, mensagensFiltradas);
            lx.executarLexico();
            CapturaDadosRede capitura = new CapturaDadosRede(mContext);
            capitura.executa(mensagensFiltradas);
            //redeTask.doIt();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
            finish();
        }

        /*/ Para limitar a busca
        private UtilidadeData pegarDataInicioCaptura(MensagemUsuario pme){
            long dd = System.currentTimeMillis() - (diasAnalise.getValor() * 6000000);
            UtilidadeData d = new UtilidadeData(dd);
            UtilidadeData dpme = pme.getUtilidadeData();
            if(dpme.dataEmMilisegundos() < d.dataEmMilisegundos())
                return dpme;
            return d;
        }*/

        private List<MensagemUsuario> filtraMensagensAutor(String autor, int dias){
            List<MensagemUsuario> msg = new ArrayList<>();
            final long tempo = System.currentTimeMillis() - (dias * 3600000 * 24);
            Log.d(TAG, "TEMPO: " +tempo);
            for(MensagemUsuario mu : mensagensUsuarioAdapter.getMensagens()){
                if(mu.getAutor().equals(autor)){
                    long tt = mu.getUtilidadeData().dataEmMilisegundos();
                    Log.d("timestamp", "TT: " + tt + "tempo: " + tempo);
                    if(tt >= tempo)
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
        builder.setItems(autores, (dialog, which) -> {
            MensagemUsuario escolhido = mensagensUsuarioAdapter.getAutores().get(which);
            verificaIntervaloFoiSalvo(escolhido);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Verifica se o usuario salvou o intervalo para a analise de mensagem (e captura de uso de internet).
    private void verificaIntervaloFoiSalvo(final MensagemUsuario mu){
        if(preferences.contains(Utils.TEMPO_CAPTURA_REDE)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            MinutosCapturaDados minutos = MinutosCapturaDados.factory(preferences.getInt(Utils.TEMPO_CAPTURA_REDE, 15));
            builder.setTitle("Deseja alterar o intervalo de captura de dados de "+minutos.toString()+"?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mostraOpcoesIntervaloCapturaDados(mu);
                }
            });
            builder.setNegativeButton("Não", (dialogInterface, i) -> verificaSeSalvoLimiteDiasAnalise(mu, MinutosCapturaDados.factory(preferences.getInt(Utils.TEMPO_CAPTURA_REDE, 15))));
            AlertDialog dialog = builder.create();
            dialog.show();
        }else
            mostraOpcoesIntervaloCapturaDados(mu);
    }

    // Usuario escolher o intervalo de analise das mensagens (e captura de uso de internet).
    private void mostraOpcoesIntervaloCapturaDados(final MensagemUsuario mu){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] tempos = getResources().getStringArray(R.array.intervalos_captura_dados_rede);
            builder.setTitle(R.string.tempo_execucao);
            builder.setItems(tempos, (dialog, i) -> {
                MinutosCapturaDados min = MinutosCapturaDados.factory(i);
                preferences.edit().putInt(Utils.TEMPO_CAPTURA_REDE, min.getId()).apply();
                verificaSeSalvoLimiteDiasAnalise(mu, min);
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Verifica se o usuario escolheu a quantidade de dias atras para analise das mensagens.
    private void verificaSeSalvoLimiteDiasAnalise(final MensagemUsuario mu, final MinutosCapturaDados intervaloCapturaRede){
        if(preferences.contains(Utils.DIAS_ANTERIOR_PARA_ANALISAR)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            DiasAnterioresParaAnalise dias = DiasAnterioresParaAnalise.factory(preferences.getInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, 0));
            builder.setTitle("Deseja alterar o intervalo de dias para análise de "+dias.toString()+"?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                // se refere a quantidade de dias anteriores que deve ser considerado na alise.
                alterarDiasParaAnalise(mu, intervaloCapturaRede);
            });
            builder.setNegativeButton("Não", (dialogInterface, i) -> iniciaAnlaliseLexico(mu, MinutosCapturaDados.factory(preferences.getInt(Utils.TEMPO_CAPTURA_REDE, 0)), DiasAnterioresParaAnalise.factory(preferences.getInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, 1))));
            AlertDialog dialog = builder.create();
            dialog.show();
        }else
            alterarDiasParaAnalise(mu, intervaloCapturaRede);
    }

    // Usuario escolhe quantos dias atras para analisar as mensagens.
    private void alterarDiasParaAnalise(final MensagemUsuario mu, final MinutosCapturaDados minCaptrua){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] tempos = getResources().getStringArray(R.array.diasMinimosParaAnalisar);
        builder.setTitle(R.string.diasParaAnalise);
        builder.setItems(tempos, (dialog, i) -> {
            DiasAnterioresParaAnalise dias = DiasAnterioresParaAnalise.factory(i);
            preferences.edit().putInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, dias.getId()).apply();
            iniciaAnlaliseLexico(mu, minCaptrua, dias);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void iniciaAnlaliseLexico(final MensagemUsuario msg, MinutosCapturaDados min, DiasAnterioresParaAnalise dias){
        IniciaAnaliseLexico analise = new IniciaAnaliseLexico(msg, min, dias, getApplicationContext());
        analise.execute();
    }

    private String[] getAutoresMensagens(){
        String[] aut = new String[mensagensUsuarioAdapter.getAutores().size()];
        for(int i = 0 ; i < mensagensUsuarioAdapter.getAutores().size(); i++)
            aut[i] = mensagensUsuarioAdapter.getAutores().get(i).getAutor();
        return aut;
    }
}