package com.projetos.redes.alerts;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.R;
import com.projetos.redes.worker.LexicoWorker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConfiguraTempoLexicoDialog extends DialogFragment {
    private String tag = "LexicoDialog";
    private SeekBar sBar;
    private TextView txMinutes;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
                int min = (sBar.getProgress()+1)*15;
                SharedPreferences.Editor edit = getActivity().getSharedPreferences(MainActivity.LEXICO_CONFIG, Context.MODE_PRIVATE).edit();
                edit.putInt(MainActivity.LEXICO_TIME, min);
                edit.commit();
                WorkManager wManager = WorkManager.getInstance(getContext());
                PeriodicWorkRequest.Builder b = new PeriodicWorkRequest.Builder(LexicoWorker.class, min, TimeUnit.MINUTES);
                PeriodicWorkRequest req = b.build();
                wManager.enqueueUniquePeriodicWork(LexicoWorker.WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, req);
                Log.d(tag, "Salvo para executar a cada " + min + " minutos.");
            }
        }).setNegativeButton("Cancelar execução", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}
