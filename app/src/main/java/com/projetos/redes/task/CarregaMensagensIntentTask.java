package com.projetos.redes.task;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.adapters.MostraMensagensAdapter;
import com.projetos.redes.modelos.MensagemUsuario;
import com.projetos.redes.utilidades.Data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CarregaMensagensIntentTask  extends AsyncTask<Void, Void, Void> {
    private String TAG = "MsgTask";
    private Intent intent;
    private List<MensagemUsuario> mensagens;
    private List<MensagemUsuario> autores;
    private ProgressBar pBarCarregando;
    private Button iniciarLexico;
    private RecyclerView rcMensagens;
    private MostraMensagensAdapter adapter;
    private Context mContext;

    public CarregaMensagensIntentTask(Intent in, Context context, ProgressBar pBarCarregando, Button iniciarLexico, RecyclerView rc, MostraMensagensAdapter adp){
        this.intent = in;
        this.mContext = context;
        this.pBarCarregando = pBarCarregando;
        this.iniciarLexico = iniciarLexico;
        this.rcMensagens = rc;
        this.adapter = adp;
        this.mensagens = new ArrayList<>();
        this.autores = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pBarCarregando.setVisibility(View.VISIBLE);
        iniciarLexico.setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            ClipData data = intent.getClipData();
            for(int i = 0; i < data.getItemCount(); i++){
                Uri uri = data.getItemAt(i).getUri();
                InputStream stream = mContext.getContentResolver().openInputStream(uri);
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader bReader = new BufferedReader(reader);
                String str;
                while((str=bReader.readLine())!=null){
                    int p1 = str.indexOf('-');
                    int p2 = str.indexOf(':',p1);
                    if(p2 < 0)
                        continue;
                    String dt = str.substring(0, p1);
                    String au = str.substring(p1, p2).replaceAll("-", "");
                    String mg = str.substring(++p2);
                    if(mg.contains("<Arquivo de mídia"))
                        continue;
                    MensagemUsuario msg = new MensagemUsuario(new Data(dt), au, mg);
                    if(!autores.contains(msg))
                        autores.add(msg);
                    mensagens.add(msg);
                }
            }
        }catch (Exception e){
            Log.e(TAG, "Ocorreu um erro ao ler as mensagens.\n");
            Log.e(TAG, "ERRO: " + e.getMessage()+"\n");
            Log.e(TAG, "toString(): "+e.toString()+"\n");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pBarCarregando.setVisibility(View.GONE);
        adapter.setMensagens(mensagens);
        adapter.setAutores(autores);
        adapter.notifyDataSetChanged();
        rcMensagens.setVisibility(View.VISIBLE);
        iniciarLexico.setVisibility(View.VISIBLE);
    }

}
