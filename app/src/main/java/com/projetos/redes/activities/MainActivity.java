package com.projetos.redes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projetos.redes.R;
import com.projetos.redes.alerts.ConfigurationApp;
import com.projetos.redes.services.MyAccessibilitiService;
import com.projetos.redes.services.PopulateDatabaseService;
import com.projetos.redes.worker.LexicoWorker;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag = "LexicoApp";
    public static final int PERMISSION_READ_STATE = 1234;
    public static final String LEXICO_CONFIG = "network_configs";
    public static final String LEXICO_TIME="time";
    private Button btInicaAplicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_VerDadosRedes).setOnClickListener(this);
        findViewById(R.id.btResultadoProcessamentoLexico).setOnClickListener(this);
        findViewById(R.id.btResultadoFinalClassificado).setOnClickListener(this);
        btInicaAplicacao = findViewById(R.id.btIniciaAplicacao);
        btInicaAplicacao.setOnClickListener(this);

        findViewById(R.id.btHelpUsoInternet).setOnClickListener(this);
        findViewById(R.id.btHelpResultadoLexico).setOnClickListener(this);
        findViewById(R.id.btHelpResultadoFinal).setOnClickListener(this);
        findViewById(R.id.btHelpInicioParadaAplicacao).setOnClickListener(this);

        /*final boolean acessibilitService = MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(MainActivity.this));
        final boolean workerActive = LexicoWorker.WorkActive(this);
        if(!acessibilitService && !workerActive)
            btInicaAplicacao.setText(getText(R.string.btIniciarAplicacao));
        else
            btInicaAplicacao.setText(getText(R.string.btPararAplicacao));
         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = new Intent(MainActivity.this, PopulateDatabaseService.class);
        startService(in);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_READ_STATE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(tag, "permição obitida.");
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent in = null;
        switch (view.getId()) {
            case R.id.btn_VerDadosRedes :
                in = new Intent(MainActivity.this, NetUsageActivity.class);
                break;
            case R.id.btResultadoProcessamentoLexico:
                in = new Intent(MainActivity.this, LexicoResultActivity.class);
                break;
            case R.id.btIniciaAplicacao:
                configuraAplicacao();
                break;
            case R.id.btResultadoFinalClassificado:
                in = new Intent(MainActivity.this, LexicoResultadoFinal.class);
                break;
            case R.id.btHelpUsoInternet:
                mostrarDialogoAjuda(getString(R.string.btAjudaUsoDeInternet));
                break;
            case R.id.btHelpResultadoLexico:
                mostrarDialogoAjuda(getString(R.string.btAjudaResultadoLexico));
                break;
            case R.id.btHelpResultadoFinal:
                mostrarDialogoAjuda(getString(R.string.btAjudaResultadoFinal));
                break;
            case R.id.btHelpInicioParadaAplicacao:
                mostrarDialogoAjuda(getString(R.string.btAjudaIniciaParaAplicacao));
                break;
        }
        if(in != null)
            startActivity(in);
    }

    private void configuraAplicacao(){
        /*if(btInicaAplicacao.getText().equals(getString(R.string.btIniciarAplicacao))) {
            ConfigurationApp c = new ConfigurationApp(btInicaAplicacao);
            c.show(getSupportFragmentManager(), "ConfigurationApp");
        }else {
            Toast.makeText(this, "Parando aplicação.", Toast.LENGTH_LONG).show();
            LexicoWorker.stopWorker(this);
            MyAccessibilitiService.stopService(this);
            btInicaAplicacao.setText(getString(R.string.btIniciarAplicacao));
        }*/
        Intent in = new Intent(Intent.ACTION_SENDTO);
        in.setData(Uri.parse("mailto:"));
        in.putExtra(Intent.EXTRA_EMAIL, "alterar.email.para@real.com");
        in.putExtra(Intent.EXTRA_SUBJECT, "Dados de sentimentos de usuario");
        in.putExtra(Intent.EXTRA_TEXT, "sb.toString()");

            startActivity(in);
    }

    private void mostrarDialogoAjuda(String mensagem){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialogo_de_ajuda, null);
        TextView descricaoAjuda = v.findViewById(R.id.descricao_ajuda);
        descricaoAjuda.setText(mensagem);
        builder.setView(v).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

}