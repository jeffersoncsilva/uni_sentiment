package com.projetos.redes.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.adapters.ResultadoFinalAdapter;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.ResultadoFinalLexico;
import com.projetos.redes.task.SendMailTask;

import java.util.List;

public class ResultadoFinalActivity extends AppCompatActivity {
    private RecyclerView rc;
    private ResultadoFinalAdapter adapter;
    private Button bt_reload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_final);
        rc = findViewById(R.id.rc_resultado_final);
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultadoFinalAdapter(this);
        rc.setAdapter(adapter);
        bt_reload = findViewById(R.id.bt_reload_);
        bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PegarDadosBanco(getBaseContext()).execute();
            }
        });

        findViewById(R.id.btSendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarActivityEnviarEmail();
            }
        });
        setTitle("UniSentiment - Resultado final");
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
            extra.putString("ajuda_nome", getString(R.string.btAjudaResultadoFinal));
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
    protected void onStart() {
        super.onStart();
        new PegarDadosBanco(this).execute();
    }

    private void iniciarActivityEnviarEmail(){
        StringBuilder sb = new StringBuilder();
        sb.append("<html> <body>");
        for(ResultadoFinalLexico rf : adapter.getItems()){
            sb.append(rf.resultadoParaEnviar());
            sb.append("<br>");
        }
        sb.append("</body></html>");
        Log.d("HTMLEMAIL", sb.toString());
        SendMailTask task = new SendMailTask(this, sb.toString());
        task.execute();
    }

    protected class PegarDadosBanco extends AsyncTask<Void, Void, Void>{
        private List<ResultadoFinalLexico> lst;
        private final Context context;

        public PegarDadosBanco(Context con){
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reload.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BancoDeDados db = new BancoDeDados(context);
            lst = db.pegarResultadoFinal();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setData(lst);
            adapter.notifyDataSetChanged();
            bt_reload.setEnabled(true);
        }
    }
}
