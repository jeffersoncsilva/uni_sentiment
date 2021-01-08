package com.projetos.redes.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.projetos.redes.adapters.ResultadoFinalAdapter;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.ResultadoFinalLexico;

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
        for(ResultadoFinalLexico rf : adapter.getItems()){
            sb.append(rf.toString());
            sb.append("\n");
        }
        Intent in = new Intent(Intent.ACTION_SENDTO);
        in.setData(Uri.parse("mailto:"));
        in.putExtra(Intent.EXTRA_EMAIL, "alterar.email.para@real.com");
        in.putExtra(Intent.EXTRA_SUBJECT, "Dados de sentimentos de usuario");
        in.putExtra(Intent.EXTRA_TEXT, sb.toString());
        if(in.resolveActivity(getPackageManager()) != null)
            startActivity(in);
        else
            Toast.makeText(getApplicationContext(), "Por favor, instale um aplicativo de email para poder enviar os dados.", Toast.LENGTH_LONG).show();
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
