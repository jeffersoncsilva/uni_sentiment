package com.projetos.redes.fragments;


import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.projetos.redes.services.LexicoBroadcast;
import com.projetos.redes.services.MyAccessibilitiService;
import com.projetos.redes.R;

import java.util.Objects;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class FragmentHome extends Fragment implements View.OnClickListener {
    private static final String tag = "FragmentHome";
    private static final int PERMISSION_READ_STATE = 1234;
    private Button btLexico;
    private Button btService;
    private TextView txInstrucaoService, tx_minutes;
    private SeekBar sBarMinutes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btLexico = v.findViewById(R.id.btnLexico);
        btLexico.setOnClickListener(this);//listener do botao que inicia a execução do lexico manualmente.
        btService = v.findViewById(R.id.btService);// listener do botao que abre a tela de configuração ou para o acessibility service.
        btService.setOnClickListener(this);
        txInstrucaoService = v.findViewById(R.id.tx_service_desc);
        sBarMinutes = v.findViewById(R.id.sBarMinutes);
        tx_minutes = v.findViewById(R.id.tx_minutes);
        setMinutesText(sBarMinutes.getProgress() + 1);
        sBarMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMinutesText(i+1);
                Log.d(tag, "Progres: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return v;
    }

    private void setMinutesText(int min){
        tx_minutes.setText(String.format( getString(R.string.intervalo_minutos), (min * 10)));
    }

    /**
     * @author Jefferson C. Silva
     * Realiza a verificação se ja existe o arquivo de menssagens. Caso não exista, o usuario deve permitir o serviço de assecibilidade nas configurações
     * de acessibilidade do dispositivo, para poder ser feita a captura das menssagens que o usuário digita no whatsapp.
     */
    @Override
    public void onStart() {
        super.onStart();
        boolean p = checkForPermission(getContext());
        boolean q = readPhoneStatePermission(getContext());
        Log.d(tag, "checkForPermission: " + p);
        Log.d(tag, "readPhoneStatePermission: " + q);
        if(!p){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Autorização para obter dados de rede.");
            alert.setTitle("E necessario autorizar o app para a obter dados de rede para poder ter todas as suas funcionalidade.");
            alert.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
            alert.show();
            return;
        }
        if(!q){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Autorização para obter dados de rede.");
            alert.setTitle("E necessario autorizar o app para a obter dados de rede para poder ter todas as suas funcionalidade.");
            alert.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
                }
            });
            alert.show();
            return;
        }



       /*if(MyAccessibilitiService.serviceIsRunning(Objects.requireNonNull(getContext()))){
            this.btService.setText(getText(R.string.bt_service_end));
            this.txInstrucaoService.setText(getText(R.string.bt_service_end_descprition));
        }else{
            this.btService.setText(getText(R.string.bt_service_start));
            this.txInstrucaoService.setText(getText(R.string.bt_service_start_description));
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_READ_STATE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(tag, "permição obitida.");
            }else{
                Log.d(tag, "permição não foi obitida.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLexico){
            setAlarmLexico();
        }else if(v.getId() == R.id.btService){
            service();
        }
    }

    private void setAlarmLexico(){
        try {
            AlarmManager alarm = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
            Intent in = new Intent(getContext(), LexicoBroadcast.class);
            PendingIntent pen = PendingIntent.getBroadcast(getContext(), LexicoBroadcast.REQUEST_CODE, in, 0);
            int i = (sBarMinutes.getProgress() + 1) * 10;
            alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pen);

            SharedPreferences.Editor edit = getContext().getSharedPreferences("network_configs", Context.MODE_PRIVATE).edit();
            edit.putInt("time", i);
            edit.commit();


            Toast.makeText(getContext(), String.format(getString(R.string.str_alarm_set), i), Toast.LENGTH_LONG).show();
        }catch (NullPointerException ne){
            Log.e(tag, "Erro ao criar alarme. ERRO: " + ne.getMessage());
            ne.printStackTrace();
        }
    }

    private void service(){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());

        return mode == MODE_ALLOWED;
    }

    private boolean readPhoneStatePermission(Context c){
        return ContextCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }


}
