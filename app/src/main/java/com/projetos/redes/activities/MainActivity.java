package com.projetos.redes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.alerts.ConfiguraTempoLexicoDialog;
import com.projetos.redes.alerts.ReadPhoneDialog;
import com.projetos.redes.fragments.FragmentHome;
import com.projetos.redes.fragments.FragmentMessages;
import com.projetos.redes.services.MyAccessibilitiService;
import com.projetos.redes.services.PopulateDatabaseService;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag = "LexicoApp";
    public static final int PERMISSION_READ_STATE = 1234;

    public static final String LEXICO_CONFIG = "network_configs";
    public static final String LEXICO_TIME="time";

    private Button bt_start_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_VerDadosRedes).setOnClickListener(this);
        findViewById(R.id.btn_lst_usr_msg).setOnClickListener(this);
        findViewById(R.id.bt_lexico_result).setOnClickListener(this);
        bt_start_service = findViewById(R.id.btn_stop_service_msg);
        bt_start_service.setOnClickListener(this);
        findViewById(R.id.bt_stop_lexico).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent in = new Intent(MainActivity.this, PopulateDatabaseService.class);
        startService(in);

        boolean b;

        b = MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(MainActivity.this));
        Log.d(tag, "MyAccessibilityService.serviceIsRunning: " + b);
        if(!b){
            pedeInicarMyAcessibilitiService();
            return;
        }else{
            bt_start_service.setText(getString(R.string.bt_service_end));
        }

        //checa permissão a acesso a dados do dispositivo.
        b = Utils.checkForPermission(this);
        Log.d(tag, "checkForPermission: " + b);
        if(!b){
            mostraPedidoPermisaoDeEstados();
            return;
        }

        b = Utils.lexicoJaConfigurado(MainActivity.this, LEXICO_TIME);
        Log.d(tag, "lexicoJaConfigurado: " + b);
        if(!b){
            configuraLexico();
        }

        // checa permissão de acesso a dados do telefone do dispositivo.
        b = Utils.readPhoneStatePermission(this);
        Log.d(tag, "readPhoneStatePermission: " + b);
        if(!b){
            mostraPedidoPermisao();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_READ_STATE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(tag, "permição obitida.");
            }else{
                mostraPedidoPermisao();
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
                    pedeInicarMyAcessibilitiService();
                break;
            case R.id.bt_stop_lexico:
                configuraLexico();
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
        });
        alert.show();
    }

    private void mostraPedidoPermisao(){
        DialogFragment f = new ReadPhoneDialog();
        f.show(getSupportFragmentManager(), "ReadPhoneDialog");
    }

    private void pedeInicarMyAcessibilitiService(){
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
                bt_start_service.setText(getString(R.string.bt_service_start));
            }
        });
        alert.show();
    }

    private void configuraLexico(){
        DialogFragment f = new ConfiguraTempoLexicoDialog();
        f.show(getSupportFragmentManager(), "ConfigraTempoDialog");
    }

}