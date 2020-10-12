package com.projetos.redes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.alerts.ConfiguraTempoLexicoDialog;
import com.projetos.redes.alerts.ReadPhoneDialog;
import com.projetos.redes.services.MyAccessibilitiService;
import com.projetos.redes.services.PopulateDatabaseService;
import com.projetos.redes.testes.InsertMessagesTest;
import com.projetos.redes.worker.LexicoWorker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag = "LexicoApp";
    public static final int PERMISSION_READ_STATE = 1234;
    public static final String LEXICO_CONFIG = "network_configs";
    public static final String LEXICO_TIME="time";
    private Button bt_start_service;
    private Button bt_acesso_dados;
    private Button tb_fone_autorizacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_VerDadosRedes).setOnClickListener(this);
        findViewById(R.id.btn_lst_usr_msg).setOnClickListener(this);
        findViewById(R.id.bt_lexico_result).setOnClickListener(this);
        findViewById(R.id.bt_result_final).setOnClickListener(this);
        bt_acesso_dados = findViewById(R.id.bt_acesso_dados);
        bt_acesso_dados.setOnClickListener(this);
        bt_start_service = findViewById(R.id.btn_stop_service_msg);
        bt_start_service.setOnClickListener(this);
        findViewById(R.id.bt_stop_lexico).setOnClickListener(this);
        tb_fone_autorizacao = findViewById(R.id.bt_acesso_mobile);
        tb_fone_autorizacao.setOnClickListener(this);
        new InsertMessagesTest(getApplicationContext()).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = new Intent(MainActivity.this, PopulateDatabaseService.class);
        startService(in);
        /*checaServicoAcessibiliidade();
        checaPermissaoAcessoAosDados();
        checaPermissaoAcessoAosDadosRede();
        configuraWorkerExecucao();
         */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_READ_STATE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(tag, "permição obitida.");
            }else{
                pedidoPermisaoFoneMobile();
                Log.d(tag, "permição não foi obitida.");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_VerDadosRedes :
                Intent in = new Intent(MainActivity.this, NetUsageActivity.class);
                startActivity(in);
                break;
            case R.id.btn_lst_usr_msg:
                Intent in2 = new Intent(MainActivity.this, ListUsrMsgs.class);
                startActivity(in2);
                break;
            case R.id.bt_lexico_result:
                Intent in3 = new Intent(MainActivity.this, LexicoResultActivity.class);
                startActivity(in3);
                break;
            case R.id.btn_stop_service_msg:
                showDialogAcessibilidade();
                break;
            case R.id.bt_stop_lexico:
                configuraLexico();
                break;
            case R.id.bt_result_final:
                Intent in4 = new Intent(MainActivity.this, LexicoResultadoFinal.class);
                startActivity(in4);
                break;
            case R.id.bt_acesso_dados:
                mostraPedidoPermisaoDeEstados();
                break;
            case R.id.bt_acesso_mobile:
                pedidoPermisaoFoneMobile();
                break;
        }
    }

    private void mostraPedidoPermisaoDeEstados(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.permission_checkForPermission));
        alert.setMessage(getString(R.string.permission_checkForPermission_desc));
        alert.setPositiveButton(getString(R.string.permission_authorize), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }).setNegativeButton(getString(R.string.bt_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bt_acesso_dados.setVisibility(View.VISIBLE);
            }
        });
        alert.show();
    }

    private void pedidoPermisaoFoneMobile(){
        DialogFragment f = new ReadPhoneDialog();
        f.show(getSupportFragmentManager(), "ReadPhoneDialog");
    }

    private void configuraLexico(){
        DialogFragment f = new ConfiguraTempoLexicoDialog();
        f.show(getSupportFragmentManager(), "ConfigraTempoDialog");
    }

    private void showDialogAcessibilidade(){
        final boolean enable = MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(MainActivity.this));
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.myAccessibilityService_title));
        alert.setMessage(getString(R.string.myAccessibilityService_desc));
        alert.setPositiveButton(getText(R.string.bt_continuar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        }).setNegativeButton(getString(R.string.bt_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!enable)
                    bt_start_service.setText(getString(R.string.bt_service_start));
                else
                    bt_start_service.setText(getString(R.string.bt_service_end));
            }
        });
        alert.show();
    }

    private void checaServicoAcessibiliidade(){
        final boolean enable = MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(MainActivity.this));
        if(!enable){
            showDialogAcessibilidade();
        }else{
            bt_start_service.setText(getString(R.string.bt_service_end));
        }
    }

    private void checaPermissaoAcessoAosDados(){
        boolean b;
        //checa permissão a acesso a dados do dispositivo.
        b = Utils.checkForPermission(this);
        if(!b){
            mostraPedidoPermisaoDeEstados();
        }else
            bt_acesso_dados.setVisibility(View.GONE);
    }

    private void checaPermissaoAcessoAosDadosRede(){
        boolean b = Utils.readPhoneStatePermission(this);
        if(!b){
            pedidoPermisaoFoneMobile();
            tb_fone_autorizacao.setVisibility(View.VISIBLE);
        }else
            tb_fone_autorizacao.setVisibility(View.GONE);
    }

    private void configuraWorkerExecucao(){
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int s =  pref.getInt(LEXICO_TIME, -1);
        WorkManager wManager = WorkManager.getInstance(this);
        PeriodicWorkRequest.Builder b;
        int min = 10;
        if(s > 0){
            b = new PeriodicWorkRequest.Builder(LexicoWorker.class, min, TimeUnit.MINUTES);
        }else{
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(LEXICO_TIME, min);
            editor.commit();
            b = new PeriodicWorkRequest.Builder(LexicoWorker.class, min, TimeUnit.MINUTES);
        }
        PeriodicWorkRequest req = b.build();
        wManager.enqueueUniquePeriodicWork(LexicoWorker.WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, req);
    }

}