package com.projetos.redes.alerts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.services.MyAccessibilitiService;
import com.projetos.redes.worker.LexicoWorker;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConfigurationApp extends DialogFragment implements View.OnClickListener{
    private final static String tag = "ConfigurationApp";
    private static int autorizacoes = 0;
    private int workerInterval = 0;
    private Button btWorker, btAcessibilityService, btPhoneState, btAcessoDados, btPararAplicacao;

    public ConfigurationApp(){
        btPararAplicacao = null;
        autorizacoes = 0;
    }

    public ConfigurationApp(Button btPararAplicacao){
        this.btPararAplicacao  = btPararAplicacao;
        autorizacoes = 0;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_autorizacao, null);
        btWorker = v.findViewById(R.id.btWorker);
        btWorker.setOnClickListener(this);
        btAcessibilityService = v.findViewById(R.id.btAccessibitliyService);
        btAcessibilityService.setOnClickListener(this);
        btPhoneState = v.findViewById(R.id.btFoneEstate);
        if(Utils.readPhoneStatePermission(getContext())) {
            btPhoneState.setEnabled(false);
            autorizacoes++;
        }
        else
            btPhoneState.setOnClickListener(this);

        btAcessoDados = v.findViewById(R.id.btAcessoDado);
        if(Utils.checkForPermission(getContext())) {
            btAcessoDados.setEnabled(false);
            autorizacoes++;
        }else
            btAcessoDados.setOnClickListener(this);

        builder.setView(v).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(autorizacoes >= 4){
                    configuraWorker(workerInterval);
                    if(btPararAplicacao!=null)
                        btPararAplicacao.setText(getContext().getText(R.string.btPararAplicacao));
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btFoneEstate){
            readPhoneDialog();
        }
        else if(v.getId() == R.id.btAcessoDado){
            acessoDados();
        }else if(v.getId() == R.id.btAccessibitliyService){
            servicoCapturaMensagens();
        }else if(v.getId() == R.id.btWorker){
            configuraLexico();
        }
    }

    private void readPhoneDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.permission_readfohnestate));
        alert.setMessage(getString(R.string.permission_readphonestate_desc));
        alert.setPositiveButton(getString(R.string.permission_authorize), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.PERMISSION_READ_STATE);
                autorizacoes++;
                btPhoneState.setEnabled(false);
            }
        });
        alert.show();
    }

    private void acessoDados(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.permission_checkForPermission));
        alert.setMessage(getString(R.string.permission_checkForPermission_desc));
        alert.setPositiveButton(getString(R.string.permission_authorize), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btAcessoDados.setEnabled(false);
                autorizacoes++;
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        });
        alert.show();
    }

    private void servicoCapturaMensagens(){
        final boolean enable = MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(getContext()));
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.myAccessibilityService_title));
        alert.setMessage(getString(R.string.myAccessibilityService_desc));
        alert.setPositiveButton(getText(R.string.bt_continuar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btAcessibilityService.setEnabled(false);
                autorizacoes++;
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });
        alert.show();
    }

    private void configuraLexico(){
        final SeekBar sBar;
        final TextView txMinutes;
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_service_execution, null);
        sBar = v.findViewById(R.id.sBarMinutes);
        txMinutes = v.findViewById(R.id.tx_minutes);
        txMinutes.setText(String.format( getString(R.string.intervalo_minutos), ((sBar.getProgress()+1) * 15)));
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txMinutes.setText(String.format( getString(R.string.intervalo_minutos), ((i+1) * 15)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setView(v).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                workerInterval = (sBar.getProgress()+1)*15;
                autorizacoes++;
                btWorker.setEnabled(false);
                Log.d(tag, "time interval: " + workerInterval);
            }
        });
        builder.show();
    }

    private void configuraWorker(int min){
        WorkManager wManager = WorkManager.getInstance(getContext());
        PeriodicWorkRequest.Builder b = new PeriodicWorkRequest.Builder(LexicoWorker.class, min, TimeUnit.MINUTES).addTag(LexicoWorker.TAG_WORKER);
        PeriodicWorkRequest req = b.build();
        wManager.enqueueUniquePeriodicWork(LexicoWorker.WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, req);
    }

}