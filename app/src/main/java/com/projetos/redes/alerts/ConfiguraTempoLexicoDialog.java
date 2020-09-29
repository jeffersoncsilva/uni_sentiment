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

import com.projetos.redes.activities.MainActivity;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.services.LexicoBroadcast;

import java.util.Objects;

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
        txMinutes.setText(String.format( getString(R.string.intervalo_minutos), ((sBar.getProgress()+1) * 10)));
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txMinutes.setText(String.format( getString(R.string.intervalo_minutos), ((i+1) * 10)));
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
                int min = (sBar.getProgress()+1)*10;
                SharedPreferences.Editor edit = getActivity().getSharedPreferences(MainActivity.LEXICO_CONFIG, Context.MODE_PRIVATE).edit();
                edit.putInt(MainActivity.LEXICO_TIME, min);
                edit.commit();
                Log.d(tag, "Salvo para executar a cada " + min + " minutos.");
                Utils.setAlarmLexico(getContext(), min);
            }
        }).setNegativeButton("Cancelar execução", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelarExecucao();
            }
        });
        return builder.create();
    }

    private void cancelarExecucao(){
        Context con = getContext();
        AlarmManager alarm = (AlarmManager) Objects.requireNonNull(con).getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(con, LexicoBroadcast.class);
        PendingIntent pin = PendingIntent.getBroadcast(con, LexicoBroadcast.REQUEST_CODE, in, 0);
        if(pin != null)
            alarm.cancel(pin);
        else
            Toast.makeText(con, "Alarme cancelado!", Toast.LENGTH_SHORT).show();
    }

}
