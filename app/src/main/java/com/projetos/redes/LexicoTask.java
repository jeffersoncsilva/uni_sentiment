package com.projetos.redes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projetos.redes.bd.LexicoDb;

/**
 * @author Jefferson C. Silva
 * Classe responsavel por executar em background ('uma nova thread') o
 * processamento referente ao analisador lexico.
 */
public class LexicoTask extends AsyncTask<String, String, String> {
    private static final String TAG = "LexicoTask";
    private Context mContext;
    private Button mBtLexico;
    private Button mBtService;
    private ProgressBar mLoagindBar;
    private TextView mTxtInstrucaoService;
    private TextView mInstrucaoLexico;
    private View separator;
    private Lexico lexico;


    private boolean executeByAlarm;

    public LexicoTask(Context context, Button blexico, Button service, ProgressBar loadingBar,
                      TextView instrucaoService, TextView instrucaoLexico, View separator){
        //this.mContext = context;
        lexico = Lexico.getLexico(context);
        this.mBtLexico = blexico;
        this.mBtService = service;
        this.mLoagindBar = loadingBar;
        this.mTxtInstrucaoService = instrucaoService;
        this.mInstrucaoLexico = instrucaoLexico;
        this.separator = separator;
        this.executeByAlarm = false;
    }

    public LexicoTask(Context con){
        lexico = Lexico.getLexico(con);
        this.mContext = con;
        this.executeByAlarm = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!this.executeByAlarm) {
            this.mBtLexico.setVisibility(View.GONE);
            this.mBtService.setVisibility(View.GONE);
            this.mTxtInstrucaoService.setVisibility(View.GONE);
            this.mInstrucaoLexico.setVisibility(View.GONE);
            this.separator.setVisibility(View.GONE);
            this.mLoagindBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        //Log.v(TAG, "Iniciando leitura de arquivos.");
        //long started = System.currentTimeMillis();
        String str = lexico.executarLexico();
        //long end = System.currentTimeMillis();
        //long diff = end - started;
        //Log.v(TAG, "Tempo execução: " + diff);
        //Log.v(TAG, str);
        return str;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!this.executeByAlarm) {
            this.mBtLexico.setVisibility(View.VISIBLE);
            this.mBtService.setVisibility(View.VISIBLE);
            this.mTxtInstrucaoService.setVisibility(View.VISIBLE);
            this.mInstrucaoLexico.setVisibility(View.VISIBLE);
            this.separator.setVisibility(View.VISIBLE);
            this.mLoagindBar.setVisibility(View.GONE);
        }else{
            Toast.makeText(this.mContext, "Lexico executado!", Toast.LENGTH_SHORT).show();
        }
    }
}
