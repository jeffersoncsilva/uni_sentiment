package com.projetos.redes.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.Lexico;
import com.projetos.redes.Utils;
import com.projetos.redes.alerts.AcessoAoTelefoneAutorizacao;
import com.projetos.redes.alerts.AutorizacaoAcessoAosDadosTelefone;
import com.projetos.redes.task.CapturaDadosRedeTask;
import com.projetos.redes.task.CarregaMensagensIntentTask;
import com.projetos.redes.R;
import com.projetos.redes.adapters.MostraMensagensAdapter;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.utilidades.UtilidadeData;

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
        private final int intCapturaMensagens;
        private final int qtdDiasAnterioresParaAnalise;
        private final Context mContext;

        public IniciaAnaliseLexico(MensagemUsuario user, int intervalo, int diasAnteriores, Context context) {
            this.usuario = user;
            intCapturaMensagens = intervalo;
            this.mContext = context;
            qtdDiasAnterioresParaAnalise = diasAnteriores;
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
            List<MensagemUsuario> mensagensFiltradas = filtraMensagensAutor(usuario.getAutor(), qtdDiasAnterioresParaAnalise);
            Log.d(TAG, "Iniciando execução da task que realiza a analise lexica.");
            Lexico lx = new Lexico(mContext, mensagensFiltradas);
            lx.executarLexico();
            // Pega a data da primeira mensagem.
            CapturaDadosRedeTask redeTask = new CapturaDadosRedeTask(mContext, mensagensFiltradas);
            Log.d(TAG, "Iniciando captura de dados da internet.");
            redeTask.doIt();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
            finish();
        }

        // Para limitar a busca
        private UtilidadeData pegarDataInicioCaptura(MensagemUsuario pme){
            long dd = System.currentTimeMillis() - (qtdDiasAnterioresParaAnalise * 6000000);
            UtilidadeData d = new UtilidadeData(dd);
            UtilidadeData dpme = pme.getUtilidadeData();
            if(dpme.dataEmMilisegundos() < d.dataEmMilisegundos())
                return dpme;
            return d;
        }

        private List<MensagemUsuario> filtraMensagensAutor(String autor, int dias){
            List<MensagemUsuario> msg = new ArrayList<>();
            final long tempo = System.currentTimeMillis() - (dias * 3600000 * 24);
            for(MensagemUsuario mu : mensagensUsuarioAdapter.getMensagens()){
                if(mu.getAutor().equals(autor)){
                    if(mu.getUtilidadeData().dataEmMilisegundos() >= tempo)
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
                verificaIntervaloFoiSalvo(escolhido);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Verifica se o usuario salvou o intervalo para a analise de mensagem (e captura de uso de internet).
    private void verificaIntervaloFoiSalvo(final MensagemUsuario mu){
        final SharedPreferences prefs = getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
        if(prefs.contains(Utils.TEMPO_CAPTURA_REDE)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String tempo = pegaTempoCapturaSalvo(prefs);
            builder.setTitle("Deseja alterar o intervalo de "+tempo+" dias anteriores de análise?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mostraOpcoesIntervaloCapturaDados(mu);
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    verificaSeSalvoLimiteDiasAnalise(mu, prefs.getInt(Utils.TEMPO_CAPTURA_REDE, 15));
                }
            });
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
            builder.setItems(tempos, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    verificaSeSalvoLimiteDiasAnalise(mu, pegarIntervaloCapturaRede(i));
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Verifica se o usuario escolheu a quantidade de dias atras para analise das mensagens.
    private void verificaSeSalvoLimiteDiasAnalise(final MensagemUsuario mu, final int intervaloCapturaRede){
        final SharedPreferences prefs = getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
        if(prefs.contains(Utils.DIAS_ANTERIOR_PARA_ANALISAR)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String tempo = pegaQuantidadeDiasSalvo(prefs);
            builder.setTitle("Deseja alterar o intervalo de dias para análise de "+tempo+"?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // se refere a quantidade de dias anteriores que deve ser considerado na alise.
                    alterarDiasParaAnalise(mu, intervaloCapturaRede);
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    iniciaAnlaliseLexico(mu, prefs.getInt(Utils.TEMPO_CAPTURA_REDE, 15), prefs.getInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, 1));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else
            alterarDiasParaAnalise(mu, intervaloCapturaRede);
    }

    // Usuario escolhe quantos dias atras para analisar as mensagens.
    private void alterarDiasParaAnalise(final MensagemUsuario mu, final int intRede){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] tempos = getResources().getStringArray(R.array.diasMinimosParaAnalisar);
        builder.setTitle(R.string.diasParaAnalise);
        builder.setItems(tempos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Saber se ele escolheu 1, 2, 4, 7 ou 14 dias.
                int diasEscolhidos = pegarDiasEscolhidos(i);
                iniciaAnlaliseLexico(mu, intRede, diasEscolhidos);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int pegarDiasEscolhidos(int i){
        switch (i){
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 7;
            case 4:
                return 14;
            default:
                return 1;
        }
    }

    private void iniciaAnlaliseLexico(final MensagemUsuario msg, int intervaloCapturaRede, int limiteDiasAnalise){
        IniciaAnaliseLexico analise = new IniciaAnaliseLexico(msg, intervaloCapturaRede, limiteDiasAnalise, getApplicationContext());
        analise.execute();
    }

    private int pegarIntervaloCapturaRede(int i){
        switch (i){
            case 0:
                return 15;
            case 1:
                return 30;
            case 2:
                return  45;
            case 3:
                return 60;
            case 4:
                return 120;
            case 5:
                return 240;
            default:
                return 480;
        }
    }

    private String pegaTempoCapturaSalvo(SharedPreferences p){
        int t = p.getInt(Utils.TEMPO_CAPTURA_REDE, 15);
        switch (t){
            case 15:
                return " 15 Minutos";
            case 30:
                return " 30 Minutos";
            case 45:
                return " 45 Minutos";
            case 60:
                return " 1 Hora";
            case 120:
                return " 2 Horas";
            case 240:
                return " 4 Horas";
            default:
                return " 8 Horas";
        }
    }

    private String pegaQuantidadeDiasSalvo(SharedPreferences sp){
        int dia = sp.getInt(Utils.DIAS_ANTERIOR_PARA_ANALISAR, 1);
        switch (dia){
            case 1:
                return "2 dias";
            case 2:
                return "4 dias";
            case 3:
                return "1 semana";
            case 4:
                return "2 semanas";
            default:
                return "1 dia";
        }
    }

    private String[] getAutoresMensagens(){
        String[] aut = new String[mensagensUsuarioAdapter.getAutores().size()];
        for(int i = 0 ; i < mensagensUsuarioAdapter.getAutores().size(); i++)
            aut[i] = mensagensUsuarioAdapter.getAutores().get(i).getAutor();
        return aut;
    }
}